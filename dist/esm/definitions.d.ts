import type { PermissionState, PluginListenerHandle } from '@capacitor/core';
export declare type NotificationType = 'incoming' | 'inProgress' | 'missed';
export declare type NotificationResponse = 'tap' | 'answer' | 'decline' | 'terminate' | 'hold';
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
    hide(data: {
        type: NotificationType;
    }): Promise<void>;
    checkPermissions(): Promise<PermissionStatus>;
    requestPermissions(): Promise<PermissionStatus>;
    addListener(eventName: 'response', listenerFunc: (data: {
        response: NotificationResponse;
    }) => void): Promise<PluginListenerHandle>;
    removeAllListeners(): Promise<void>;
}
