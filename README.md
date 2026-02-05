<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Safe Area</h3>
<p align="center"><strong><code>@capacitor-community/safe-area</code></strong></p>
<p align="center">
  Capacitor Plugin that patches the safe area for older versions of Chromium
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2026?style=flat-square" />
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/l/@capacitor-community/safe-area?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/dw/@capacitor-community/safe-area?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/v/@capacitor-community/safe-area?style=flat-square" /></a>
</p>

## Introduction

On web and iOS the safe area insets work perfectly fine out of the box<sup>1</sup>. That is, on these platforms the `env(safe-area-inset-*)` CSS variables will have the correct values by default. On Android (in combination with Capacitor), however, those CSS variables will not always have the correct values when Edge-to-Edge mode is enabled. So we need to work some magic in order to achieve the desired behavior. This plugin does that by detecting the Chromium version a user has installed. If a user has a Chromium version lower than 140, this plugin makes sure the webview gets the safe area as a padding. The `env(safe-area-inset-*)` values will be set to `0px`. That means for the Chromium webview versions with a bug, the developer doesn't have to worry about safe area insets at all. For all other versions, the developer should handle the safe area insets just as he would on web or iOS (by using the `env(safe-area-inset-*)` CSS variables). In short you can think of this plugin as a polyfill for older webview versions.

