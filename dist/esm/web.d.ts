import { WebPlugin } from '@capacitor/core';
import type { PhoneCallNotificationPlugin, NotificationPermissionStatus, FullScreenIntentPermissionStatus, GlobalNotificationSettings } from './definitions';
export declare class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
    setGlobalNotificationSettings(_settings: GlobalNotificationSettings): Promise<void>;
    showIncomingPhoneCallNotification(): Promise<void>;
    showCallInProgressNotification(): Promise<void>;
    hideIncomingPhoneCallNotification(): Promise<void>;
    hideCallInProgressNotification(): Promise<void>;
    hideAll(): Promise<void>;
    checkNotificationsPermission(): Promise<NotificationPermissionStatus>;
    requestNotificationsPermission(): Promise<NotificationPermissionStatus>;
    checkFullScreenIntentPermission(): Promise<FullScreenIntentPermissionStatus>;
    openFullScreenIntentSettings(): Promise<void>;
}
