package cn.gtv.sdk.dcas.api;

public interface IWebSocketMessenger {

    boolean connect(boolean isRetry);

    boolean sendMsg(String msg);

    void close();
}
