package ar.com.anura.plugins.phonecallnotification;

import java.io.Serializable;

public class IncomingPhoneCallNotificationSettings implements Serializable {
  public static final String DEFAULT_ICON = "answer";
  public static final String DEFAULT_PICTURE = "picture";
  public static final Boolean DEFAULT_CALL_WAITING = false;
  public static final String DEFAULT_DECLINE_BUTTON_TEXT = "Decline";
  public static final String DEFAULT_DECLINE_BUTTON_COLOR = "#e76565";
  public static final String DEFAULT_ANSWER_BUTTON_TEXT = "Answer";
  public static final String DEFAULT_ANSWER_BUTTON_COLOR = "#65bf6c";
  public static final String DEFAULT_TERMINATE_AND_ANSWER_BUTTON_TEXT = "Terminate & answer";
  public static final String DEFAULT_TERMINATE_AND_ANSWER_BUTTON_COLOR = "#fac281";
  public static final String DEFAULT_TERMINATE_BUTTON_TEXT = "Terminate";
  public static final String DEFAULT_TERMINATE_BUTTON_COLOR = "#e76565";
  public static final String DEFAULT_DECLINE_CALL_WAITING_BUTTON_TEXT = "Decline";
  public static final String DEFAULT_DECLINE_CALL_WAITING_BUTTON_COLOR = "#e76565";
  public static final String DEFAULT_HOLD_BUTTON_TEXT = "Hold";
  public static final String DEFAULT_HOLD_BUTTON_COLOR = "#2b94f4";
  public static final String DEFAULT_HOLD_AND_ANSWER_BUTTON_TEXT = "Hold & answer";
  public static final String DEFAULT_HOLD_AND_ANSWER_BUTTON_COLOR = "#65bf6c";
  public static final String DEFAULT_COLOR = "#55335A";
  public static final int DEFAULT_DURATION = 0;
  public static final String DEFAULT_CHANNEL_NAME = "phone-call-notification";
  public static final String DEFAULT_CHANNEL_DESCRIPTION = "Phone call notifications";
  public static final String DEFAULT_CALLING_NAME = "Unknown";
  public static final String DEFAULT_CALLING_NUMBER = "Unknown";

  private final String icon;
  private final String picture;
  private final Boolean callWaiting;
  private final String declineButtonText;
  private final String declineButtonColor;
  private final String answerButtonText;
  private final String answerButtonColor;
  private final String terminateAndAnswerButtonText;
  private final String terminateAndAnswerButtonColor;
  private final String terminateButtonText;
  private final String terminateButtonColor;
  private final String declineCallWaitingButtonText;
  private final String declineCallWaitingButtonColor;
  private final String holdButtonText;
  private final String holdButtonColor;
  private final String holdAndAnswerButtonText;
  private final String holdAndAnswerButtonColor;
  private final String color;
  private final int duration;
  private final String channelName;
  private final String channelDescription;
  private final String callingName;
  private final String callingNumber;

  private IncomingPhoneCallNotificationSettings(Builder builder) {
    this.icon = builder.icon != null ? builder.icon : DEFAULT_ICON;
    this.picture = builder.picture != null ? builder.picture : DEFAULT_PICTURE;
    this.callWaiting = builder.callWaiting != null ? builder.callWaiting : DEFAULT_CALL_WAITING;
    this.declineButtonText = builder.declineButtonText != null ? builder.declineButtonText : DEFAULT_DECLINE_BUTTON_TEXT;
    this.declineButtonColor = builder.declineButtonColor != null ? builder.declineButtonColor : DEFAULT_DECLINE_BUTTON_COLOR;
    this.answerButtonText = builder.answerButtonText != null ? builder.answerButtonText : DEFAULT_ANSWER_BUTTON_TEXT;
    this.answerButtonColor = builder.answerButtonColor != null ? builder.answerButtonColor : DEFAULT_ANSWER_BUTTON_COLOR;
    this.terminateAndAnswerButtonText = builder.terminateAndAnswerButtonText != null ? builder.terminateAndAnswerButtonText : DEFAULT_TERMINATE_AND_ANSWER_BUTTON_TEXT;
    this.terminateAndAnswerButtonColor = builder.terminateAndAnswerButtonColor != null ? builder.terminateAndAnswerButtonColor : DEFAULT_TERMINATE_AND_ANSWER_BUTTON_COLOR;
    this.terminateButtonText = builder.terminateButtonText != null ? builder.terminateButtonText : DEFAULT_TERMINATE_BUTTON_TEXT;
    this.terminateButtonColor = builder.terminateButtonColor != null ? builder.terminateButtonColor : DEFAULT_TERMINATE_BUTTON_COLOR;
    this.declineCallWaitingButtonText = builder.declineCallWaitingButtonText != null ? builder.declineCallWaitingButtonText : DEFAULT_DECLINE_CALL_WAITING_BUTTON_TEXT;
    this.declineCallWaitingButtonColor = builder.declineCallWaitingButtonColor != null ? builder.declineCallWaitingButtonColor : DEFAULT_DECLINE_CALL_WAITING_BUTTON_COLOR;
    this.holdButtonText = builder.holdButtonText != null ? builder.holdButtonText : DEFAULT_HOLD_BUTTON_TEXT;
    this.holdButtonColor = builder.holdButtonColor != null ? builder.holdButtonColor : DEFAULT_HOLD_BUTTON_COLOR;
    this.holdAndAnswerButtonText = builder.holdAndAnswerButtonText != null ? builder.holdAndAnswerButtonText : DEFAULT_HOLD_AND_ANSWER_BUTTON_TEXT;
    this.holdAndAnswerButtonColor = builder.holdAndAnswerButtonColor != null ? builder.holdAndAnswerButtonColor : DEFAULT_HOLD_AND_ANSWER_BUTTON_COLOR;
    this.color = builder.color != null ? builder.color : DEFAULT_COLOR;
    this.duration = builder.duration != null ? builder.duration : DEFAULT_DURATION;
    this.channelName = builder.channelName != null ? builder.channelName : DEFAULT_CHANNEL_NAME;
    this.channelDescription = builder.channelDescription != null ? builder.channelDescription : DEFAULT_CHANNEL_DESCRIPTION;
    this.callingName = builder.callingName != null ? builder.callingName : DEFAULT_CALLING_NAME;
    this.callingNumber = builder.callingNumber != null ? builder.callingNumber : DEFAULT_CALLING_NUMBER;
  }

