package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;

public class HttpDataClient extends DataClient<HttpDataClient>{
    private String serverToken="";
    private HttpDataClient(String serverToken){
        super();
        this.serverToken=serverToken;
    }
    public static HttpDataClient newInstance(String serverToken){
        return new HttpDataClient(serverToken);
    }

    @Override
    public IDataModelProducer<String> dataModelProducer(Context context, String tagAndClientKey, boolean internalLog){
        IDataModelRequest request=requestCreator.tag(tagAndClientKey).create();
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