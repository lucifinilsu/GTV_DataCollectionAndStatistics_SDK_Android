package cn.gtv.sdk.dcas.api;

import android.content.Context;
import android.os.Build;

import com.sad.basic.utils.app.AppInfoUtil;
import com.sad.jetpack.v1.datamodel.api.GlobalDataModelConfig;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.DefaultCacheLoader;

import org.json.JSONObject;

public final class DCASCore {

    protected static Context mContext=null;
    protected static String mServerAuthKeyV3 ="FpTx5RtXUftP4l-kTGtHKCCVS8vX86_tEkf7jgS1Ml8";
    public static void init(Context context,String serverAuthKeyV3,boolean privacyAgree){
        if (context==null && mContext==null){
            mContext=context;
            mServerAuthKeyV3=serverAuthKeyV3;
            DefaultCacheLoader.initCacheLoader(mContext);
            GlobalDataModelConfig.getInstance().enableLogUtils(true);
            if (privacyAgree){
                String currAppProcessName=AppInfoUtil.getCurrAppProccessName(mContext);
                if (context.getPackageName().equals(currAppProcessName)){
                    //主进程下处理
                }
            }

        }
    }


}
