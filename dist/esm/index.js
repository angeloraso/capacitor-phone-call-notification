import { registerPlugin } from '@capacitor/core';
const PhoneCallNotification = registerPlugin('PhoneCallNotification', {
    web: () => import('./web').then(m => new m.PhoneCallNotificationWeb()),
});
export * from './definitions';
export { PhoneCallNotification };
//# sourceMappingURL=index.js.map