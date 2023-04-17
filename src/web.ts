import { WebPlugin } from '@capacitor/core';

import type { PhoneCallNotificationPlugin } from './definitions';

export class PhoneCallNotificationWeb
  extends WebPlugin
  implements PhoneCallNotificationPlugin
{
  async show(): Promise<{ response: 'tap' | 'answer' | 'decline' | 'terminate' | 'hold' }> {
    throw this.unimplemented('Not implemented on web.');
  }  
  async hide(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }
}
