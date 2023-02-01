package cn.gtv.sdk.dcas.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;

import org.json.JSONObject;

import cn.gtv.sdk.dcas.api.DCASCore;
import cn.gtv.sdk.dcas.api.HttpDataClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DCASCore.init(getApplicationContext(),"FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8",true);
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
}