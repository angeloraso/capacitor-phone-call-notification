import { WebPlugin } from '@capacitor/core';
import type { PhoneCallNotificationPlugin, PermissionStatus } from './definitions';
export declare class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
    show(): Promise<void>;
    hide(): Promise<void>;
    requestPermissions(): Promise<PermissionStatus>;
    checkPermissions(): Promise<PermissionStatus>;
}
