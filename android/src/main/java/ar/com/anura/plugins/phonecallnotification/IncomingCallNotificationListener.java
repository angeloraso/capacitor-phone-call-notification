package ar.com.anura.plugins.phonecallnotifications;

public interface IncomingCallNotificationListener {
    void onTap();
    void onDecline();
    void onAnswer();
    void onTerminate();
}
