package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.DataModelRequestImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;
import org.json.JSONObject;

public abstract class DataClient<D extends DataClient<D>> {

    protected IDataModelRequest.Creator requestCreator=null;
    protected DataClient(){
        this.requestCreator= DataModelRequestImpl.newCreator();
    }


    public D url(String url){
        this.requestCreator.url(url);
        return (D) this;
    }

    public D method(IDataModelRequest.Method method){
        this.requestCreator.method(method);
        return (D) this;
    }

    public D params(JSONObject jsonObject){
        this.requestCreator.body(jsonObject.toString());
        return (D) this;
    }
    public D params(String s){
        this.requestCreator.body(s);
        return (D) this;
    }

    public D request(IDataModelRequest request){
        this.requestCreator=request.toCreator();
        return (D) this;
    }

    public abstract IDataModelProducer<String> dataModelProducer(Context context,String tagAndClientKey,boolean internalLog);
}
