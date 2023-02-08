package cn.gtv.sdk.dcas.api;

import android.content.Context;

import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelProducer;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;

public class WebSocketDataClient extends DataClient<WebSocketDataClient> implements IWebSocketMessenger{

    private OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener;
    private OkHttpWebSocketEngine engine;

    private WebSocketDataClient(OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener){
        super();
        this.connectionListener=connectionListener;
        this.engine=new OkHttpWebSocketEngine(connectionListener);
    }

    public static WebSocketDataClient newInstance(OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener connectionListener){
        return new WebSocketDataClient(connectionListener);
    }

    @Override
    public IDataModelProducer<String> dataModelProducer(Context context, String tagAndClientKey, boolean internalLog) {
        IDataModelRequest request=requestCreator.tag(tagAndClientKey).create();
        LogDataModelInterceptor logInterceptor = LogDataModelInterceptor.newInstance();
        IDataModelProducer<String> dataModelProducer = DataModelProducerImpl.<String>newInstance();
        if (internalLog){
            dataModelProducer.addInputInterceptor(logInterceptor).addOutputInterceptor(logInterceptor);
        }
        dataModelProducer
                .request(request)
                .engine(engine);
        return dataModelProducer;
    }

    @Override
    public boolean sendMsg(String msg) {
        if (engine!=null){
            return engine.sendMsg(msg);
        }
        return false;
    }

    @Override
    public void close() {
        if (engine!=null){
            engine.close();
        }
    }
}
