import type { PermissionState, PluginListenerHandle } from '@capacitor/core';

export type NotificationType = 'incoming' | 'inProgress' | 'missed';
export type NotificationResponse = 'tap' | 'answer' | 'decline' | 'terminate' | 'hold';

export interface NotificationPermissionStatus {
  notifications: PermissionState;
}
export interface IncomingPhoneCallNotificationSettings {
    icon: string;
    picture: string;
    callWaiting: boolean;
    declineButtonText: string;
    declineButtonColor: string;
    answerButtonText: string;
    answerButtonColor: string;
    terminateAndAnswerButtonText: string;
    terminateAndAnswerButtonColor: string;
    terminateButtonText: string;
    terminateButtonColor: string;
    declineCallWaitingButtonText: string;
    declineCallWaitingButtonColor: string;
    holdButtonText: string;
    holdButtonColor: string;
    holdAndAnswerButtonText: string;
    holdAndAnswerButtonColor: string;
    color: string;
    duration: number;
    channelName: string;
    channelDescription: string;
    callingName: string;
    callingNumber: string;
}

export interface CallInProgressNotificationSettings {
  icon: string;
  picture: string;
  terminateButtonText: string;
  terminateButtonColor: string;
  holdButtonText: string;
  holdButtonColor: string;
  color: string;
  duration: number;
  channelName: string;
  channelDescription: string;
  callingName: string;
  callingNumber: string;
}
export interface PhoneCallNotificationPlugin {
  showIncomingPhoneCallNotification(data?: Partial<IncomingPhoneCallNotificationSettings>): Promise<void>;
  showCallInProgressNotification(data?: Partial<CallInProgressNotificationSettings>): Promise<void>;
  hideIncomingPhoneCallNotification(): Promise<void>;
  hideCallInProgressNotification(): Promise<void>;
  checkNotificationsPermission(): Promise<NotificationPermissionStatus>;
  requestNotificationsPermission(): Promise<NotificationPermissionStatus>;
  addListener(
    eventName: 'response',
    listenerFunc: (data: { response: NotificationResponse }) => void,
  ): Promise<PluginListenerHandle>;
  removeAllListeners(): Promise<void>;
}
