<p align="center"><br><img src="https://user-images.githubusercontent.com/236501/85893648-1c92e880-b7a8-11ea-926d-95355b8175c7.png" width="128" height="128" /></p>
<h3 align="center">Safe Area</h3>
<p align="center"><strong><code>@capacitor-community/safe-area</code></strong></p>
<p align="center">
  A plugin to expose the safe area insets from the native iOS/Android device to your web project.
</p>

<p align="center">
  <img src="https://img.shields.io/maintenance/yes/2020?style=flat-square" />
  <a href="https://github.com/capacitor-community/safe-area/actions?query=workflow%3A%22CI%22"><img src="https://img.shields.io/github/workflow/status/capacitor-community/safe-area/CI?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/l/@capacitor-community/safe-area?style=flat-square" /></a>
<br>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/dw/@capacitor-community/safe-area?style=flat-square" /></a>
  <a href="https://www.npmjs.com/package/@capacitor-community/safe-area"><img src="https://img.shields.io/npm/v/@capacitor-community/safe-area?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
<a href="#contributors-"><img src="https://img.shields.io/badge/all%20contributors-0-orange?style=flat-square" /></a>
<!-- ALL-CONTRIBUTORS-BADGE:END -->
</p>

## Maintainers

| Maintainer | GitHub | Social |
| -----------| -------| -------|
| Kevin Pacheco | [PolymorphiK](https://github.com/PolymorphiK) | [@k_pacheco10](https://twitter.com/k_pacheco10) |

## Installation

Soon(TM)

## Configuration

iOS: Soon(TM)

For Android, register plugin in your main activity.

```java
import com.getcapacitor.community.safearea.SafeAreaPlugin;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
  
	this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
	  add(SafeAreaPlugin.class);
	}});
  }
}
```
Here is a bonus tip, to get full screen mode use this in your main activity. Requires Android **28+**

```java
@Override
public void onResume() {
super.onResume();

// Requires API 28+
this.getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;

View decorView = this.getWindow().getDecorView();

decorView.setSystemUiVisibility(
		View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
				// Set the content to appear under the system bars so that the
				// content doesn't resize when the system bars hide and show.
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				// Hide the nav bar and status bar
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN);
}
  ```
To change the Android build version, change the **minSdkVersion** to at least 28 in the **variables.gradle** file. <br />
For more information please see Android's [immersive](https://developer.android.com/training/system-ui/immersive), and [short edges](https://developer.android.com/reference/android/view/WindowManager.LayoutParams#LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES) documentation.

## Usage
Register the plugin via the entry file of your project.

```javascript
// Register Capacitor Plugin...
import '@capacitor-community/safe-area';

```

It is **strongly** recomended that you use the **SafeAreaController** as it makes it super easy to use the plugin :)

```javascript
import { SafeAreaController } from '@capacitor-community/safe-area';

// Initialize the controller.
SafeAreaController.load();

// Gets the insets object
// Shape
/*
 {
     top: number,
     bottom: number,
     right: number,
     left: number,
 }
 */
SafeAreaController.getInsets();

// Use this to listen for changes in the insets.
// i.e. when the device is rotated
SafeAreaController.addListener((insets) => {
    
});

// Use this to force the plugin to invoke the event
// Example, after you add a listener perhaps you invoke refresh
// to get the most up-to-date inset values.
SafeAreaController.refresh();

// Uninitialize the controller when you don't need it anymore.
SafeAreaController.unload();
```

Once the **SafeAreaController** has been loaded, it will inject CSS variables for you to use via your CSS.

```css
/* styling for every case Web, iOS, and/or Android */
.myContainer {
	paddingTop: max(1.5rem, val(--safe-area-inset-top)); 
	paddingLeft: max(1.5rem, val(--safe-area-inset-left));
	paddingRight: max(1.5rem, val(--safe-area-inset-right));
	paddingBottom: val(--safe-area-inset-bottom);
}

/* If you need Android specific stying */
.myContainerForAndroidOnlyForSomeReason {
	paddingTop: max(1.5rem, val(--android-safe-area-inset-top)); 
	paddingLeft: max(1.5rem, val(--android-safe-area-inset-left));
	paddingRight: max(1.5rem, val(--android-safe-area-inset-right));
	paddingBottom: val(--android-safe-area-inset-bottom);
}

/* If you need iOS specific styling */
.myContainerForIOSOnly {
	paddingTop: max(1.5rem, val(--ios-safe-area-inset-top)); 
	paddingLeft: max(1.5rem, val(--ios-safe-area-inset-left));
	paddingRight: max(1.5rem, val(--ios-safe-area-inset-right));
	paddingBottom: val(--ios-safe-area-inset-bottom);
}
```

This can also be used with the styles attribute in something like React.js for example.
```jsx
// This div will grow to cover the area of the cutout
// this would be at the very top.
<div
	style={{
		height: "var(--safe-area-inset-top)",
		backgroundColor: "#12005e"
	}}>
</div>
```

<details>
  <summary>React - SafeAreaInsetsProvider</summary>
  
  Here is a component that can be used by React.js developers. This handles everything for you via the SafeAreaController. There is a hook you can use as well called **useSafeAreaInsetsState** which will return a JSON object with top, bottom, right, and left number properties.
  
```javascript 
import * as React from 'react';
import { SafeAreaController } from '@capacitor-community/safe-area';

const StateContext = React.createContext();

export const useSafeAreaInsetsState = () => {
	const context = React.useContext(StateContext);

	if(context === undefined)
		throw new Error("Cannot use 'useSafeAreaInsetsState' outside of a SafeAreaInsetsProvider!");
	
	return context;
}

const SafeAreaInsetsProvider = ({children}) => {
	const [state, setState] = React.useState({
		top: 0,
		bottom: 0,
		right: 0,
		left: 0
	});

	React.useState(() => {
		SafeAreaController.addListener((insets) => {
			setState(insets);
		});

		SafeAreaController.load();

		return () => {
			SafeAreaController.removeAllListeners();
			SafeAreaController.unload();
		}
	}, []);

	return (
		<StateContext.Provider value={state}>
			{children}
		</StateContext.Provider>
	)
};

export default SafeAreaInsetsProvider;
```

You can then use this provider ideally in the **index** file of your project.

```javascript
ReactDOM.render(
	<React.StrictMode>
		<SafeAreaInsetsProvider>
			<App />
		</SafeAreaInsetsProvider>
	</React.StrictMode>,
	document.getElementById('root')
);
```
</details>
