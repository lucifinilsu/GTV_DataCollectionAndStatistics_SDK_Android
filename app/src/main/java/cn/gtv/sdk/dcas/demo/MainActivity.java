package cn.gtv.sdk.dcas.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.util.Log;

import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.ISocketMessenger;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.OkHttpWebSocketEngine;
import com.sad.jetpack.v1.datamodel.api.extension.client.socket.WebSocketDataClient;

import org.json.JSONObject;

import java.util.List;

import cn.gtv.sdk.dcas.api.DCASCore;
import cn.gtv.sdk.dcas.api.DCASTrackerImpl;
import cn.gtv.sdk.dcas.api.EventsManager;
import cn.gtv.sdk.dcas.api.HttpDJDataClient;
import cn.gtv.sdk.dcas.api.IServerTokenConsumer;
import cn.gtv.sdk.dcas.api.IServerTokenFactory;
import cn.gtv.sdk.dcas.api.TrackerDataCreator;
import cn.gtv.sdk.dcas.api.db.Event;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DCASCore.init(getApplicationContext(), "FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8", "f@aix+xk7du0*dh$98-w", true)
                .product("DJGZ")
                .serverTokenFactory(new IServerTokenFactory() {
                    @Override
                    public void onCreateServerToken(IServerTokenConsumer consumer) {
                        String serverToken = "ac12ac23ca13ca31ac31ac31ac52ac32ac12ac23ca13ca31ac31ac31ac52ac32";
                        consumer.optServerToken(serverToken);
                    }
                })
        ;
        //LogcatUtils.e("本机局域网IP:"+IPCServer.getLocalIP());
        //testServerSocket();
        testDCAS();

        testDb();
    }

    private void testDCAS() {
        DCASTrackerImpl.newBuilder(this.getApplicationContext())
                .event("hit", "article", "xxcscascascacac")
                .detail(new JSONObject())
                .build()
                .post();
    }

    private void testHttp() {
        HttpDJDataClient.newInstance(getApplicationContext(), "")
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

    private void testWebSocket() {
        WebSocketDataClient.newInstance(new OkHttpWebSocketEngine.IOkhttpWebSocketConnectionListener() {
                    @Override
                    public void onWebSocketConnectionAlive(String tag, ISocketMessenger messenger, int socketAliveMode) {
                        if (socketAliveMode == OkHttpWebSocketEngine.SOCKET_ALIVE_MODE_INIT) {
                            messenger.sendMsg("第一次连接到服务端,我先发一条信息给服务端");
                        }
                    }

                    @Override
                    public void onWebSocketMessageReceived(String tag, ISocketMessenger messenger, String text, int socketAliveMode) {

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

    public void testServerSocket() {
        //IPCServer.startServer(this);
    }

    public void testDb() {
        produceEvent();

        EventsManager.getInstance(this).getAllLiveData().observeForever(new Observer<List<Event>>() {
            @Override
            public void onChanged(List<Event> events) {
                Log.e("db_test", events.size() + "");
                if (events.size() >= 50) {
                    consumeEvent(events);
                    //立即删除数据已经消费完的数据，防止重复消费
                    EventsManager.getInstance(getApplicationContext()).deleteOldest(events.get(events.size() - 1).getUpdateTime());
                }
            }
        });
    }

    int d = 0;
    public void produceEvent(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) (Math.random() * 100+200));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                EventsManager.getInstance(MainActivity.this).insert("第" + d++ + "条数据");
                produceEvent();
            }
        }).start();
    }

    public void consumeEvent(List<Event> events) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep((long) (Math.random() * 2000 + 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}