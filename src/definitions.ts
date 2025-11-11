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

export interface SafeAreaPlugin {}
