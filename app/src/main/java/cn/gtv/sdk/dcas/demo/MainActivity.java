package cn.gtv.sdk.dcas.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.utils.LogcatUtils;

import org.json.JSONObject;

import cn.gtv.sdk.dcas.api.DCASCore;
import cn.gtv.sdk.dcas.api.HttpDataClient;
import cn.gtv.sdk.dcas.api.IWebSocketMessenger;
import cn.gtv.sdk.dcas.api.OkHttpWebSocketEngine;
import cn.gtv.sdk.dcas.api.WebSocketDataClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DCASCore.init(getApplicationContext(),"FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8",true);
        //LogcatUtils.e("本机局域网IP:"+IPCServer.getLocalIP());
        testServerSocket();

    }

    private void testHttp(){
        HttpDataClient.newInstance("")
                .url("")
                .params(new JSONObject())
                .method(IDataModelRequest.Method.GET)
                .dataModelProducer(getApplicationContext(),"test123",true)
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
                    public void onWebSocketConnectionAlive(String tag,IWebSocketMessenger messenger, int socketAliveMode) {
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
                .params(new JSONObject())
                .dataModelProducer(getApplicationContext(),"test320",true)
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