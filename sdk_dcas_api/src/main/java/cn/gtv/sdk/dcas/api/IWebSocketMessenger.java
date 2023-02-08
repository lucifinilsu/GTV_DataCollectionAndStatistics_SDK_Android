package cn.gtv.sdk.dcas.api;

public interface IWebSocketMessenger {

    boolean sendMsg(String msg);

    void close();
}
