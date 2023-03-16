package cn.gtv.sdk.dcas.api;

import org.json.JSONObject;

public interface ITacker {

    JSONObject event();

    JSONObject detail();

    String tag();

    IServerTokenFactory serverTokenFactory();

    void post();

    Builder toBuilder();

    interface Builder{

        Builder event(JSONObject eventJson);

        Builder event(String type,String target,String id);

        Builder detail(JSONObject detailJson);

        Builder tag(String tag);

        Builder serverFactory(IServerTokenFactory serverTokenFactory);

        ITacker build();
    }

}
