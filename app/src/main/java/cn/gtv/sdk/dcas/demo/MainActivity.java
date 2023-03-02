package cn.gtv.sdk.dcas.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.ISocketMessenger;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.OkHttpWebSocketEngine;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.WebSocketDataClient;

import org.json.JSONObject;

import cn.gtv.sdk.dcas.api.DCASCore;
import cn.gtv.sdk.dcas.api.DCASTrackerImpl;
import cn.gtv.sdk.dcas.api.HttpDJDataClient;
import cn.gtv.sdk.dcas.api.TrackerDataCreator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DCASCore.init(getApplicationContext(),"FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8",true);
        //LogcatUtils.e("本机局域网IP:"+IPCServer.getLocalIP());
        testServerSocket();

    }

    private void testDCAS(){
        DCASTrackerImpl.newBuilder(this)
                .event("hit","article")
                .detail(new JSONObject())
                .build()
                .post("xxxx");
    }

    private void testHttp(){
        HttpDJDataClient.newInstance(getApplicationContext(),"")
                .url("")
                .log(true)
                .params(new JSONObject())
                .method(IDataModelRequest.Method.GET)
                .dataModelProducer("test123")
                .callback(new IDataModelObtainedCallback<String>() {
                    @Override
                    public void onDataObtainedCompleted(IDataModelResponse<String> response) {

                    }
                })
                .exceptionListener(new IDataModelObtainedExceptionListener() {
                    @Override
                    public void onDataObtainedException(IDataModelRequest request, Throwable throwable) {

                    }
                })
                .execute();
        ;
    }

    private void testWebSocket(){
        WebSocketDataClient.newInstance(new OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener() {
                    @Override
                    public void onWebSocketConnectionAlive(String tag, ISocketMessenger messenger, int socketAliveMode) {
                        if (socketAliveMode==OkHttpWebSocketEngine.SOCKET_ALIVE_MODE_INIT){
                            messenger.sendMsg("第一次连接到服务端,我先发一条信息给服务端");
                        }
                    }

                    @Override
                    public void onWebSocketConnectionShutDown(String tag, int code, String reason) {

                    }

                    @Override
                    public void onWebSocketConnectionClosed(String tag, int code, String reason) {

                    }
                })
                .url("")
                .log(true)
                .params(new JSONObject())
                .dataModelProducer("test320")
                .callback(new IDataModelObtainedCallback<String>() {
                    @Override
                    public void onDataObtainedCompleted(IDataModelResponse<String> response) {

                    }
                })
                .exceptionListener(new IDataModelObtainedExceptionListener() {
                    @Override
                    public void onDataObtainedException(IDataModelRequest request, Throwable throwable) {

                    }
                })
                .execute();
    }

    public void testServerSocket(){
        //IPCServer.startServer(this);
    }




}