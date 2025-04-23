# @anuradev/capacitor-phone-call-notification

Capacitor plugin to create phone call notifications

## Install

```bash
npm install @anuradev/capacitor-phone-call-notification
npx cap sync
```

## API

<docgen-index>

* [`showIncomingPhoneCallNotification(...)`](#showincomingphonecallnotification)
* [`showCallInProgressNotification(...)`](#showcallinprogressnotification)
* [`hideIncomingPhoneCallNotification()`](#hideincomingphonecallnotification)
* [`hideCallInProgressNotification()`](#hidecallinprogressnotification)
* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [`addListener('response', ...)`](#addlistenerresponse-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

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


### checkPermissions()

```typescript
checkPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<PermissionStatus>
```

**Returns:** <code>Promise&lt;<a href="#permissionstatus">PermissionStatus</a>&gt;</code>

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

| Prop                       | Type                |
| -------------------------- | ------------------- |
| **`icon`**                 | <code>string</code> |
| **`picture`**              | <code>string</code> |
| **`terminateButtonText`**  | <code>string</code> |
| **`terminateButtonColor`** | <code>string</code> |
| **`holdButtonText`**       | <code>string</code> |
| **`holdButtonColor`**      | <code>string</code> |
| **`color`**                | <code>string</code> |
| **`duration`**             | <code>number</code> |
| **`channelName`**          | <code>string</code> |
| **`channelDescription`**   | <code>string</code> |
| **`callingName`**          | <code>string</code> |
| **`callingNumber`**        | <code>string</code> |


#### PermissionStatus

| Prop          | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`display`** | <code><a href="#permissionstate">PermissionState</a></code> |


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

<code>'tap' | 'answer' | 'decline' | 'terminate' | 'hold'</code>

</docgen-api>
