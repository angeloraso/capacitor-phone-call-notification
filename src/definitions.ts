import type { PermissionState, PluginListenerHandle } from '@capacitor/core';

export type NotificationType = 'incoming' | 'inProgress' | 'missed';
export type NotificationResponse = 'tap' | 'answer' | 'decline' | 'terminate' | 'hold';

export interface PermissionStatus {
  display: PermissionState;
}
export interface NotificationSettings {
  type: NotificationType;
  duration: number;
  callerName: string;
  callerNumber: string;
  icon: string;
  picture: string;
  thereIsACallInProgress: boolean;
  declineButtonText: string;
  declineButtonColor: string;
  answerButtonText: string;
  answerButtonColor: string;
  terminateButtonText: string;
  terminateButtonColor: string;
  holdButtonText: string;
  holdButtonColor: string;
  terminateAndAnswerButtonText: string;
  terminateAndAnswerButtonColor: string;
  declineSecondCallButtonText: string;
  declineSecondCallButtonColor: string;
  holdAndAnswerButtonText: string;
  holdAndAnswerButtonColor: string;
  color: string;
  channelName: string;
  channelDescription: string;
}
export interface PhoneCallNotificationPlugin {
  show(data?: Partial<NotificationSettings>): Promise<void>;
  hide(data: { type: NotificationType }): Promise<void>;
  checkPermissions(): Promise<PermissionStatus>;
  getPushNotificationResponse(): Promise<{response: string}>;
  requestPermissions(): Promise<PermissionStatus>;
  registerPushNotifications(data?: Partial<NotificationSettings>): Promise<void>;
  unregisterPushNotifications(): Promise<void>;
  addListener(
    eventName: 'response',
    listenerFunc: (data: { response: NotificationResponse }) => void,
  ): Promise<PluginListenerHandle>;
  addListener(
    eventName: 'pushNotificationToken',
    listenerFunc: (data: { value: string }) => void,
  ): Promise<PluginListenerHandle>;
  addListener(
    eventName: 'pushNotificationData',
    listenerFunc: (data: { data: Record<string, string> }) => void,
  ): Promise<PluginListenerHandle>;
  removeAllListeners(): Promise<void>;
}
