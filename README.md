# @anuradev/capacitor-phone-call-notification

Capacitor plugin to create phone call notifications

## Install

```bash
npm install @anuradev/capacitor-phone-call-notification
npx cap sync
```

## API

<docgen-index>

* [`show(...)`](#show)
* [`hide(...)`](#hide)
* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [`addListener('response', ...)`](#addlistenerresponse-)
* [`removeAllListeners()`](#removealllisteners)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### show(...)

```typescript
show(data?: Partial<NotificationSettings> | undefined) => Promise<void>
```

| Param      | Type                                                                                                        |
| ---------- | ----------------------------------------------------------------------------------------------------------- |
| **`data`** | <code><a href="#partial">Partial</a>&lt;<a href="#notificationsettings">NotificationSettings</a>&gt;</code> |

--------------------


### hide(...)

```typescript
hide(data: { type: NotificationType; }) => Promise<void>
```

| Param      | Type                                                                     |
| ---------- | ------------------------------------------------------------------------ |
| **`data`** | <code>{ type: <a href="#notificationtype">NotificationType</a>; }</code> |

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


#### NotificationSettings

| Prop                                | Type                                                          |
| ----------------------------------- | ------------------------------------------------------------- |
| **`type`**                          | <code><a href="#notificationtype">NotificationType</a></code> |
| **`duration`**                      | <code>number</code>                                           |
| **`callerName`**                    | <code>string</code>                                           |
| **`callerNumber`**                  | <code>string</code>                                           |
| **`icon`**                          | <code>string</code>                                           |
| **`picture`**                       | <code>string</code>                                           |
| **`thereIsACallInProgress`**        | <code>boolean</code>                                          |
| **`declineButtonText`**             | <code>string</code>                                           |
| **`declineButtonColor`**            | <code>string</code>                                           |
| **`answerButtonText`**              | <code>string</code>                                           |
| **`answerButtonColor`**             | <code>string</code>                                           |
| **`terminateButtonText`**           | <code>string</code>                                           |
| **`terminateButtonColor`**          | <code>string</code>                                           |
| **`holdButtonText`**                | <code>string</code>                                           |
| **`holdButtonColor`**               | <code>string</code>                                           |
| **`terminateAndAnswerButtonText`**  | <code>string</code>                                           |
| **`terminateAndAnswerButtonColor`** | <code>string</code>                                           |
| **`declineSecondCallButtonText`**   | <code>string</code>                                           |
| **`declineSecondCallButtonColor`**  | <code>string</code>                                           |
| **`holdAndAnswerButtonText`**       | <code>string</code>                                           |
| **`holdAndAnswerButtonColor`**      | <code>string</code>                                           |
| **`color`**                         | <code>string</code>                                           |
| **`channelName`**                   | <code>string</code>                                           |
| **`channelDescription`**            | <code>string</code>                                           |


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


#### NotificationType

<code>'incoming' | 'inProgress' | 'missed'</code>


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


#### NotificationResponse

<code>'tap' | 'answer' | 'decline' | 'terminate' | 'hold'</code>

</docgen-api>
