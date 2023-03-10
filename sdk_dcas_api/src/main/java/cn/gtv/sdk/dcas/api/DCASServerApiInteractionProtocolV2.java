package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.net.Uri;

import com.sad.jetpack.v1.datamodel.api.IDataModelChainInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelInterceptorInput;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;

public class DCASServerApiInteractionProtocolV2 implements IDataModelInterceptorInput<String> {
    private Context context;
    private String serverToken;
    public DCASServerApiInteractionProtocolV2(Context context,String serverToken) {
        this.context = context;
        this.serverToken=serverToken;
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
        String path=Uri.parse(url).getPath();
        String ts=(System.currentTimeMillis()/1000)+"";
        JSONObject json_event=json.optJSONObject("event");
        String event_type=json_event.optString("type");
        String event_target=json_event.optString("target");
        String product=json.optJSONObject("info").optString("product");
        String data=product+event_type+event_target;
        String signature=createSignature(path,data,ts);
        //开始封装实际提交的json
        JSONObject newJson=new JSONObject();
        newJson.put("eventData",json);
        newJson.put("signature",signature);
        newJson.put("ts",ts);
        LogcatUtils.e("------------->埋点实际提交数据："+newJson.toString(4));
        requestCreator.body(newJson.toString());
        request=requestCreator.create();
        chainInput.proceed(request,null,chainInput.currIndex());
    }

    public static String createSignature(String path, String data, String ts){
        BigInteger b = new BigInteger(ts.trim());
        String hex_ts=Integer.toHexString(b.intValue());
        String s=(path+data+hex_ts).toLowerCase()+ getXXX();
        LogcatUtils.e("------------->path="+path);
        LogcatUtils.e("------------->data="+data);
        LogcatUtils.e("------------->hex_ts="+hex_ts);
        LogcatUtils.e("------------->SIGN_KEY="+getXXX());
        String ms= encodeToMD5(s.getBytes()).toLowerCase();
        LogcatUtils.e("------------->signature="+ms);
        return ms;
    }

    private static String getXXX() {
        return DCASCore.globalConfig.getServerAuthKeyV2();
    }
    private static String encodeToMD5(byte[] btInput){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            //byte[] btInput = s.getBytes();
            // 获得MD5摘要算法�?MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘�?
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
