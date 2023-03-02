package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.json.JSONObject;

public class DCASTrackerImpl implements ITacker,ITacker.Builder{

    private JSONObject eventJson=new JSONObject();
    private JSONObject detailJson =new JSONObject();
    private Context context;
    private String tck="dcas_tracker";

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
    public void post(String serverToken) {
        JSONObject data=TrackerDataCreator.createTrackerData(context,serverToken,eventJson, detailJson);
        HttpDJDataClient.newInstance(context,serverToken)
                .log(true)
                .url("")
                .params(data)
                .method(IDataModelRequest.Method.POST)
                .dataModelProducer(tck)
                .execute()
                ;
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
    public Builder event(String type, String target) {
        try {
            this.eventJson.put("type",type);
            this.eventJson.put("target",target);
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
    public ITacker build() {
        return this;
    }
}
