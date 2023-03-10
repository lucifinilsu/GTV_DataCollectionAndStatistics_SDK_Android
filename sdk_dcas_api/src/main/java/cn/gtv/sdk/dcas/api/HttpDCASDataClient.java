package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.extension.client.DataClient;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultStringCacheDataModelInterceptor;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;

public class HttpDCASDataClient extends DataClient<HttpDCASDataClient> {
    private String serverToken="";
    private Context context;
    private HttpDCASDataClient(Context context, String serverToken){
        super();
        this.serverToken=serverToken;
        this.context=context;
    }
    public static HttpDCASDataClient newInstance(Context context, String serverToken){
        return new HttpDCASDataClient(context,serverToken);
    }

    @Override
    public IDataModelProducer<String> dataModelProducer(String tagAndClientKey){
        IDataModelRequest request=requestCreator.tag(tagAndClientKey).create();
        LogDataModelInterceptor logInterceptor = LogDataModelInterceptor.newInstance();
        DefaultStringCacheDataModelInterceptor cacheInterceptor = new DefaultStringCacheDataModelInterceptor(context);
        IDataModelProducer<String> dataModelProducer = DataModelProducerImpl.<String>newInstance();
        dataModelProducer.addInputInterceptor(new DCASServerApiInteractionProtocolV2(context,serverToken));
        if (isInternalLog()){
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
