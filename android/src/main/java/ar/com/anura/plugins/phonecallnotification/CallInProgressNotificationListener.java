package ar.com.anura.plugins.phonecallnotification;

public interface CallInProgressNotificationListener {
    void onTap();
    void onTerminate();
    void onHold();
}
