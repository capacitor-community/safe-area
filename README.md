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

On web and iOS the safe area insets work perfectly fine out of the box<sup>1</sup>. That is, on these platforms the `env(safe-area-inset-*)` CSS variables will have the correct values by default. On Android (in combination with Capacitor), however, those CSS variables will not always have the correct values when Edge-to-Edge mode is enabled. So we need to work some magic in order to have correct behavior. This plugin does that by detecting the Chromium version a user has installed. If a user has a Chromium version lower than 140, this plugin makes sure the webview gets the safe area as a margin. The `env(safe-area-inset-*)` values will be set to `0px`. That means for the Chromium webview versions with a bug, the developer doesn't have to worry about safe area insets at all. For all other versions, the developer should handle the safe area insets just as he would on web or iOS (by using the `env(safe-area-inset-*)` CSS variables). In short you can think of this plugin as a polyfill for older webview versions.

> [!NOTE]
>
> <sup>1</sup> As with all web applications, you still need to enable the use the variables, by telling the browser to
> use the whole available space on the screen by adding a new viewport meta value:
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

Additionally this plugin provides a helper method to style the system bars and its content. Currently, this only works by calling it from the `MainActivity`. For example:

```kotlin
SafeAreaPlugin.setSystemBarsStyle(this, SafeAreaPlugin.SystemBarsStyle.DARK)
```

## Quirks

Currently there are a few known quirks:

### `@capacitor/keyboard`

The keyboard plugin by Capacitor has a configuration prop called `resizeOnFullScreen`. You should prevent to utilize that in your code by either omitting it or setting it to `false`. Otherwise it would interfere with the logic in this plugin. The bug that the Capacitor team is trying to workaround with that prop is already accounted for in this plugin. So you should be good to go a don't worry about it at all.

### `@capacitor/status-bar`

This plugin is not (yet) tested for use in combination with the status bar plugin. Ideally you should refrain from using it. Just using the `StatusBar.setStyle` should be safe though.

### `adjustMarginsForEdgeToEdge` setting

Capacitor provides a setting called `adjustMarginsForEdgeToEdge` which does a similar thing this plugin does when it detects a broken webview. It's advised to set this value to `disable` to prevent interference. On a similar note, you should probably remove `windowOptOutEdgeToEdgeEnforcement` if you've set that in the past.

### Other safe area plugins

If you've installed any other safe area plugin, you should remove them to prevent interference.

These can include `@capawesome/capacitor-android-edge-to-edge-support`, `capacitor-plugin-safe-area`, `@aashu-dubey/capacitor-statusbar-safe-area` or even older versions of this plugin. You should make sure to uninstall those.

### Ongoing Chromium bug

There's a known Chromium bug that makes the webview report the bottom inset as more than `0px` even if the keyboard is visible. It will be fixed in Chromium version 144 ([source](https://issues.chromium.org/issues/457682720)).

### Respecting the `viewport-fit=cover` tag

When this plugin is installed, edge-to-edge mode is always enabled. Regardless of any other setting (except for broken webview versions of course). This means that the `viewport-fit=cover` tag is also not respected. So you should make sure any content loaded into the webview handles the `env(safe-area-inset-*)` accordingly. This is a known shortcoming of this plugin currently. I'm looking for ways to fix this, but haven't yet come up with a solution. For most use cases this shouldn't be a problem though. As most apps either do or don't support edge-to-edge. PRs to fix this are warmly welcomed!

### Future work

The Capacitor team themselves are also working on a [similar solution](https://github.com/ionic-team/capacitor/pull/8180). It's still a work in progress though. You should be aware that you might have to migrate once they've integrated that PR into their codebase.
