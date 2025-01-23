<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Safe Area</h3>
<p align="center"><strong><code>@capacitor-community/safe-area</code></strong></p>
<p align="center">
  Capacitor Plugin that exposes the safe area insets from the native iOS/Android device to your web project.
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2025?style=flat-square" />
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/l/@capacitor-community/safe-area?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/dw/@capacitor-community/safe-area?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/v/@capacitor-community/safe-area?style=flat-square" /></a>
</p>

## Introduction

On web and iOS the safe area insets work out of the box<sup>1</sup>. That is, on these platforms the `env(safe-area-inset-*)` CSS variables will have the correct values by default. On Android (in combination with Capacitor), however, those CSS variables will not have the correct values when Edge-to-Edge mode is enabled. So we need to work some magic in order to have access to the correct values. This plugin does that by detecting the safe area insets on Android, and injecting them as CSS variables to the browser. On web and iOS it will just fallback to the given values (which, again, are working out of the box).

There's one small but important quirck though, since we cannot override the native `env(safe-area-inset-*)` variables, the values are instead written to custom `var(--safe-area-inset-*)` variables.

> [!NOTE]
> <sup>1</sup> As with all web applications, you still need to enable the use the variables, by telling the browser to use the whole available space on the screen by adding a new viewport meta value:
>
> ```html
> <meta name="viewport" content="viewport-fit=cover" />
> ```
>
> More info on the [Mozilla website](https://developer.mozilla.org/en-US/docs/Web/CSS/env#usage)

### Edge-to-Edge

You'll only need access to the safe area insets when you want to display your app edge-to-edge, by drawing your app content behind the system bars. The system bars are the status bar and the navigation bar. See the comparison table below for clarification:

<table>
  <thead>
    <tr>
      <th>Edge-to-Edge mode:</th>
      <th>Disabled</th>
      <th>Enabled</th>
      <th>Enabled</th>
    </tr>
  </thead>
  <tbody>
      <tr>
          <td>
              Safe Area Plugin installed:
          </td>
          <td>
              N/A.*
          </td>
          <td>
              Plugin not installed
          </td>
          <td>
              Plugin installed
          </td>
      </tr>
      <tr>
          <td>
              Screenshot:
          </td>
          <td>
              <img src="https://github.com/capacitor-community/in-app-review/assets/35837839/eeecc728-f776-4c44-9faf-fa93e84e4888" width="250" />
          </td>
          <td>
              <img src="https://github.com/capacitor-community/in-app-review/assets/35837839/d71fbe99-2dbd-4fda-9ca7-d186b946a0f3" width="250" />
          </td>
          <td>
              <img src="https://github.com/capacitor-community/in-app-review/assets/35837839/119d9ccf-cabc-4ddd-af52-3840d90c18a1" width="250" />
          </td>
      </tr>
  </tbody>
</table>

<sub>
*Plugin not needed when not in Edge-to-Edge mode. Because the safe area insets will always be 0px in this mode. So it wouldn't have any effect.
</sub>

<br>
<br>

This plugin, therefore, also provides utilities for styling those system bars and its content. It enables you to set the color of the bars to any color (e.g. black, white or transparent). It also enables you to set the color of the content (i.e. icon color) to either `light` or `dark`.

## Installation

```bash
npm install @capacitor-community/safe-area
npx cap sync
```

## Usage

### Enabling the plugin

This plugin can be enabled either by using the API or by using the Configuration. It's also possible to combine those two.

#### Enable by using the API

```ts
import { SafeArea } from '@capacitor-community/safe-area';

SafeArea.enable({
  config: {
    customColorsForSystemBars: true,
    statusBarColor: '#00000000', // transparent
    statusBarContent: 'light',
    navigationBarColor: '#00000000', // transparent
    navigationBarContent: 'light',
  },
});
```

#### Enable by using the Configuration

See the [Configuration documentation below](#examples) for examples.

> [!IMPORTANT]
> If you're enabling this plugin by using the configuration only and not by calling any of the API methods,
> you should still import the plugin like so:
>
> ```ts
> import '@capacitor-community/safe-area';
> ```

### Using the CSS variables

Having enabled the plugin, it will inject the correct safe area insets as CSS variables to the browser. This enables you to use those variables inside your CSS. You're now able to do this for example:

```css
#header {
  padding-top: var(--safe-area-inset-top);
}
```

Or maybe you want to do something like this:

```css
#header {
  padding-top: calc(var(--safe-area-inset-top) + 1rem);
}
```

> [!IMPORTANT]
> It's important to note that the used CSS variables are `var(--safe-area-inset-*)` and not `env(safe-area-inset-*)`.
> Unfortunately it's not (yet) possible to override the native `env(` variables. So therefore custom variables are injected instead.

> [!NOTE]
> On web and iOS both `var(--safe-area-inset-*)` and `env(safe-area-inset-*)` variables will be available.
> It's recommended to only use `var(--safe-area-inset-*)` for consistency though.
> The initialize call will make sure those variables are available on all platforms, including web and iOS.

### Using the plugin in an SSR environment

This plugin can be used in an SSR environment. But you should manually call `initialize` like so:

```ts
import { initialize } from '@capacitor-community/safe-area';

initialize();
```

Note that this step is only needed for users using this plugin in an SSR environment.

#### Background information on calling `initialize`

Calling `initialize` will set initial safe area values. It will inject `var(--safe-area-inset-*)` CSS variables with the values set to `max(env(safe-area-inset-*), 0px)` as properties to the document root. This makes sure the CSS variables can be used immediately and everywhere. This is especially important for Android when the plugin isn't enabled (yet) and always for web and iOS. Because otherwise - in those cases - `var(--safe-area-inset-*)` won't have any values.

## API

<docgen-index>

* [`enable(...)`](#enable)
* [`disable(...)`](#disable)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### enable(...)

```typescript
enable(options: { config: Config; }) => Promise<void>
```

| Param         | Type                                                   |
| ------------- | ------------------------------------------------------ |
| **`options`** | <code>{ config: <a href="#config">Config</a>; }</code> |

--------------------


### disable(...)

```typescript
disable(options: { config: Config; }) => Promise<void>
```

| Param         | Type                                                   |
| ------------- | ------------------------------------------------------ |
| **`options`** | <code>{ config: <a href="#config">Config</a>; }</code> |

--------------------


### Interfaces


#### Config

| Prop                            | Type                           | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Default                |
| ------------------------------- | ------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| **`customColorsForSystemBars`** | <code>boolean</code>           | Flag indicating that you are responsible for drawing the background color for the system bars. If `false` it will fallback to the default colors for the system bars.                                                                                                                                                                                                                                                                                                  | <code>true</code>      |
| **`statusBarColor`**            | <code>string</code>            | Specifies the background color of the status bar. Should be in the format `#RRGGBB` or `#AARRGGBB`. Will only have effect if `customColorsForSystemBars` is set to `true`.                                                                                                                                                                                                                                                                                             | <code>'#000000'</code> |
| **`statusBarContent`**          | <code>'light' \| 'dark'</code> | Specifies the color of the content (i.e. icon color) in the status bar.                                                                                                                                                                                                                                                                                                                                                                                                | <code>'light'</code>   |
| **`navigationBarColor`**        | <code>string</code>            | Specifies the background color of the navigation bar. Should be in the format `#RRGGBB` or `#AARRGGBB`. Will only have effect if `customColorsForSystemBars` is set to `true`.                                                                                                                                                                                                                                                                                         | <code>'#000000'</code> |
| **`navigationBarContent`**      | <code>'light' \| 'dark'</code> | Specifies the color of the content (i.e. icon color) in the navigation bar.                                                                                                                                                                                                                                                                                                                                                                                            | <code>'light'</code>   |
| **`offset`**                    | <code>number</code>            | Specifies the offset to be applied to the safe area insets. This means that if the safe area top inset is 30px, and the offset specified is 10px, the safe area top inset will be exposed as being 40px. Usually you don't need this, but on iOS the safe area insets are mostly offset a little more by itself already. So you might want to compensate for that on Android. It's totally up to you. The offset will be applied if Edge-to-Edge mode is enabled only. | <code>0</code>         |

</docgen-api>

## Configuration

<docgen-config>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

For ease of use and speed, these configuration values are available:

| Prop                            | Type                           | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                            | Default                |
| ------------------------------- | ------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| **`enabled`**                   | <code>boolean</code>           | Flag indicating whether of not the plugin should be enabled from startup.                                                                                                                                                                                                                                                                                                                                                                                              | <code>false</code>     |
| **`customColorsForSystemBars`** | <code>boolean</code>           | Flag indicating that you are responsible for drawing the background color for the system bars. If `false` it will fallback to the default colors for the system bars.                                                                                                                                                                                                                                                                                                  | <code>true</code>      |
| **`statusBarColor`**            | <code>string</code>            | Specifies the background color of the status bar. Should be in the format `#RRGGBB` or `#AARRGGBB`. Will only have effect if `customColorsForSystemBars` is set to `true`.                                                                                                                                                                                                                                                                                             | <code>'#000000'</code> |
| **`statusBarContent`**          | <code>'light' \| 'dark'</code> | Specifies the color of the content (i.e. icon color) in the status bar.                                                                                                                                                                                                                                                                                                                                                                                                | <code>'light'</code>   |
| **`navigationBarColor`**        | <code>string</code>            | Specifies the background color of the navigation bar. Should be in the format `#RRGGBB` or `#AARRGGBB`. Will only have effect if `customColorsForSystemBars` is set to `true`.                                                                                                                                                                                                                                                                                         | <code>'#000000'</code> |
| **`navigationBarContent`**      | <code>'light' \| 'dark'</code> | Specifies the color of the content (i.e. icon color) in the navigation bar.                                                                                                                                                                                                                                                                                                                                                                                            | <code>'light'</code>   |
| **`offset`**                    | <code>number</code>            | Specifies the offset to be applied to the safe area insets. This means that if the safe area top inset is 30px, and the offset specified is 10px, the safe area top inset will be exposed as being 40px. Usually you don't need this, but on iOS the safe area insets are mostly offset a little more by itself already. So you might want to compensate for that on Android. It's totally up to you. The offset will be applied if Edge-to-Edge mode is enabled only. | <code>0</code>         |

### Examples

In `capacitor.config.json`:

```json
{
  "plugins": {
    "SafeArea": {
      "enabled": true,
      "customColorsForSystemBars": true,
      "statusBarColor": '#000000',
      "statusBarContent": 'light',
      "navigationBarColor": '#000000',
      "navigationBarContent": 'light',
      "offset": 0
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
      enabled: true,
      customColorsForSystemBars: true,
      statusBarColor: '#000000',
      statusBarContent: 'light',
      navigationBarColor: '#000000',
      navigationBarContent: 'light',
      offset: 0,
    },
  },
};

export default config;
```

</docgen-config>
