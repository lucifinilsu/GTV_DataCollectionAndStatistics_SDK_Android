package cn.gtv.sdk.dcas.api;

import org.json.JSONObject;

public interface ITacker {

    JSONObject event();

    JSONObject detail();

    String tag();

    void post(String serverToken);

    Builder toBuilder();

    interface Builder{

        Builder event(JSONObject eventJson);

        Builder event(String type,String target);

        Builder detail(JSONObject detailJson);

        Builder tag(String tag);

        ITacker build();
    }

}
