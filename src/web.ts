import { WebPlugin } from '@capacitor/core';
import { SafeAreaInsets, SafeArea, SafeAreaInsetsChangeEventName, SafeAreaInsetsResult } from './definitions';

export class SafeAreaPluginWeb extends WebPlugin implements SafeArea {
	constructor() {
		super({
		name: 'SafeAreaPlugin',
		platforms: ['web'],
		});
	}

	/**
	 * Call this whenever you want the EventOnInsetsChanged to be fired manually.
	 */
	async refresh(): Promise<void> {
		const dummy: SafeAreaInsets = {
			top: 0,
			bottom: 0,
			right: 0,
			left: 0
		};

		this.notifyListeners(SafeAreaInsetsChangeEventName, {
			insets: dummy
		});
	}

	/**
	 * Gets the current SafeAreaInsets.
	 */
	getSafeAreaInsets(): Promise<SafeAreaInsetsResult> {
		const dummy: SafeAreaInsets = {
			top: 0,
			bottom: 0,
			right: 0,
			left: 0,
		};

		return new Promise<SafeAreaInsetsResult>((resolve) => {
			resolve({ insets: dummy});
		});
	}
}

const SafeAreaPlugin = new SafeAreaPluginWeb();

export { SafeAreaPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(SafeAreaPlugin);
