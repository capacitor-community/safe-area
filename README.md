<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Safe Area</h3>
<p align="center"><strong><code>@capacitor-community/safe-area</code></strong></p>
<p align="center">
  Capacitor Plugin that exposes the safe area insets from the native iOS/Android device to your web project.
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2024?style=flat-square" />
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/l/@capacitor-community/safe-area?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/dw/@capacitor-community/safe-area?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/v/@capacitor-community/safe-area?style=flat-square" /></a>
</p>

## Introduction

On web and iOS the safe area insets work out of the box. On Android (in combination with Capacitor), however, the safe area insets are not exposed by default. So we need to work some magic in order to have access to those values. This plugin detects the safe area insets on Android, and exposes them to the browser. On web and iOS it will just fallback to the given values (which, again, are working out of the box).

There's one small but important quirck though, since we cannot override the native `env(safe-area-inset-*)` variables, the values are instead written to custom `var(--safe-area-inset-*)` variables.

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

```ts
import { SafeArea } from '@capacitor-community/safe-area';

SafeArea.enable({
  customColorsForSystemBars: true,
  statusBarColor: '#00000000', // transparent
  statusBarContent: 'light',
  navigationBarColor: '#00000000', // transparent
  navigationBarContent: 'light',
});
```

> [!IMPORTANT]
> If you're enabling this plugin by using the configuration only and not by calling any of the API methods,
> you should still import the plugin like so:
>
> ```ts
> import '@capacitor-community/safe-area';
> ```

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
