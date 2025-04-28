import { WebPlugin } from '@capacitor/core';

import type { PhoneCallNotificationPlugin, NotificationPermissionStatus } from './definitions';

export class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
  async showIncomingPhoneCallNotification(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async showCallInProgressNotification(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideIncomingPhoneCallNotification(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async hideCallInProgressNotification(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async checkNotificationsPermission(): Promise<NotificationPermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }
  async requestNotificationsPermission(): Promise<NotificationPermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }
}
