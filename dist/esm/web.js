import { WebPlugin } from '@capacitor/core';
export class PhoneCallNotificationWeb extends WebPlugin {
    async showIncomingPhoneCallNotification() {
        throw this.unimplemented('Not implemented on web.');
    }
    async showCallInProgressNotification() {
        throw this.unimplemented('Not implemented on web.');
    }
    async hideIncomingPhoneCallNotification() {
        throw this.unimplemented('Not implemented on web.');
    }
    async hideCallInProgressNotification() {
        throw this.unimplemented('Not implemented on web.');
    }
    async checkNotificationsPermission() {
        throw this.unimplemented('Not implemented on web.');
    }
    async requestNotificationsPermission() {
        throw this.unimplemented('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map