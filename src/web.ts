import { WebPlugin } from '@capacitor/core';

import type { PhoneCallNotificationPlugin } from './definitions';

export class PhoneCallNotificationWeb
  extends WebPlugin
  implements PhoneCallNotificationPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
