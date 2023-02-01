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

public class HttpDataClient {
    private String serverToken="";
    private IDataModelRequest.Creator requestCreator=null;
    private HttpDataClient(String serverToken){
        this.serverToken=serverToken;
        this.requestCreator= DataModelRequestImpl.newCreator();
    }

    public static HttpDataClient newInstance(String serverToken){
        return new HttpDataClient(serverToken);
    }

    public HttpDataClient url(String url){
        this.requestCreator.url(url);
        return this;
    }

    public HttpDataClient method(IDataModelRequest.Method method){
        this.requestCreator.method(method);
        return this;
    }

    public HttpDataClient params(JSONObject jsonObject){
        this.requestCreator.body(jsonObject.toString());
        return this;
    }

    public HttpDataClient request(IDataModelRequest request){
        this.requestCreator=request.toCreator();
        return this;
    }

    public IDataModelProducer<String> dataModelProducer(Context context,String tagAndCacheKey,boolean internalLog){
        IDataModelRequest request=requestCreator.tag(tagAndCacheKey).create();
        LogDataModelInterceptor logInterceptor = LogDataModelInterceptor.newInstance();
        DefaultStringCacheDataModelInterceptor cacheInterceptor = new DefaultStringCacheDataModelInterceptor(context);
        IDataModelProducer<String> dataModelProducer = DataModelProducerImpl.<String>newInstance();
        dataModelProducer.addInputInterceptor(new DJServerApiInteractionProtocolV3(context,serverToken));
        if (internalLog){
            dataModelProducer.addInputInterceptor(logInterceptor).addOutputInterceptor(logInterceptor);
        }
        if (request.method()== IDataModelRequest.Method.GET){
            dataModelProducer.addInputInterceptor(cacheInterceptor).addOutputInterceptor(cacheInterceptor);
        }
        dataModelProducer
                .request(request)
                .engine(new OkhttpEngineForStringByStringBody());
        return dataModelProducer;
    }
}
