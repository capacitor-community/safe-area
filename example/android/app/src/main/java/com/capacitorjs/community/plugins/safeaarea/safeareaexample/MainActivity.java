package com.capacitorjs.community.plugins.safeaarea.safeareaexample;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.Plugin;
import com.capacitorjs.community.plugins.safearea.SafeAreaPlugin;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	// Initializes the Bridge
	this.init(savedInstanceState, new ArrayList<Class<? extends Plugin>>() {{
	  // Additional plugins you've installed go here
	  // Ex: add(TotallyAwesomePlugin.class);
	  add(SafeAreaPlugin.class);
	}});
  }

  @Override
  public void onResume() {
	super.onResume();

	if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return;

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
}
