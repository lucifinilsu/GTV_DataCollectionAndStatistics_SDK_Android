package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.os.Build;

import com.sad.basic.utils.app.AppInfoUtil;

import org.json.JSONObject;

public class TrackerDataCreator {
    /**
     * "info": {
     *         "token":"asdfgh12345",
     *         "ip":"127.0.0.1",
     *         "client":"APP",//客户端类型
     *         "client_version":"7.2.3",//客户端版本
     *         "sys_os":"Andriod",//系统名称
     *         "sys_os_version":"12.3",//系统版本
     *         "device_manufacturer",//设备厂商/品牌
     *         "device_model",//设备型号
     *
     *     }
     *     "detail": {
     *         "mc":"动静贵州",
     *         "nav1":"资讯",
     *         "nav2":"教育",
     *         "content_type":"news.article",
     *         "id":"896054",
     *         "title":"测试文章",
     *         "source_type":"newscolumn",
     *         "source_id":203,
     *         "source_title":"热门推荐",
     *         "rate":"28%",
     *         "share_token":"qwrfssdf423",
     *         "share_appuser_id":"134909",
     *         "start":"2023-01-30 10:34:55",
     *         "end":"2023-01-30 10:36:02",
     *     }
     *
     *     "event": {
     *         "type":"hit",//事件类型
     *         "target":"navi",//事件目标（名称）
     *     },
     */

    public static JSONObject createTrackerData(Context context, String serverToken, String type, String target, JSONObject detailJson){
        JSONObject eventJson=createEventJson(context,type,target);
        return createTrackerData(context,serverToken,eventJson,detailJson);
    }
    public static JSONObject createTrackerData(Context context,String serverToken,JSONObject evenJson,JSONObject detailJson){
        JSONObject trackerData=new JSONObject();
        try {
            JSONObject infoJson=createInfoJson(context,serverToken);
            trackerData.put("event",evenJson);
            trackerData.put("info",infoJson);
            trackerData.put("detail",detailJson);
        }catch (Exception e){
            e.printStackTrace();
        }
        return trackerData;
    }

    public static JSONObject createEventJson(Context context,String type,String target){
        JSONObject event=new JSONObject();
        try {
            event.put("type",type);
            event.put("target",target);
        }catch (Exception e){
            e.printStackTrace();
        }
        return event;
    }

    public static JSONObject createInfoJson(Context context,String serverToken){
        JSONObject info=new JSONObject();
        try {
            info.put("token",serverToken);
            info.put("client","APP");
            setupBaseDeviceInfoJson(context,info);
            setupBaseAppInfoJson(context,info);
        }catch (Exception e){
            e.printStackTrace();
        }

        return info;
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
            jsonObject.put("client_version_code", AppInfoUtil.getVersionCode(context)+"");
            jsonObject.put("client_version", AppInfoUtil.getVersionName(context)+"");
            jsonObject.put("device_model",DeviceTools.getPhone_Model(context)+"");
            jsonObject.put("device_manufacturer", Build.MANUFACTURER+"");
            jsonObject.put("product",DCASCore.globalConfig.getProduct());
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }
}
