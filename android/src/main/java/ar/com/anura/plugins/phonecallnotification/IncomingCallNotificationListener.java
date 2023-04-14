package ar.com.anura.plugins.phonecallnotification;

public interface IncomingCallNotificationListener {
    void onTap();
    void onDecline();
    void onAnswer();
    void onTerminate();
}
