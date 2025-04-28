import { WebPlugin } from '@capacitor/core';
import type { PhoneCallNotificationPlugin, NotificationPermissionStatus } from './definitions';
export declare class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
    showIncomingPhoneCallNotification(): Promise<void>;
    showCallInProgressNotification(): Promise<void>;
    hideIncomingPhoneCallNotification(): Promise<void>;
    hideCallInProgressNotification(): Promise<void>;
    checkNotificationsPermission(): Promise<NotificationPermissionStatus>;
    requestNotificationsPermission(): Promise<NotificationPermissionStatus>;
}
