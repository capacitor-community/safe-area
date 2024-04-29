/// <reference types="@capacitor/cli" />

export interface Config {
  /**
   * Flag indicating that you are responsible for drawing the background color for the system bars.
   * If `false` it will fallback to the default colors for the system bars.
   *
   * @example true
   * @default true
   */
  customColorsForSystemBars?: boolean;
  /**
   * Specifies the background color of the status bar.
   * Should be in the format `#RRGGBB` or `#AARRGGBB`.
   * Will only have effect if `customColorsForSystemBars` is set to `true`.
   *
   * @example '#000000'
   * @default '#000000'
   */
  statusBarColor?: string;
  /**
   * Specifies the color of the content (i.e. icon color) in the status bar.
   *
   * @example 'light'
   * @default 'light'
   */
  statusBarContent?: 'light' | 'dark';
  /**
   * Specifies the background color of the navigation bar.
   * Should be in the format `#RRGGBB` or `#AARRGGBB`.
   * Will only have effect if `customColorsForSystemBars` is set to `true`.
   *
   * @example '#000000'
   * @default '#000000'
   */
  navigationBarColor?: string;
  /**
   * Specifies the color of the content (i.e. icon color) in the navigation bar.
   *
   * @example 'light'
   * @default 'light'
   */
  navigationBarContent?: 'light' | 'dark';
  /**
   * Specifies the offset to be applied to the safe area insets.
   * This means that if the safe area top inset is 30px, and the offset specified is 10px,
   * the safe area top inset will be exposed as being 40px.
   * Usually you don't need this, but on iOS the safe area insets are mostly offset a little more
   * by itself already. So you might want to compensate for that on Android. It's totally up to you.
   * The offset will be applied if Edge-to-Edge mode is enabled only.
   *
   * @example 0
   * @default 0
   */
  offset?: number;
}

declare module '@capacitor/cli' {
  export interface PluginsConfig {
    /**
     * For ease of use and speed, these configuration values are available:
     */
    SafeArea?: {
      /**
       * Flag indicating whether of not the plugin should be enabled from startup.
       *
       * @example true
       * @default false
       */
      enabled?: boolean;
      /**
       * Flag indicating that you are responsible for drawing the background color for the system bars.
       * If `false` it will fallback to the default colors for the system bars.
       *
       * @example true
       * @default true
       */
      customColorsForSystemBars?: boolean;
      /**
       * Specifies the background color of the status bar.
       * Should be in the format `#RRGGBB` or `#AARRGGBB`.
       * Will only have effect if `customColorsForSystemBars` is set to `true`.
       *
       * @example '#000000'
       * @default '#000000'
       */
      statusBarColor?: string;
      /**
       * Specifies the color of the content (i.e. icon color) in the status bar.
       *
       * @example 'light'
       * @default 'light'
       */
      statusBarContent?: 'light' | 'dark';
      /**
       * Specifies the background color of the navigation bar.
       * Should be in the format `#RRGGBB` or `#AARRGGBB`.
       * Will only have effect if `customColorsForSystemBars` is set to `true`.
       *
       * @example '#000000'
       * @default '#000000'
       */
      navigationBarColor?: string;
      /**
       * Specifies the color of the content (i.e. icon color) in the navigation bar.
       *
       * @example 'light'
       * @default 'light'
       */
      navigationBarContent?: 'light' | 'dark';
      /**
       * Specifies the offset to be applied to the safe area insets.
       * This means that if the safe area top inset is 30px, and the offset specified is 10px,
       * the safe area top inset will be exposed as being 40px.
       * Usually you don't need this, but on iOS the safe area insets are mostly offset a little more
       * by itself already. So you might want to compensate for that on Android. It's totally up to you.
       * The offset will be applied if Edge-to-Edge mode is enabled only.
       *
       * @example 0
       * @default 0
       */
      offset?: number;
    };
  }
}

export interface SafeAreaPlugin {
  enable(options: { config: Config }): Promise<void>;

  disable(options: { config: Config }): Promise<void>;
}
