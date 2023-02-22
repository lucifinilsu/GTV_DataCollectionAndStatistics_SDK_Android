package cn.gtv.sdk.dcas.api;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

public class DeviceTools {



	@SuppressLint("MissingPermission")
    public static String getPhone_IMEI(Context context) {
        String s = "";
        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            s=manager.getDeviceId();
        } catch(Exception localException1) {
        }
        return s;
    }
    
    @SuppressLint("MissingPermission")
    public static String getSIM_IMEI(Context context) {
        String s = "";
        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            s=manager.getSimSerialNumber();
        } catch(Exception localException1) {
        }
        return s;
    }
    
    @SuppressLint("MissingPermission")
    public static String getPhone_IMSI(Context context) {
        String s = "";
        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            s=manager.getSubscriberId();
        } catch(Exception localException1) {
        }
        return s;
    }
    
    public static String getSIM_NAME(Context context) {
        String s = "";
        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            s=manager.getSimOperatorName();
        } catch(Exception localException1) {
        }
        return s;
    }
    
    @SuppressLint("MissingPermission")
    public static String getSIM_NUM(Context context) {
        String s = "";
        try {
            TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
            s=manager.getLine1Number();
        } catch(Exception localException1) {
        }
        return s;
    }
    
    public static String getPhone_Model(Context context) {
        String s = "";
        try {
            return Build.MODEL;
        } catch(Exception localException1) {
        }
        return s;
    }
    
    public static String getPhone_System(Context context) {
        String s = "";
        try {
            return Build.VERSION.RELEASE;
        } catch(Exception localException1) {
        }
        return s;
    }
    
    public static String getMAC(Context context) {
        String macAddress = "";
        try {
            WifiManager wifiMgr = (WifiManager)context.getSystemService(WIFI_SERVICE);
            WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
            if(info != null) {
                macAddress = info.getMacAddress();
            }			
		} catch (Exception e) {

		}

        return macAddress;
    }
    
    public static long getAvaliableMEM(Context context) {
        long m = 0x0;
        try{
        	ActivityManager _ActivityManager = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo minfo = new ActivityManager.MemoryInfo();
            _ActivityManager.getMemoryInfo(minfo);
            System.out.println(minfo.availMem);
            return minfo.availMem;
        }catch (Exception e) {
        	return m;
		}
        
    }
 // 获得总内存
    public static long getTotalMEM() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

	content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }
    /*public static String getAPILevel(Context context){
    	String l="";
    	try{
        	int version = android.provider.Settings.System.getInt(context
          		     .getContentResolver(),
          		     android.provider.Settings.System.SYS_PROP_SETTING_VERSION,
          		   
          		     3);
        	l=version+"";
    	}catch (Exception e) {

		}

    	return l;
    }*/
    public static HashMap<String, String> getPhoneInfo(Context context) {
        HashMap<String, String> info = new HashMap<String, String>();
        TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String p_model = getPhone_Model(context);
        String p_system = getPhone_System(context);
        String p_imei = getPhone_IMEI(context);
        String p_mac = getMAC(context);
        info.put("device_model", p_model);
        info.put("device_system", p_system);
        //info.put("device_imei", p_imei);
        //info.put("device_mac", p_mac);
        
        String p_imsi="";
        String s_imei="";
        String s_name="";
        String s_num="";
        if(manager.getSimState() == 0x5) {
            p_imsi = getPhone_IMSI(context);
            s_imei = getSIM_IMEI(context);
            s_name = getSIM_NAME(context);
            s_num = getSIM_NUM(context);
        }
        info.put("device_imsi", p_imsi);
        info.put("sim_imei", s_imei);
        info.put("sim_name", s_name);
        info.put("sim_num", s_num);
        info.put("device_LocationIP",getLocalIpAddress());
        info.put("device_AvaliableMEM",getAvaliableMEM(context)+"");
        //info.put("device_APILevel", getAPILevel(context));
        //info.put("self_version",SADBaseApplication.getVerName());
        //info.put("userUsername", AppStaticConfig.CurrUser.getUserName());
        return info;
    }
    
    public static String getPhoneInfoToUrlParams(Context context){
    	String p="";
    	TelephonyManager manager = (TelephonyManager)context.getSystemService(TELEPHONY_SERVICE);
        String p_model = getPhone_Model(context);
        String p_system = getPhone_System(context);
        String p_imei = getPhone_IMEI(context);
        String p_mac = getMAC(context);
        p+=CreatePModel(p,"phone_model", p_model);
        p+=CreatePModel(p, "phone_system", p_system);
        p+=CreatePModel(p, "phone_imei", p_imei);
        p+=CreatePModel(p,"phone_mac", p_mac);
        
        
        String p_imsi="";
        String s_imei="";
        String s_name="";
        String s_num="";
        if(manager.getSimState() == 0x5) {
            p_imsi = getPhone_IMSI(context);
            s_imei = getSIM_IMEI(context);
            s_name = getSIM_NAME(context);
            s_num = getSIM_NUM(context);
        }
        p+=CreatePModel(p,"phone_imsi", p_imsi);
        p+=CreatePModel(p, "sim_imei", s_imei);
        p+=CreatePModel(p, "sim_name", s_name);
        p+=CreatePModel(p, "sim_num", s_num);
        p+=CreatePModel(p, "phone_LocationIP",getLocalIpAddress());
        p+=CreatePModel(p, "phone_AvaliableMEM",getAvaliableMEM(context)+"");
        //p+=CreatePModel(p, "phone_APILevel", getAPILevel(context));
        return p;
    	
    }
    private static String CreatePModel(String p,String key,String val){
    	return ("".endsWith(val)?"":"&"+key+"="+val);
    }
    public static String getLocalIpAddress() {
		String ip="";
	     try {
	         for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	             NetworkInterface intf = en.nextElement();
	             for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                 InetAddress inetAddress = enumIpAddr.nextElement();
	                 if (!inetAddress.isLoopbackAddress()) {
	                     ip= inetAddress.getHostAddress().toString();
	                 }
	             }
	         }
	     } catch (SocketException ex) {
	         //Log.e("获取IP异常", ex.toString());
	     }
	     return ip;
	} 
    
    public static void Test(Application app){
    	String name=app.getPackageName();
    	PackageManager pm =app.getPackageManager();
    	String appname=app.getApplicationInfo().loadLabel(pm).toString();
    	//LogPrinterUtils.e("包名："+name+"   应用名："+appname);
    	/*if(!"cn.gzmovement".equals(name)
    			|| SADBaseApplication.CurrApplication==null
    			||!"动静新闻".equals(appname)
    			){
    		System.exit(0);
			//throw new Exception("illegal possession!Please do not use this procedure without authorization!");
		}  */ 	
    }

}
