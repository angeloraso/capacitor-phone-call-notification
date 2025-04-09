import { WebPlugin } from '@capacitor/core';
export class PhoneCallNotificationWeb extends WebPlugin {
    async show() {
        throw this.unimplemented('Not implemented on web.');
    }
    async hide() {
        throw this.unimplemented('Not implemented on web.');
    }
    async requestPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
    async checkPermissions() {
        throw this.unimplemented('Not implemented on web.');
    }
}
//# sourceMappingURL=web.js.map