> [!NOTE]
>
> <sup>1</sup> As with all (regular) web applications, you still need to tell the browser to scale the viewport as so to
> fill the device display by setting a viewport meta value like so:
>
> ```html
> <meta name="viewport" content="viewport-fit=cover" />
> ```
>
> More info on the Mozilla website about setting the [`<meta name="viewport">`](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Elements/meta/name/viewport#viewport-fit) and [using CSS env()](https://developer.mozilla.org/en-US/docs/Web/CSS/Reference/Values/env#using_env_to_ensure_buttons_are_not_obscured_by_device_ui).

## Installation

### For Capacitor v8

```bash
npm install @capacitor-community/safe-area
npx cap sync
```

### For Capacitor v7

```bash
npm install @capacitor-community/safe-area@^7.0.0
npx cap sync
```

## Usage

The plugin itself is enabled automatically. Additionally, you should enable native edge-to-edge mode in your `MainActivity` like so:

Java:

```java
import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import androidx.activity.EdgeToEdge;

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

import android.os.Bundle;
import com.getcapacitor.BridgeActivity;
import androidx.activity.EdgeToEdge;

class MainActivity : BridgeActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // enable edge-to-edge mode
    }
}
```

Additionally, this plugin provides helper methods to style the system bars and its content. You can find the API docs [here](#system-bars-api).

## Setup

This plugin might conflict with some of your existing setup. Please go through the following steps, to ensure proper functioning of this plugin.

> If you're installing this plugin into a fresh Capacitor setup, you can probably skip most - if not all - of these steps.

### `@capacitor/keyboard`

The keyboard plugin by Capacitor has a configuration prop called `resizeOnFullScreen`. You should either omit it or set it to `false`. Otherwise it would interfere with the logic in this plugin. The bug that the Capacitor team is trying to fix with that prop is already accounted for in this plugin. So you should be good to go a don't worry about it at all.

### `@capacitor/status-bar`

When using `@capacitor-community/safe-area`, you should uninstall `@capacitor/status-bar`. Instead you can use the [System Bars API](#system-bars-api) this plugin provides.

### `adjustMarginsForEdgeToEdge` setting

Capacitor provides a setting called `adjustMarginsForEdgeToEdge`. It's advised to either omit this value or set it to `disable` to prevent interference. This plugin already does a similar thing when it detects a broken webview.

### `windowOptOutEdgeToEdgeEnforcement`

If you've set `windowOptOutEdgeToEdgeEnforcement` in your `AndroidManifest.xml`, you should probably remove it. It has been deprecated and shouldn't be necessary when using this plugin anyways.

### Extending `BridgeWebViewClient` and calling `setWebViewClient`

If you're running Capacitor v7 and have extended the `BridgeWebViewClient` by calling `bridge.setWebViewClient` in your code, you should update your code so that it extends `SafeAreaWebViewClient` instead. If you're running Capacitor v8 you do not have to worry about this at all.

### Other safe area plugins

If you've installed any other safe area plugin, you should remove them to prevent interference.

Examples of these are: `@capawesome/capacitor-android-edge-to-edge-support`, `capacitor-plugin-safe-area` and `@aashu-dubey/capacitor-statusbar-safe-area`.

Just make sure to uninstall those and you should be good to go.

### Earlier versions of this plugin

Alpha versions of this plugin (`@capacitor-community/safe-area@alpha`) are deprecated and usage of those versions is advised against. Please migrate to a `latest` channel instead. Differences between the older versions and the newer versions are outlined [here](https://github.com/capacitor-community/safe-area/issues/82#issuecomment-3600442770).

### Capacitor v8

If you're running Capacitor v8, you should set the following in your `capacitor.config.json`:

```json
{
  "plugins": {
    "SystemBars": {
      "insetsHandling": "disable"
    }
  }
}
```

## Differences between this plugin, `system-bars` and `status-bar`

The main differences are as follows:

|                                                       | [`@capacitor-community/safe-area`](https://github.com/capacitor-community/safe-area)                                        | [`CapacitorSystemBars`](https://capacitorjs.com/docs/apis/system-bars)<sup>5</sup>                                                                                                                                     | [`@capacitor/status-bar`](https://capacitorjs.com/docs/apis/status-bar) |
| ----------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------- |
| Supported Capacitor versions                          | ✅ v7+                                                                                                                      | ⚠️ v8+                                                                                                                                                                                                                 | ✅ v7+                                                                  |
| Helper methods for styling and hiding the system bars | ✅                                                                                                                          | ✅                                                                                                                                                                                                                     | ✅                                                                      |
| Updates styling of system bars upon config changes    | ✅                                                                                                                          | ✅                                                                                                                                                                                                                     | ✅                                                                      |
| Enables Edge to Edge functionality                    | ✅                                                                                                                          | ✅                                                                                                                                                                                                                     | ❌                                                                      |
| Option to opt out of handling insets for Android      | ✅                                                                                                                          | ❌                                                                                                                                                                                                                     | N/A                                                                     |
| Philosophy<sup>2</sup>                                | ✅ Native approach. Aligns with iOS and web behavior. Does not require extra work. Fallback to padding for broken webviews. | ⚠️ All or nothing approach using custom CSS vars injection. Requires extra work (migrating from envs to vars). Doesn't distinguish between broken and fixed webview versions. It's arguably more prone to future bugs. | N/A                                                                     |
| Fallback supported on Android versions                | ✅ Android 6+                                                                                                               | ❌ Android 15+ ([broken](https://github.com/ionic-team/capacitor/pull/8268#discussion_r2610099030) behavior on older versions)                                                                                         | N/A                                                                     |
| Workaround for resizing keyboard bug<sup>3</sup>      | ✅ Working out of the box                                                                                                   | ⚠️ Requires installing `@capacitor/keyboard` and setting `resizeOnFullScreen`. Their workaround also still has [bugs](https://github.com/ionic-team/capacitor/pull/8180#issuecomment-3512336606)                       | ❌                                                                      |
| Workaround for inset keyboard bug<sup>4</sup>         | ✅ Working out of the box                                                                                                   | ❌                                                                                                                                                                                                                     | N/A                                                                     |
| Detects `viewport-fit=cover` changes                  | ✅ Works for any arbitrary web content                                                                                      | ⚠️ Works only for web content that has Capacitor v8 set up                                                                                                                                                             | N/A                                                                     |

<sup>2</sup> Chromium versions < 140 do not correctly report safe area insets. So we need a workaround for those webviews. This can be done using padding or custom CSS vars. This plugin advocates for using padding. As it seems to be the least breaking behavior.

<sup>3</sup> The webview has a known bug to not resize the webview when the keyboard is shown.

<sup>4</sup> The webview has [another known bug](https://issues.chromium.org/issues/457682720) to not properly report
bottom insets when the keyboard is shown. Which will be fixed in Chromium 144

<sup>5</sup> The Capacitor team themselves are also working on a [similar
solution](https://github.com/ionic-team/capacitor/pull/8180) by means of a `CapacitorSystemBars` plugin. It's still a
work in progress though. Currently that PR is merged as is and in a beta state. Their design/approach philosophy also
deviates from the one in this plugin.

## FAQ

### Do I need this plugin?

You probably do! Apps targeting Android sdk version 36 will automatically be in edge to edge mode on a device running Android 16+. This means you should properly support safe area insets. This seemingly works just fine. However Chromium versions < 140 have a bug that causes the webview to receive incorrect values (0px). This plugin works around that by adding a padding to the webview for those devices. As a bonus, this plugin resizes the webview upon keyboard visibility changes, something that also isn't standard.

### Why doesn't Capacitor take care of this out of the box?

They can and they should. They tried / are trying actually. But IMHO their approach isn't ready for production (yet). The differences between their approach and the one of this plugin are outlined [here](#system-bars-api). I try to adhere to their coding style and I am actively providing feedback to the Capacitor team in hopes they will - sometime - upstream this plugin into their core codebase.

### I don't care about safe area insets or edge to edge functionality!

That's not a question ;) And your users might care about them though! So it's recommended to support them. But if you really do not want to support edge to edge, you've got two options:

1. Remove the `viewport-fit=cover` meta tag if you've set that. Mind you that this removes edge to edge support entirely, for all platforms (Android, iOS and web)
2. If you want to only remove support for Android, set the following value in your `capacitor.config.json`:
   ```json
   {
     "plugins": {
       "SafeArea": {
         "detectViewportFitCoverChanges": false,
         "initialViewportFitCover": false
       }
     }
   }
   ```
   This effectively makes this plugin to only add padding around the webview on all of your Android devices. And disables detecting the `viewport-fit` value and disables passing through insets (because they're already consumed by the padding).

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

--------------------


### showSystemBars(...)

```typescript
showSystemBars(options: SystemBarsVisibilityOptions) => Promise<void>
```

| Param         | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#systembarsvisibilityoptions">SystemBarsVisibilityOptions</a></code> |

--------------------


### hideSystemBars(...)

```typescript
hideSystemBars(options: SystemBarsVisibilityOptions) => Promise<void>
```

| Param         | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`options`** | <code><a href="#systembarsvisibilityoptions">SystemBarsVisibilityOptions</a></code> |

--------------------


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

| Prop                                | Type                                                        | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Default           |
| ----------------------------------- | ----------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ----------------- |
| **`statusBarStyle`**                | <code><a href="#systembarsstyle">SystemBarsStyle</a></code> | Indicates which style to apply to the status bar initially.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | <code>null</code> |
| **`navigationBarStyle`**            | <code><a href="#systembarsstyle">SystemBarsStyle</a></code> | Indicates which style to apply to the navigation bar initially. On iOS the home indicator cannot be styled. It will always automatically be applied a color by iOS out of the box.                                                                                                                                                                                                                                                                                                                                                                                                                                      | <code>null</code> |
| **`detectViewportFitCoverChanges`** | <code>boolean</code>                                        | This plugin detects changes to the `viewport-fit=cover` meta tag. This comes in handy when you do not know for sure if the content loaded into the webview will have `viewport-fit` set to `cover`. For most use cases you do not need to touch this config variable. However if you know for sure you want to always keep the `initialViewportFitCover` value unchanged, you could disable this feature by setting it to `false`. Be aware that this might result in a visually broken UI if the content loaded into the webview does not correctly handle safe area insets. This option is only supported on Android. | <code>true</code> |
| **`initialViewportFitCover`**       | <code>boolean</code>                                        | Set an initial value for the to be detected `viewport-fit=cover`. For most apps this value will eventually be `true`. Therefore this value is set to `true` by default to help prevent layout jumps and glitches. If you know (or want) the value to be `false` initially, you can set it here. The value will always end up correctly, no matter what you set here, as long as `detectViewportFitCoverChanges` is set to `true`. It only exists to help prevent layout jumps and glitches. This option is only supported on Android.                                                                                   | <code>true</code> |
| **`offsetForKeyboardInsetBug`**     | <code>boolean</code>                                        |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |                   |

### Examples

In `capacitor.config.json`:

```json
{
  "plugins": {
    "SafeArea": {
      "statusBarStyle": undefined,
      "navigationBarStyle": undefined,
      "detectViewportFitCoverChanges": undefined,
      "initialViewportFitCover": undefined,
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
      detectViewportFitCoverChanges: undefined,
      initialViewportFitCover: undefined,
      offsetForKeyboardInsetBug: undefined,
    },
  },
};

export default config;
```

</docgen-config>
