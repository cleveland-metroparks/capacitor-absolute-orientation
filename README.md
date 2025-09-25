# capacitor-absolute-orientation

Provides access to the device absolute orientation sensor.

## Install

```bash
npm install capacitor-absolute-orientation
npx cap sync
```

## API

<docgen-index>

* [`start()`](#start)
* [`stop()`](#stop)
* [`isActivated()`](#isactivated)
* [`addReadingListener(...)`](#addreadinglistener)
* [`removeReadingListener(...)`](#removereadinglistener)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### start()

```typescript
start() => Promise<void>
```

--------------------


### stop()

```typescript
stop() => Promise<void>
```

--------------------


### isActivated()

```typescript
isActivated() => Promise<{ value: boolean; }>
```

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### addReadingListener(...)

```typescript
addReadingListener(callback: (reading: AbsoluteOrientationReading) => void) => Promise<CallbackId>
```

| Param          | Type                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| **`callback`** | <code>(reading: <a href="#absoluteorientationreading">AbsoluteOrientationReading</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;string&gt;</code>

--------------------


### removeReadingListener(...)

```typescript
removeReadingListener(id: CallbackId) => Promise<void>
```

| Param    | Type                |
| -------- | ------------------- |
| **`id`** | <code>string</code> |

--------------------


### Interfaces


#### AbsoluteOrientationReading

| Prop                 | Type                                              |
| -------------------- | ------------------------------------------------- |
| **`timestamp`**      | <code>number</code>                               |
| **`quaternion`**     | <code><a href="#quaternion">Quaternion</a></code> |
| **`compassHeading`** | <code>number</code>                               |


#### Quaternion

| Prop    | Type                |
| ------- | ------------------- |
| **`x`** | <code>number</code> |
| **`y`** | <code>number</code> |
| **`z`** | <code>number</code> |
| **`w`** | <code>number</code> |


### Type Aliases


#### CallbackId

<code>string</code>

</docgen-api>
