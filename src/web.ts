import { WebPlugin } from '@capacitor/core';

import type {
  PhoneCallNotificationPlugin,
  NotificationPermissionStatus,
  FullScreenIntentPermissionStatus,
  GlobalNotificationSettings,
} from './definitions';

export class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
  async setGlobalNotificationSettings(_settings: GlobalNotificationSettings): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

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

  async hideAll(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async checkNotificationsPermission(): Promise<NotificationPermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }
  async requestNotificationsPermission(): Promise<NotificationPermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async checkFullScreenIntentPermission(): Promise<FullScreenIntentPermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async openFullScreenIntentSettings(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
