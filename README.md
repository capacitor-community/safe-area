<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Safe Area</h3>
<p align="center"><strong><code>@capacitor-community/safe-area</code></strong></p>
<p align="center">
  Capacitor Plugin that patches the safe area for older versions of Chromium
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2025?style=flat-square" />
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/l/@capacitor-community/safe-area?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/dw/@capacitor-community/safe-area?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/v/@capacitor-community/safe-area?style=flat-square" /></a>
</p>

## Introduction

On web and iOS the safe area insets work perfectly fine out of the box<sup>1</sup>. That is, on these platforms the `env(safe-area-inset-*)` CSS variables will have the correct values by default. On Android (in combination with Capacitor), however, those CSS variables will not always have the correct values when Edge-to-Edge mode is enabled. So we need to work some magic in order to have correct behavior. This plugin does that by detecting the Chromium version a user has installed. If a user has a Chromium version lower than 140, this plugin makes sure the webview gets the safe area as a padding. The `env(safe-area-inset-*)` values will be set to `0px`. That means for the Chromium webview versions with a bug, the developer doesn't have to worry about safe area insets at all. For all other versions, the developer should handle the safe area insets just as he would on web or iOS (by using the `env(safe-area-inset-*)` CSS variables). In short you can think of this plugin as a polyfill for older webview versions.

