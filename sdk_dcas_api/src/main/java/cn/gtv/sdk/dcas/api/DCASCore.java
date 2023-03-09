package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.os.Build;

import com.sad.basic.utils.app.AppInfoUtil;
import com.sad.jetpack.v1.datamodel.api.GlobalDataModelConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultCacheLoader;

import org.json.JSONObject;

public final class DCASCore {

    protected static Context mContext=null;
    public static GlobalConfig globalConfig=new GlobalConfig();
    public static GlobalConfig init(Context context,String serverAuthKeyV3,boolean privacyAgree){
        globalConfig=new GlobalConfig();
        if (context==null && mContext==null){
            mContext=context;
            globalConfig.serverAuthKeyV3(serverAuthKeyV3);
            DefaultCacheLoader.initCacheLoader(mContext);
            GlobalDataModelConfig.getInstance().enableLogUtils(true);
            if (privacyAgree){
                String currAppProcessName=AppInfoUtil.getCurrAppProccessName(mContext);
                if (context.getPackageName().equals(currAppProcessName)){
                    //主进程下处理
                }
            }
        }

        return globalConfig;
    }

    public static class GlobalConfig{

        private IServerTokenFactory serverTokenFactory;
        private String serverAuthKeyV3="FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8";
        private String product="";
        private String client="APP";

        public GlobalConfig client(String client){
            this.client=client;
            return this;
        }
        public String getClient() {
            return client;
        }

        public GlobalConfig product(String product){
            this.product=product;
            return this;
        }
        public String getProduct() {
            return product;
        }

        public GlobalConfig serverTokenFactory(IServerTokenFactory serverTokenFactory){
            this.serverTokenFactory=serverTokenFactory;
            return this;
        }

        public GlobalConfig serverAuthKeyV3(String serverAuthKeyV3){
            this.serverAuthKeyV3=serverAuthKeyV3;
            return this;
        }

        public IServerTokenFactory getServerTokenFactory() {
            return serverTokenFactory;
        }

        public String getServerAuthKeyV3() {
            return serverAuthKeyV3;
        }
    }


}
