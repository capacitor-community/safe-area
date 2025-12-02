/// <reference types="@capacitor/cli" />

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    SafeArea?: {
      /**
       * Indicates which style to apply to the status bar initially.
       *
       * @default null
       */
      statusBarStyle?: SystemBarsStyle;

      /**
       * Indicates which style to apply to the navigation bar initially.
       *
       * On iOS the home indicator cannot be styled. It will always automatically be applied a color by iOS out of the box.
       *
       * @default null
       */
      navigationBarStyle?: SystemBarsStyle;

      /**
       * @deprecated Setting this value is not necessary anymore, as it now works out of the box.
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
