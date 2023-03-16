package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class DCASTrackerImpl implements ITacker,ITacker.Builder{

    private JSONObject eventJson=new JSONObject();
    private JSONObject detailJson =new JSONObject();
    private Context context;
    private String tck="dcas_tracker";
    private IServerTokenFactory serverTokenFactory= DCASCore.globalConfig.getServerTokenFactory();
    private DCASTrackerImpl(){}
    private DCASTrackerImpl(Context context){
        this.context=context;
    }

    public static ITacker.Builder newBuilder(Context context){
        return new DCASTrackerImpl(context);
    }

    @Override
    public JSONObject event() {
        return this.eventJson;
    }

    @Override
    public JSONObject detail() {
        return this.detailJson;
    }

    @Override
    public String tag() {
        return this.tck;
    }

    @Override
    public IServerTokenFactory serverTokenFactory() {
        return this.serverTokenFactory;
    }

    @Override
    public void post() {
        if (serverTokenFactory!=null){
            serverTokenFactory.onCreateServerToken(new IServerTokenConsumer() {
                @Override
                public void optServerToken(String serverToken) {
                    doPost(serverToken);
                }
            });
        }
    }

    private void doPost(String serverToken) {
        JSONObject data=TrackerDataCreator.createTrackerData(context,serverToken,eventJson, detailJson);
        try {
            HttpDCASDataClient.newInstance(context,serverToken)
                    .log(true)
                    .url("https://eventlog-api.gzstv.com/data-upload/submit")
                    .params(data.toString())
                    .method(IDataModelRequest.Method.POST)
                    .dataModelProducer(tck)
                    .execute()
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*HttpDJDataClient.newInstance(context,serverToken)
                .log(true)
                .url("")
                .params(data.toString())
                .method(IDataModelRequest.Method.POST)
                .dataModelProducer(tck)
                .execute()
                ;*/
    }

    @Override
    public Builder toBuilder() {
        return this;
    }


    @Override
    public Builder event(JSONObject eventJson) {
        this.eventJson=eventJson;
        return this;
    }

    @Override
    public Builder event(String type, String target,String id) {
        try {
            this.eventJson.put("type",type);
            this.eventJson.put("target",target);
            this.eventJson.put("id",id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return this;
    }

    @Override
    public Builder detail(JSONObject detailJson) {
        this.detailJson=detailJson;
        return this;
    }

    @Override
    public Builder tag(String tag) {
        this.tck=tag;
        return this;
    }

    @Override
    public Builder serverFactory(IServerTokenFactory serverTokenFactory) {
        this.serverTokenFactory=serverTokenFactory;
        return this;
    }

    @Override
    public ITacker build() {
        return this;
    }

}
