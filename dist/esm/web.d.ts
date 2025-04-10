import { WebPlugin } from '@capacitor/core';
import type { PhoneCallNotificationPlugin, PermissionStatus } from './definitions';
export declare class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
    showIncomingPhoneCallNotification(): Promise<void>;
    showCallInProgressNotification(): Promise<void>;
    hideIncomingPhoneCallNotification(): Promise<void>;
    hideCallInProgressNotification(): Promise<void>;
    requestPermissions(): Promise<PermissionStatus>;
    checkPermissions(): Promise<PermissionStatus>;
}
