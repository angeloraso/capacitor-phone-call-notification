package ar.com.anura.plugins.phonecallnotification;

import java.io.Serializable;

public class CallInProgressNotificationSettings implements Serializable {
  public static final String DEFAULT_ICON = "answer";
  public static final String DEFAULT_PICTURE = "picture";
  public static final String DEFAULT_TERMINATE_BUTTON_TEXT = "Terminate";
  public static final String DEFAULT_TERMINATE_BUTTON_COLOR = "#e76565";
  public static final String DEFAULT_HOLD_BUTTON_TEXT = "Hold";
  public static final String DEFAULT_HOLD_BUTTON_COLOR = "#2b94f4";
  public static final String DEFAULT_COLOR = "#666666";
  public static final int DEFAULT_DURATION = 0;
  public static final String DEFAULT_CHANNEL_NAME = "phone-call-notification";
  public static final String DEFAULT_CHANNEL_DESCRIPTION = "Phone call notifications";
  public static final String DEFAULT_CALLING_NAME = "Unknown";
  public static final String DEFAULT_CALLING_NUMBER = "Unknown";

  private final String icon;
  private final String picture;
  private final String terminateButtonText;
  private final String terminateButtonColor;
  private final String holdButtonText;
  private final String holdButtonColor;
  private final String color;
  private final int duration;
  private final String channelName;
  private final String channelDescription;
  private final String callingName;
  private final String callingNumber;

  private CallInProgressNotificationSettings(Builder builder) {
    this.icon = builder.icon != null ? builder.icon : DEFAULT_ICON;
    this.picture = builder.picture != null ? builder.picture : DEFAULT_PICTURE;
    this.terminateButtonText = builder.terminateButtonText != null ? builder.terminateButtonText : DEFAULT_TERMINATE_BUTTON_TEXT;
    this.terminateButtonColor = builder.terminateButtonColor != null ? builder.terminateButtonColor : DEFAULT_TERMINATE_BUTTON_COLOR;
    this.holdButtonText = builder.holdButtonText != null ? builder.holdButtonText : DEFAULT_HOLD_BUTTON_TEXT;
    this.holdButtonColor = builder.holdButtonColor != null ? builder.holdButtonColor : DEFAULT_HOLD_BUTTON_COLOR;
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
    private String terminateButtonText;
    private String terminateButtonColor;
    private String holdButtonText;
    private String holdButtonColor;
    private String color;
    private Integer duration;
    private String channelName;
    private String channelDescription;
    private String callingName;
    private String callingNumber;

    public Builder icon(String icon) { this.icon = icon; return this; }
    public Builder picture(String picture) { this.picture = picture; return this; }
    public Builder terminateButtonText(String terminateButtonText) { this.terminateButtonText = terminateButtonText; return this; }
    public Builder terminateButtonColor(String terminateButtonColor) { this.terminateButtonColor = terminateButtonColor; return this; }
    public Builder holdButtonText(String holdButtonText) { this.holdButtonText = holdButtonText; return this; }
    public Builder holdButtonColor(String holdButtonColor) { this.holdButtonColor = holdButtonColor; return this; }
    public Builder color(String color) { this.color = color; return this; }
    public Builder duration(Integer duration) { this.duration = duration; return this; }
    public Builder channelName(String channelName) { this.channelName = channelName; return this; }
    public Builder channelDescription(String channelDescription) { this.channelDescription = channelDescription; return this; }
    public Builder callingName(String callingName) { this.callingName = callingName; return this; }
    public Builder callingNumber(String callingNumber) { this.callingNumber = callingNumber; return this; }

    public CallInProgressNotificationSettings build() {
      return new CallInProgressNotificationSettings(this);
    }
  }

  public String getIcon() {
    return icon;
  }

  public String getPicture() {
    return picture;
  }

  public String getTerminateButtonText() {
    return terminateButtonText;
  }

  public String getTerminateButtonColor() {
    return terminateButtonColor;
  }

  public String getHoldButtonText() {
    return holdButtonText;
  }

  public String getHoldButtonColor() {
    return holdButtonColor;
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
