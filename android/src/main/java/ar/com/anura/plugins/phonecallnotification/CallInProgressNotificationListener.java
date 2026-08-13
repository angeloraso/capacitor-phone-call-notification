package ar.com.anura.plugins.phonecallnotification;

public interface CallInProgressNotificationListener {
    void onTap();
    void onTerminate();
    void onMute();
    void onHold();
    void onSwitchSession();
}
