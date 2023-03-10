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
    public static GlobalConfig init(Context context,String serverAuthKeyV3,String serverAuthKeyV2,boolean privacyAgree){
        if (globalConfig==null){
            globalConfig=new GlobalConfig();
        }
        if (context!=null && mContext==null){
            mContext=context;
        }
        globalConfig.serverAuthKeyV3(serverAuthKeyV3).serverAuthKeyV2(serverAuthKeyV2);
        if (mContext!=null){
            DefaultCacheLoader.initCacheLoader(mContext);
        }
        GlobalDataModelConfig.getInstance().enableLogUtils(true);
        if (privacyAgree){
            String currAppProcessName=AppInfoUtil.getCurrAppProccessName(mContext);
            if (context.getPackageName().equals(currAppProcessName)){
                //主进程下处理
            }
        }

        return globalConfig;
    }

    public static class GlobalConfig{

        private IServerTokenFactory serverTokenFactory;
        private String serverAuthKeyV3="FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8";
        private String serverAuthKeyV2="f@aix+xk7du0*dh$98-w";
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

        public GlobalConfig serverAuthKeyV2(String serverAuthKeyV2){
            this.serverAuthKeyV2=serverAuthKeyV2;
            return this;
        }

        public String getServerAuthKeyV2() {
            return serverAuthKeyV2;
        }
    }


}
