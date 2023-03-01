package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;

import org.json.JSONObject;

public class DCASTrackerImpl implements ITacker,ITacker.Builder{

    private JSONObject eventJson=new JSONObject();
    private JSONObject detailJson =new JSONObject();
    private Context context;

    private DCASTrackerImpl(){}
    private DCASTrackerImpl(Context context){
        this.context=context;
    }

    public static ITacker newInstance(Context context){
        return new DCASTrackerImpl(context);
    }

    @Override
    public void post(String serverToken) {
        JSONObject data=TrackerDataCreator.createTrackerData(context,serverToken,eventJson, detailJson);
        HttpDJDataClient.newInstance(context,serverToken)
                .log(true)
                .url("")
                .params(data)
                .method(IDataModelRequest.Method.POST)
                .dataModelProducer("dcas_tracker")
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
    public ITacker build() {
        return this;
    }
}