> [!NOTE]
>
> <sup>1</sup> As with all web applications, you still need to tell the browser to use the whole available space on the
> screen by adding a new viewport meta value:
>
> ```html
> <meta name="viewport" content="viewport-fit=cover" />
> ```
>
> More info on the [Mozilla website](https://developer.mozilla.org/en-US/docs/Web/CSS/env#usage)

## Installation

```bash
npm install @capacitor-community/safe-area@beta
npx cap sync
```

## Usage

The plugin itself is enabled automatically. Additionally, you should enable native edge-to-edge mode in your `MainActivity` like so:

Java:

```java
public class MainActivity extends BridgeActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // enable edge-to-edge mode
    }
}
```

Kotlin:

```kotlin
class MainActivity : BridgeActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // enable edge-to-edge mode
    }
}
```

Additionally this plugin provides helper methods to style the system bars and its content. You can find the API docs [here](#system-bars-api).

## Quirks

Currently there are a few known quirks:

### `@capacitor/keyboard`

The keyboard plugin by Capacitor has a configuration prop called `resizeOnFullScreen`. You should prevent to utilize that in your code by either omitting it or setting it to `false`. Otherwise it would interfere with the logic in this plugin. The bug that the Capacitor team is trying to workaround with that prop is already accounted for in this plugin. So you should be good to go a don't worry about it at all.

### `@capacitor/status-bar`

When using `@capacitor-community/safe-area`, you should uninstall `@capacitor/status-bar`. Instead you can use the [System Bars API](#system-bars-api) this plugin provides.

### `adjustMarginsForEdgeToEdge` setting

Capacitor provides a setting called `adjustMarginsForEdgeToEdge` which does a similar thing this plugin does when it detects a broken webview. It's advised to set this value to `disable` to prevent interference. On a similar note, you should probably remove `windowOptOutEdgeToEdgeEnforcement` if you've set that in the past.

### Other safe area plugins

If you've installed any other safe area plugin, you should remove them to prevent interference.

These can include `@capawesome/capacitor-android-edge-to-edge-support`, `capacitor-plugin-safe-area`, `@aashu-dubey/capacitor-statusbar-safe-area` or even older versions of this plugin. You should make sure to uninstall those.

### Respecting the `viewport-fit=cover` tag

When this plugin is installed, edge-to-edge mode is always enabled. Regardless of any other setting (except for broken webview versions of course). This means that the `viewport-fit=cover` tag is also not respected. So you should make sure any content loaded into the webview handles the `env(safe-area-inset-*)` accordingly. This is a known shortcoming of this plugin currently. I know how to fix this, but I'm awaiting answers from the Chromium team. Progress can be tracked [here](https://issues.chromium.org/issues/461332423). For most use cases this shouldn't be a problem though. As most apps either do or don't support edge-to-edge.

### Differences between this plugin, `system-bars` and `status-bar`

The main differences are as follows:

|                                                       | [`@capacitor-community/safe-area`](https://github.com/capacitor-community/safe-area) | [`CapacitorSystemBars`](https://github.com/ionic-team/capacitor/blob/main/core/systembars.md)<sup>5</sup>                                                                                        | [`@capacitor/status-bar`](https://capacitorjs.com/docs/apis/status-bar) |
| ----------------------------------------------------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------------- |
| Supported Capacitor versions                          | ✅ v7+                                                                               | ⚠️ v8+                                                                                                                                                                                           | ✅ v7+                                                                  |
| Helper methods for styling and hiding the system bars | ✅                                                                                   | ✅                                                                                                                                                                                               | ✅                                                                      |
| Enables Edge to Edge functionality                    | ✅                                                                                   | ✅                                                                                                                                                                                               | ❌                                                                      |
| Fallback for broken webviews<sup>2</sup>              | ✅ Padding (does not require extra work)                                             | ⚠️ Custom CSS vars (requires extra work and is more prone to future bugs)                                                                                                                        | ❌                                                                      |
| Workaround for keyboard bug<sup>3</sup>               | ✅ Working out of the box                                                            | ⚠️ Requires installing `@capacitor/keyboard` and setting `resizeOnFullScreen`. Their workaround also still has [bugs](https://github.com/ionic-team/capacitor/pull/8180#issuecomment-3512336606) | ❌                                                                      |
| Workaround for another keyboard bug<sup>4</sup>       | ✅ Working out of the box                                                            | ❌                                                                                                                                                                                               | ❌                                                                      |

<sup>2</sup> Chromium versions < 140 do not correctly report safe area insets. So we need a workaround for those webviews. This can be done using padding or custom CSS vars. This plugin advocates for using padding. As it seems to be the least breaking behavior.

<sup>3</sup> The webview has a known bug to not resize the webview when the keyboard is shown.

<sup>4</sup> The webview has [another known bug](https://issues.chromium.org/issues/457682720) to not properly report
bottom insets when the keyboard is shown. Which will be fixed in Chromium 144

<sup>5</sup> The Capacitor team themselves are also working on a [similar
solution](https://github.com/ionic-team/capacitor/pull/8180) by means of a `CapacitorSystemBars` plugin. It's still a
work in progress though. Currently that PR is merged as is and in a beta state. Their design/approach philosophy also
deviates from the one in this plugin.

## System Bars API

This plugin provides a few helper methods for styling the system bars. It's designed to be a partial drop-in replacement for the `@capacitor/status-bar` plugin. It doesn't support a few things, like `setOverlaysWebView` for example, as those aren't applicable when handling the safe areas using insets.

> [!NOTE]
> On iOS this API requires "View controller-based status bar appearance" (`UIViewControllerBasedStatusBarAppearance`) set to `YES` in `Info.plist`. Read about [Configuring iOS](https://capacitorjs.com/docs/ios/configuration) for help.
>
> The status bar visibility defaults to visible and the style defaults to [`UIStatusBarStyle.default`](https://developer.apple.com/documentation/uikit/uistatusbarstyle/default). You can change these defaults by adding [`UIStatusBarHidden`](https://developer.apple.com/documentation/bundleresources/information-property-list/uistatusbarhidden) and/or [`UIStatusBarStyle`](https://developer.apple.com/documentation/uikit/uistatusbarstyle) in `Info.plist`.

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### setSystemBarsStyle(...)

```typescript
setSystemBarsStyle(options: SystemBarsStyleOptions) => Promise<void>
```

| Param         | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`options`** | <code><a href="#systembarsstyleoptions">SystemBarsStyleOptions</a></code> |

---

### showSystemBars(...)

```typescript
showSystemBars(options: SystemBarsVisibilityOptions) => Promise<void>
```

| Param         | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#systembarsvisibilityoptions">SystemBarsVisibilityOptions</a></code> |

---

### hideSystemBars(...)

```typescript
hideSystemBars(options: SystemBarsVisibilityOptions) => Promise<void>
```

| Param         | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#systembarsvisibilityoptions">SystemBarsVisibilityOptions</a></code> |

---

### Interfaces

#### SystemBarsStyleOptions

| Prop        | Type                                                        | Description                                                                                                                                                                                                                   | Default                |
| ----------- | ----------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| **`style`** | <code><a href="#systembarsstyle">SystemBarsStyle</a></code> | Style of the content of the system bars.                                                                                                                                                                                      | <code>'DEFAULT'</code> |
| **`type`**  | <code><a href="#systembarstype">SystemBarsType</a></code>   | The system bar to which to apply the style. Providing `null` means it will be applied to both system bars. On iOS the home indicator cannot be styled. It will always automatically be applied a color by iOS out of the box. | <code>null</code>      |

#### SystemBarsVisibilityOptions

| Prop       | Type                                                      | Description                                                                             | Default           |
| ---------- | --------------------------------------------------------- | --------------------------------------------------------------------------------------- | ----------------- |
| **`type`** | <code><a href="#systembarstype">SystemBarsType</a></code> | The system bar to hide or show. Providing `null` means it will toggle both system bars. | <code>null</code> |

### Enums

#### SystemBarsStyle

| Members       | Value                  | Description                                                                                                                                                                                                              |
| ------------- | ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`Dark`**    | <code>'DARK'</code>    | Light system bar content on a dark background.                                                                                                                                                                           |
| **`Light`**   | <code>'LIGHT'</code>   | For dark system bar content on a light background.                                                                                                                                                                       |
| **`Default`** | <code>'DEFAULT'</code> | The style is based on the device appearance or the underlying content. If the device is using dark mode, the system bars content will be light. If the device is using light mode, the system bars content will be dark. |

#### SystemBarsType

| Members             | Value                         | Description                                                                                                                                           |
| ------------------- | ----------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`StatusBar`**     | <code>'STATUS_BAR'</code>     | The top status bar on both Android and iOS.                                                                                                           |
| **`NavigationBar`** | <code>'NAVIGATION_BAR'</code> | The navigation bar on both Android and iOS. On iOS this is the "home indicator". On Android this is either the "navigation bar" or the "gesture bar". |

</docgen-api>

### Config

<docgen-config>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

| Prop                            | Type                                                        | Description                                                                                                                                                                        | Default           |
| ------------------------------- | ----------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- |
| **`statusBarStyle`**            | <code><a href="#systembarsstyle">SystemBarsStyle</a></code> | Indicates which style to apply to the status bar initially.                                                                                                                        | <code>null</code> |
| **`navigationBarStyle`**        | <code><a href="#systembarsstyle">SystemBarsStyle</a></code> | Indicates which style to apply to the navigation bar initially. On iOS the home indicator cannot be styled. It will always automatically be applied a color by iOS out of the box. | <code>null</code> |
| **`offsetForKeyboardInsetBug`** | <code>boolean</code>                                        |                                                                                                                                                                                    |                   |

### Examples

In `capacitor.config.json`:

```json
{
  "plugins": {
    "SafeArea": {
      "statusBarStyle": undefined,
      "navigationBarStyle": undefined,
      "offsetForKeyboardInsetBug": undefined
    }
  }
}
```

In `capacitor.config.ts`:

```ts
/// <reference types="@capacitor-community/safe-area" />

import { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  plugins: {
    SafeArea: {
      statusBarStyle: undefined,
      navigationBarStyle: undefined,
      offsetForKeyboardInsetBug: undefined,
    },
  },
};

export default config;
```

</docgen-config>
