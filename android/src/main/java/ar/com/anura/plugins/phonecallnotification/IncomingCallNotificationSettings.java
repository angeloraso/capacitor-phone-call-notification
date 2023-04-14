package ar.com.anura.plugins.phonecallnotifications;

public class NotificationSettings {

    private String type = "incoming";
    private String callerName = "Unknown";
    private String callerNumber = "Unknown";
    private String icon = "answer_24";
    private String picture = "picture";
    private Boolean thereIsACallInProgress = false;
    private String declineButtonText = "Decline";
    private String declineButtonColor = "#e76565";
    private String answerButtonText = "Answer";
    private String answerButtonColor = "#65bf6c";
    private String terminateAndAnswerButtonText = "Terminate & answer";
    private String terminateAndAnswerButtonColor = "#fac281";
    private String color = "#55335A";
    private String declineSecondCallButtonText = "Decline";
    private String declineSecondCallButtonColor = "#e76565";
    private String holdAndAnswerButtonText = "Hold & answer";
    private String holdAndAnswerButtonColor = "#65bf6c";
    private String channelName = "incoming-call-notification";
    private String channelDescription = "Incoming call notification";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCallerName() {
        return callerName;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public String getCallerNumber() {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber) {
        this.callerNumber = callerNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getThereIsACallInProgress() {
        return thereIsACallInProgress;
    }

    public void setThereIsACallInProgress(Boolean thereIsACallInProgress) {
        this.thereIsACallInProgress = thereIsACallInProgress;
    }

    public String getDeclineButtonText() {
        return declineButtonText;
    }

    public void setDeclineButtonText(String declineButtonText) {
        this.declineButtonText = declineButtonText;
    }

    public String getDeclineButtonColor() {
        return declineButtonColor;
    }

    public void setDeclineButtonColor(String declineButtonColor) {
        this.declineButtonColor = declineButtonColor;
    }

    public String getAnswerButtonText() {
        return answerButtonText;
    }

    public void setAnswerButtonText(String answerButtonText) {
        this.answerButtonText = answerButtonText;
    }

    public String getAnswerButtonColor() {
        return answerButtonColor;
    }

    public void setAnswerButtonColor(String answerButtonColor) {
        this.answerButtonColor = answerButtonColor;
    }

    public String getTerminateAndAnswerButtonText() {
        return terminateAndAnswerButtonText;
    }

    public void setTerminateAndAnswerButtonText(String terminateAndAnswerButtonText) {
        this.terminateAndAnswerButtonText = terminateAndAnswerButtonText;
    }

    public String getTerminateAndAnswerButtonColor() {
        return terminateAndAnswerButtonColor;
    }

    public void setTerminateAndAnswerButtonColor(String terminateAndAnswerButtonColor) {
        this.terminateAndAnswerButtonColor = terminateAndAnswerButtonColor;
    }

    public String getDeclineSecondCallButtonText() {
        return declineSecondCallButtonText;
    }

    public void setDeclineSecondCallButtonText(String declineSecondCallButtonText) {
        this.declineSecondCallButtonText = declineSecondCallButtonText;
    }

    public String getDeclineSecondCallButtonColor() {
        return declineSecondCallButtonColor;
    }

    public void setDeclineSecondCallButtonColor(String declineSecondCallButtonColor) {
        this.declineSecondCallButtonColor = declineSecondCallButtonColor;
    }

    public String getHoldAndAnswerButtonText() {
        return holdAndAnswerButtonText;
    }

    public void setHoldAndAnswerButtonText(String holdAndAnswerButtonText) {
        this.holdAndAnswerButtonText = holdAndAnswerButtonText;
    }

    public String getHoldAndAnswerButtonColor() {
        return holdAndAnswerButtonColor;
    }

    public void setHoldAndAnswerButtonColor(String holdAndAnswerButtonColor) {
        this.holdAndAnswerButtonColor = holdAndAnswerButtonColor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public void setChannelDescription(String channelDescription) {
        this.channelDescription = channelDescription;
    }
}
