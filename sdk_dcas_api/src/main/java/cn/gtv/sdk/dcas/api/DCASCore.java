package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.os.Build;

import com.sad.basic.utils.app.AppInfoUtil;
import com.sad.jetpack.v1.datamodel.api.GlobalDataModelConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultCacheLoader;

import org.json.JSONObject;

public final class DCASCore {

    protected static Context mContext=null;
    protected static String mServerAuthKeyV3 ="FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8";
    public static void init(Context context,String serverAuthKeyV3,boolean privacyAgree){
        if (context==null && mContext==null && privacyAgree){
            mContext=context;
            mServerAuthKeyV3=serverAuthKeyV3;
            DefaultCacheLoader.initCacheLoader(mContext);
            GlobalDataModelConfig.getInstance().enableLogUtils(true);
            String currAppProcessName=AppInfoUtil.getCurrAppProccessName(mContext);
            if (context.getPackageName().equals(currAppProcessName)){
                //主进程下处理
            }
        }
    }


    private static JSONObject setupBaseDeviceInfoJson(Context context,JSONObject jsonObject){
        try {
            jsonObject.put("sys_os", "Android");
            jsonObject.put("sys_os_version", DeviceTools.getPhone_System(context)+"");
            jsonObject.put("device_model",DeviceTools.getPhone_Model(context)+"");
            jsonObject.put("device_manufacturer", Build.MANUFACTURER+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
    private static JSONObject setupBaseAppInfoJson(Context context,JSONObject jsonObject){
        try {
            jsonObject.put("app_version_code", AppInfoUtil.getVersionCode(context)+"");
            jsonObject.put("app_version_name", AppInfoUtil.getVersionName(context)+"");
            jsonObject.put("device_model",DeviceTools.getPhone_Model(context)+"");
            jsonObject.put("device_manufacturer", Build.MANUFACTURER+"");
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static JSONObject createBaseJson(Context context){
        JSONObject jsonObject=new JSONObject();
        try {
            setupBaseDeviceInfoJson(context,jsonObject);
            setupBaseAppInfoJson(context,jsonObject);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
