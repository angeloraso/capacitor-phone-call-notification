# @anuradev/capacitor-phone-call-notification

Capacitor plugin to create phone call notifications

For an in-progress call, pass the current number of sessions in `sessionCount`. The expanded notification only displays the switch-session button when that value is greater than one. Button presses are emitted through the `response` listener as `terminate`, `mute`, `hold`, or `switchSession`.

## Install

```bash
npm install @anuradev/capacitor-phone-call-notification
npx cap sync
```

## API

<docgen-index>

* [`setGlobalNotificationSettings(...)`](#setglobalnotificationsettings)
* [`showIncomingPhoneCallNotification(...)`](#showincomingphonecallnotification)
* [`showCallInProgressNotification(...)`](#showcallinprogressnotification)
* [`hideIncomingPhoneCallNotification()`](#hideincomingphonecallnotification)
* [`hideCallInProgressNotification()`](#hidecallinprogressnotification)
* [`hideAll()`](#hideall)
* [`checkNotificationsPermission()`](#checknotificationspermission)
* [`requestNotificationsPermission()`](#requestnotificationspermission)
* [`checkFullScreenIntentPermission()`](#checkfullscreenintentpermission)
* [`openFullScreenIntentSettings()`](#openfullscreenintentsettings)
* [`addListener('response', ...)`](#addlistenerresponse-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setGlobalNotificationSettings(...)

```typescript
setGlobalNotificationSettings(settings: GlobalNotificationSettings) => Promise<void>
```

| Param          | Type                                                                              |
| -------------- | --------------------------------------------------------------------------------- |
| **`settings`** | <code><a href="#globalnotificationsettings">GlobalNotificationSettings</a></code> |

--------------------


### showIncomingPhoneCallNotification(...)

```typescript
showIncomingPhoneCallNotification(data?: Partial<IncomingPhoneCallNotificationSettings> | undefined) => Promise<void>
```

| Param      | Type                                                                                                                                          |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **`data`** | <code><a href="#partial">Partial</a>&lt;<a href="#incomingphonecallnotificationsettings">IncomingPhoneCallNotificationSettings</a>&gt;</code> |

--------------------


### showCallInProgressNotification(...)

```typescript
showCallInProgressNotification(data?: Partial<CallInProgressNotificationSettings> | undefined) => Promise<void>
```

| Param      | Type                                                                                                                                    |
| ---------- | --------------------------------------------------------------------------------------------------------------------------------------- |
| **`data`** | <code><a href="#partial">Partial</a>&lt;<a href="#callinprogressnotificationsettings">CallInProgressNotificationSettings</a>&gt;</code> |

--------------------


### hideIncomingPhoneCallNotification()

```typescript
hideIncomingPhoneCallNotification() => Promise<void>
```

--------------------


### hideCallInProgressNotification()

```typescript
hideCallInProgressNotification() => Promise<void>
```

--------------------


### hideAll()

```typescript
hideAll() => Promise<void>
```

--------------------


### checkNotificationsPermission()

```typescript
checkNotificationsPermission() => Promise<NotificationPermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#notificationpermissionstatus">NotificationPermissionStatus</a>&gt;</code>

--------------------


### requestNotificationsPermission()

```typescript
requestNotificationsPermission() => Promise<NotificationPermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#notificationpermissionstatus">NotificationPermissionStatus</a>&gt;</code>

--------------------


### checkFullScreenIntentPermission()

```typescript
checkFullScreenIntentPermission() => Promise<FullScreenIntentPermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#fullscreenintentpermissionstatus">FullScreenIntentPermissionStatus</a>&gt;</code>

--------------------


### openFullScreenIntentSettings()

```typescript
openFullScreenIntentSettings() => Promise<void>
```

--------------------


### addListener('response', ...)

```typescript
addListener(eventName: 'response', listenerFunc: (data: { response: NotificationResponse; }) => void) => Promise<PluginListenerHandle>
```

| Param              | Type                                                                                                    |
| ------------------ | ------------------------------------------------------------------------------------------------------- |
| **`eventName`**    | <code>'response'</code>                                                                                 |
| **`listenerFunc`** | <code>(data: { response: <a href="#notificationresponse">NotificationResponse</a>; }) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### removeAllListeners()

```typescript
removeAllListeners() => Promise<void>
```

--------------------


### Interfaces


#### GlobalNotificationSettings

| Prop             | Type                                                                                                                                          |
| ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **`incoming`**   | <code><a href="#partial">Partial</a>&lt;<a href="#incomingphonecallnotificationsettings">IncomingPhoneCallNotificationSettings</a>&gt;</code> |
| **`inProgress`** | <code><a href="#partial">Partial</a>&lt;<a href="#callinprogressnotificationsettings">CallInProgressNotificationSettings</a>&gt;</code>       |


#### IncomingPhoneCallNotificationSettings

| Prop                                | Type                 |
| ----------------------------------- | -------------------- |
| **`icon`**                          | <code>string</code>  |
| **`picture`**                       | <code>string</code>  |
| **`callWaiting`**                   | <code>boolean</code> |
| **`declineButtonText`**             | <code>string</code>  |
| **`declineButtonColor`**            | <code>string</code>  |
| **`answerButtonText`**              | <code>string</code>  |
| **`answerButtonColor`**             | <code>string</code>  |
| **`terminateAndAnswerButtonText`**  | <code>string</code>  |
| **`terminateAndAnswerButtonColor`** | <code>string</code>  |
| **`terminateButtonText`**           | <code>string</code>  |
| **`terminateButtonColor`**          | <code>string</code>  |
| **`declineCallWaitingButtonText`**  | <code>string</code>  |
| **`declineCallWaitingButtonColor`** | <code>string</code>  |
| **`holdButtonText`**                | <code>string</code>  |
| **`holdButtonColor`**               | <code>string</code>  |
| **`holdAndAnswerButtonText`**       | <code>string</code>  |
| **`holdAndAnswerButtonColor`**      | <code>string</code>  |
| **`color`**                         | <code>string</code>  |
| **`duration`**                      | <code>number</code>  |
| **`channelName`**                   | <code>string</code>  |
| **`channelDescription`**            | <code>string</code>  |
| **`callingName`**                   | <code>string</code>  |
| **`callingNumber`**                 | <code>string</code>  |


#### CallInProgressNotificationSettings

| Prop                           | Type                |
| ------------------------------ | ------------------- |
| **`icon`**                     | <code>string</code> |
| **`picture`**                  | <code>string</code> |
| **`terminateButtonText`**      | <code>string</code> |
| **`terminateButtonColor`**     | <code>string</code> |
| **`holdButtonText`**           | <code>string</code> |
| **`holdButtonColor`**          | <code>string</code> |
| **`muteButtonText`**           | <code>string</code> |
| **`muteButtonColor`**          | <code>string</code> |
| **`switchSessionButtonText`**  | <code>string</code> |
| **`switchSessionButtonColor`** | <code>string</code> |
| **`sessionCount`**             | <code>number</code> |
| **`color`**                    | <code>string</code> |
| **`duration`**                 | <code>number</code> |
| **`channelName`**              | <code>string</code> |
| **`channelDescription`**       | <code>string</code> |
| **`callingName`**              | <code>string</code> |
| **`callingNumber`**            | <code>string</code> |


#### NotificationPermissionStatus

| Prop                | Type                                                        |
| ------------------- | ----------------------------------------------------------- |
| **`notifications`** | <code><a href="#permissionstate">PermissionState</a></code> |


#### FullScreenIntentPermissionStatus

| Prop                   | Type                                                        |
| ---------------------- | ----------------------------------------------------------- |
| **`fullScreenIntent`** | <code><a href="#permissionstate">PermissionState</a></code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


### Type Aliases


#### Partial

Make all properties in T optional

<code>{ [P in keyof T]?: T[P]; }</code>


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


#### NotificationResponse

<code>'tap' | 'answer' | 'decline' | 'terminate' | 'hold' | 'mute' | 'switchSession'</code>

</docgen-api>