  public static class Builder {
    private String icon;
    private String picture;
    private Boolean callWaiting;
    private String declineButtonText;
    private String declineButtonColor;
    private String answerButtonText;
    private String answerButtonColor;
    private String terminateAndAnswerButtonText;
    private String terminateAndAnswerButtonColor;
    private String terminateButtonText;
    private String terminateButtonColor;
    private String declineCallWaitingButtonText;
    private String declineCallWaitingButtonColor;
    private String holdButtonText;
    private String holdButtonColor;
    private String holdAndAnswerButtonText;
    private String holdAndAnswerButtonColor;
    private String color;
    private Integer duration;
    private String channelName;
    private String channelDescription;
    private String callingName;
    private String callingNumber;

    public Builder icon(String icon) { this.icon = icon; return this; }
    public Builder picture(String picture) { this.picture = picture; return this; }
    public Builder callWaiting(Boolean callWaiting) { this.callWaiting = callWaiting; return this; }
    public Builder declineButtonText(String text) { this.declineButtonText = text; return this; }
    public Builder declineButtonColor(String color) { this.declineButtonColor = color; return this; }
    public Builder answerButtonText(String text) { this.answerButtonText = text; return this; }
    public Builder answerButtonColor(String color) { this.answerButtonColor = color; return this; }
    public Builder terminateAndAnswerButtonText(String text) { this.terminateAndAnswerButtonText = text; return this; }
    public Builder terminateAndAnswerButtonColor(String color) { this.terminateAndAnswerButtonColor = color; return this; }
    public Builder terminateButtonText(String text) { this.terminateButtonText = text; return this; }
    public Builder terminateButtonColor(String color) { this.terminateButtonColor = color; return this; }
    public Builder declineCallWaitingButtonText(String text) { this.declineCallWaitingButtonText = text; return this; }
    public Builder declineCallWaitingButtonColor(String color) { this.declineCallWaitingButtonColor = color; return this; }
    public Builder holdButtonText(String text) { this.holdButtonText = text; return this; }
    public Builder holdButtonColor(String color) { this.holdButtonColor = color; return this; }
    public Builder holdAndAnswerButtonText(String text) { this.holdAndAnswerButtonText = text; return this; }
    public Builder holdAndAnswerButtonColor(String color) { this.holdAndAnswerButtonColor = color; return this; }
    public Builder color(String color) { this.color = color; return this; }
    public Builder duration(Integer duration) { this.duration = duration; return this; }
    public Builder channelName(String name) { this.channelName = name; return this; }
    public Builder channelDescription(String description) { this.channelDescription = description; return this; }
    public Builder callingName(String name) { this.callingName = name; return this; }
    public Builder callingNumber(String number) { this.callingNumber = number; return this; }

    public IncomingPhoneCallNotificationSettings build() {
      return new IncomingPhoneCallNotificationSettings(this);
    }
  }

  public String getIcon() {
    return icon;
  }

  public String getPicture() {
    return picture;
  }

  public Boolean getCallWaiting() {
    return callWaiting;
  }

  public String getDeclineButtonText() {
    return declineButtonText;
  }

  public String getDeclineButtonColor() {
    return declineButtonColor;
  }

  public String getAnswerButtonText() {
    return answerButtonText;
  }

  public String getAnswerButtonColor() {
    return answerButtonColor;
  }

  public String getTerminateAndAnswerButtonText() {
    return terminateAndAnswerButtonText;
  }

  public String getTerminateAndAnswerButtonColor() {
    return terminateAndAnswerButtonColor;
  }

  public String getTerminateButtonText() {
    return terminateButtonText;
  }

  public String getTerminateButtonColor() {
    return terminateButtonColor;
  }

  public String getDeclineCallWaitingButtonText() {
    return declineCallWaitingButtonText;
  }

  public String getDeclineCallWaitingButtonColor() {
    return declineCallWaitingButtonColor;
  }

  public String getHoldButtonText() {
    return holdButtonText;
  }

  public String getHoldButtonColor() {
    return holdButtonColor;
  }

  public String getHoldAndAnswerButtonText() {
    return holdAndAnswerButtonText;
  }

  public String getHoldAndAnswerButtonColor() {
    return holdAndAnswerButtonColor;
  }

  public String getColor() {
    return color;
  }

  public int getDuration() {
    return duration;
  }

  public String getChannelName() {
    return channelName;
  }

  public String getChannelDescription() {
    return channelDescription;
  }

  public String getCallingName() {
    return callingName;
  }

  public String getCallingNumber() {
    return callingNumber;
  }
}

