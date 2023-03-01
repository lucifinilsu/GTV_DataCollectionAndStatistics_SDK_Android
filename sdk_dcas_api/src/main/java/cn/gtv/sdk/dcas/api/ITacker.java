package cn.gtv.sdk.dcas.api;

import org.json.JSONObject;

public interface ITacker {

    void post(String serverToken);

    Builder toBuilder();

    interface Builder{

        Builder event(JSONObject eventJson);

        Builder event(String type,String target);

        Builder detail(JSONObject detailJson);

        ITacker build();
    }

}
