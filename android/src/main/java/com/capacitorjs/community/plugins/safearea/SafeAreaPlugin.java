package com.capacitorjs.community.plugins.safearea;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.WindowInsets;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin
public class SafeAreaPlugin extends Plugin implements SensorEventListener {
	private static final String KEY_INSET = "insets";
	private static final String EVENT_ON_INSETS_CHANGED = "safeAreaPluginsInsetChange";
	private SafeAreaInsets safeAreaInsets = new SafeAreaInsets();

	@PluginMethod
	public void refresh(PluginCall call) {
		this.doNotify();
		call.success();
	}

	@PluginMethod
	public void getSafeAreaInsets(PluginCall call) {
		JSObject ret = new JSObject();

		ret.put(SafeAreaPlugin.KEY_INSET, this.safeAreaInsets.toJSON());

		call.success(ret);
	}

	@Override
	protected void handleOnResume() {
		super.handleOnResume();

		SensorManager sm = (SensorManager)this.getBridge().getActivity().getSystemService(Context.SENSOR_SERVICE);
		sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void handleOnPause() {
		super.handleOnPause();

		SensorManager sm = (SensorManager)this.getBridge().getActivity().getSystemService(Context.SENSOR_SERVICE);
		sm.unregisterListener(this);
	}

	protected int getSafeArea(SafeAreaInsets cache) {
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
			Log.i(SafeAreaPlugin.class.toString(), String.format("Requires at least %d+", Build.VERSION_CODES.P));

			cache.clear();

			return -1;
		}

		WindowInsets windowInsets = this.getBridge().getActivity().getWindow().getDecorView().getRootWindowInsets();

		if(windowInsets == null) {
			Log.i(SafeAreaPlugin.class.toString(), "WindowInsets is not available.");

			cache.clear();

			return -1;
		}

		DisplayCutout displayCutout = windowInsets.getDisplayCutout();

		if(displayCutout == null) {
			Log.i(SafeAreaPlugin.class.toString(), "DisplayCutout is not available.");

			cache.clear();

			return -1;
		}

		float density = this.getBridge().getActivity().getResources().getDisplayMetrics().density;
		boolean hasChanged = false;

		int top = Math.round(displayCutout.getSafeInsetTop() / density);
		int bottom = Math.round(displayCutout.getSafeInsetBottom() / density);
		int left = Math.round(displayCutout.getSafeInsetLeft() / density);
		int right = Math.round(displayCutout.getSafeInsetRight() / density);

		if(cache.top() != top) {
			cache.top(top);
			hasChanged |= true;
		}

		if(cache.bottom() != bottom) {
			cache.bottom(bottom);
			hasChanged |= true;
		}

		if(cache.right() != right) {
			cache.right(right);
			hasChanged |= true;
		}

		if(cache.left() != left) {
			cache.left(left);
			hasChanged |= true;
		}

		return hasChanged ? 1 : 0;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int result = this.getSafeArea(this.safeAreaInsets);

		switch (result) {
			case SafeAreaInsetResult.ERROR:
			case SafeAreaInsetResult.NO_CHANGE: return;
		}

		this.doNotify();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) { /* Do Nothing... */ }

	protected void doNotify() {
		this.notifyListeners(SafeAreaPlugin.EVENT_ON_INSETS_CHANGED, this.safeAreaInsets.toJSON());
	}

	public static class SafeAreaInsetResult {
		public static final int ERROR = -1;
		public static final int NO_CHANGE = 0;
		public static final int CHANGE = 1;
	}
}
