import { registerPlugin } from '@capacitor/core';

import type { PhoneCallNotificationPlugin } from './definitions';

const PhoneCallNotification = registerPlugin<PhoneCallNotificationPlugin>(
  'PhoneCallNotification',
  {
    web: () => import('./web').then(m => new m.PhoneCallNotificationWeb()),
  },
);

export * from './definitions';
export { PhoneCallNotification };
