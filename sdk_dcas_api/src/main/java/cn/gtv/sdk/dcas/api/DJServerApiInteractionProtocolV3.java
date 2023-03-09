package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.utils.MapTraverseUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DJServerApiInteractionProtocolV3 implements IDataModelInterceptorInput<String> {
    private String serverToken="";
    private Context context;
    public DJServerApiInteractionProtocolV3(Context context,String serverToken) {
        this.serverToken = serverToken;
        this.context = context;
    }

    @Override
    public void onInterceptedInput(IDataModelChainInput<String> chainInput) throws Exception {
        IDataModelRequest request=chainInput.request();
        IDataModelRequest.Method method=request.method();
        IDataModelRequest.Creator requestCreator=request.toCreator();
        String body=request.body();
        String url= request.url();
        requestCreator.addHeader("Authorization","DJ "+ serverToken);
        JSONObject json=new JSONObject(body);
        Map<String, Object> signedMap=signV3(serverToken,json,false);
        if (method== IDataModelRequest.Method.GET){
            //接下来将字典配置给url
            Uri uri=Uri.parse(url);
            Uri.Builder b=uri.buildUpon();
            MapTraverseUtils.traverseGroup(signedMap, new MapTraverseUtils.ITraverseAction<String, Object>() {
                @Override
                public void onTraversed(String s, Object o) {
                    b.appendQueryParameter(s,o+"");
                }
            });
            String u_p=b.build().toString();
            requestCreator.url(u_p);
            request=requestCreator.create();
        }

        else if (method== IDataModelRequest.Method.POST){
            JSONObject newJson=new JSONObject();
            if (signedMap != null) {
                Iterator<Map.Entry<String, Object>> it = signedMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = it.next();
                    try {
                        newJson.put(entry.getKey(), entry.getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                requestCreator.body(newJson.toString());
                request=requestCreator.create();
            }

        }

        chainInput.proceed(request,null,chainInput.currIndex());
    }


    public Map<String, Object> signV3(String token, JSONObject p, boolean useTSNum) throws Exception{
        String curr=System.currentTimeMillis()+"";
        String ts=curr.substring(0,10);
        BigInteger b = new BigInteger(ts.trim());
        String hex_ts=Integer.toHexString(b.intValue());
        String nonce= RandomStringUtils.random(10,true,false);
        Map<String,Object> parametersMap=new HashMap<>();
        parametersMap.put("ts", useTSNum ?ts:hex_ts);
        parametersMap.put("nonce",nonce);
        parametersMap.put("device","Android");
        parametersMap.put("ver", getVersionCode(DCASCore.mContext));
        parametersMap.put("token",token);
        if (p!=null){
            Iterator<String> ks=p.keys();
            while (ks.hasNext()){
                String k=ks.next();
                parametersMap.put(k,p.opt(k));
            }
        }
        String s=doSign(parametersMap);
        parametersMap.put("signature",s);
        parametersMap.remove("token");
        return parametersMap;
    }
    private String doSign(Map<String,Object> parametersMap) throws Exception{
        Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator());
        sortMap.putAll(parametersMap);
        String[] signOrgStringArray=new String[sortMap.size()+1];
        String sp="&";

        MapTraverseUtils.traverseGroup(sortMap, new MapTraverseUtils.ITraverseAction<String, Object>() {
            int i=0;
            @Override
            public void onTraversed(String s, Object o) {
                String pair=s.toLowerCase()+"="+o;
                signOrgStringArray[i]=pair;
                i++;
            }
        });
        String keyPair="key="+ DCASCore.globalConfig.getServerAuthKeyV3();
        signOrgStringArray[sortMap.size()]=keyPair;
        /*String org= StringUtils.join(signOrgStringArray,sp);*/
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<signOrgStringArray.length;i++){
            if (i!=0){
                sb.append(sp);
            }
            sb.append(signOrgStringArray[i]);
        }
        String org= sb.toString();
        //LogPrinterUtils.e(">>>待签名的参数："+org);
        return doHMAC_SHA256(org, DCASCore.globalConfig.getServerAuthKeyV3());
    }
    private String doHMAC_SHA256(String data,String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }
    private static class MapKeyComparator implements Comparator<String> {
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    private int getVersionCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
        return verCode;
    }
}
