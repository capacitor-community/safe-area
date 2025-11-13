/// <reference types="@capacitor/cli" />

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    SafeArea?: {
      /**
       * Flag indicating whether the plugin should offset for a keyboard bug in Chromium version < 144.
       * That bug incorrectly reports the bottom inset as larger than `0px` if the keyboard is shown.
       * This may, for example, result in larger padding than necessary.
       * When set to `true`, the webview will be enlarged by exactly the systembars bottom insets.
       * This will make that part of the webview be shown behind the keyboard. And thus hide any excess padding.
       *
       * Alternatively, you can prevent using the `env(safe-area-inset-bottom)` variable when the keyboard is visible.
       * That is what Ionic components already do out of the box.
       * See: https://github.com/ionic-team/ionic-framework/blob/0a02e0f8cffe7b307a9f0c7da13ff6c048335e80/core/src/components/footer/footer.tsx#L137
       *
       * Chromium bug tracker: https://issues.chromium.org/issues/457682720
       *
       * @example true
       * @default false
       */
      offsetForKeyboardInsetBug?: boolean;
    };
  }
}

export enum SystemBarsStyle {
  /**
   * Light system bar content on a dark background.
   */
  Dark = 'DARK',

  /**
   * For dark system bar content on a light background.
   */
  Light = 'LIGHT',

  /**
   * The style is based on the device appearance or the underlying content.
   * If the device is using dark mode, the system bars content will be light.
   * If the device is using light mode, the system bars content will be dark.
   */
  Default = 'DEFAULT',
}

export enum SystemBarsType {
  /**
   * The top status bar on both Android and iOS.
   */
  StatusBar = 'STATUS_BAR',
  /**
   * The navigation bar on both Android and iOS.
   *
   * On iOS this is the "home indicator".
   *
   * On Android this is either the "navigation bar" or the "gesture bar".
   */
  NavigationBar = 'NAVIGATION_BAR',
}

export interface SystemBarsStyleOptions {
  /**
   * Style of the content of the system bars.
   *
   * @default 'DEFAULT'
   */
  style: SystemBarsStyle;

  /**
   * The system bar to which to apply the style.
   *
   * Providing `null` means it will be applied to both system bars.
   *
   * On iOS the home indicator cannot be styled. It will always automatically be applied a color by iOS out of the box.
   *
   * @default null
   * @example SystemBarsType.StatusBar
   */
  type?: SystemBarsType;
}

export interface SystemBarsVisibilityOptions {
  /**
   * The system bar to hide or show.
   *
   * Providing `null` means it will toggle both system bars.
   *
   * @default null
   * @example SystemBarType.StatusBar
   */
  type?: SystemBarsType;
}

export interface SafeAreaPlugin {
  setSystemBarsStyle(options: SystemBarsStyleOptions): Promise<void>;
  showSystemBars(options: SystemBarsVisibilityOptions): Promise<void>;
  hideSystemBars(options: SystemBarsVisibilityOptions): Promise<void>;
}
