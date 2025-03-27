'use strict';

var core = require('@capacitor/core');

const PhoneCallNotification = core.registerPlugin('PhoneCallNotification', {
    web: () => Promise.resolve().then(function () { return web; }).then(m => new m.PhoneCallNotificationWeb()),
});

class PhoneCallNotificationWeb extends core.WebPlugin {
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

var web = /*#__PURE__*/Object.freeze({
    __proto__: null,
    PhoneCallNotificationWeb: PhoneCallNotificationWeb
});

exports.PhoneCallNotification = PhoneCallNotification;
//# sourceMappingURL=plugin.cjs.js.map
