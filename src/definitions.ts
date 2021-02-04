import { PluginListenerHandle } from "@capacitor/core";

declare module '@capacitor/core' {
  interface PluginRegistry {
    SafeAreaPlugin: SafeArea;
  }
}

export interface SafeArea {
  refresh(): Promise<void>;
  getSafeAreaInsets(): Promise<SafeAreaInsetsResult>;
  addListener(eventName: "safeAreaPluginsInsetChange", listener: SafeAreaInsetsChangedCallback): PluginListenerHandle;
}

export interface SafeAreaInsets {
	top: number;
	bottom: number;
	right: number;
	left: number;
	[key: string]: number;
};

export interface SafeAreaInsetsResult {
	insets: SafeAreaInsets;
}
export type SafeAreaInsetsChangedCallback = (insets: SafeAreaInsets) => void;

export const SafeAreaInsetsChangeEventName = "safeAreaPluginsInsetChange";