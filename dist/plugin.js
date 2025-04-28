var capacitorPhoneCallNotification = (function (exports, core) {
    'use strict';

    const PhoneCallNotification = core.registerPlugin('PhoneCallNotification', {
        web: () => Promise.resolve().then(function () { return web; }).then(m => new m.PhoneCallNotificationWeb()),
    });

    class PhoneCallNotificationWeb extends core.WebPlugin {
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

    var web = /*#__PURE__*/Object.freeze({
        __proto__: null,
        PhoneCallNotificationWeb: PhoneCallNotificationWeb
    });

    exports.PhoneCallNotification = PhoneCallNotification;

    return exports;

})({}, capacitorExports);
//# sourceMappingURL=plugin.js.map
