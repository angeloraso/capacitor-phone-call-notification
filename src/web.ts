import { WebPlugin } from '@capacitor/core';

import type { PhoneCallNotificationPlugin, PermissionStatus } from './definitions';

export class PhoneCallNotificationWeb extends WebPlugin implements PhoneCallNotificationPlugin {
  async show(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
  async hide(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async requestPermissions(): Promise<PermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }

  async checkPermissions(): Promise<PermissionStatus> {
    throw this.unimplemented('Not implemented on web.');
  }
}
