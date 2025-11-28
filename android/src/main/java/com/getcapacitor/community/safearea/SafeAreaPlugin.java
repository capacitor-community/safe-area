package com.getcapacitor.community.safearea;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.webkit.WebViewCompat;

import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CapacitorPlugin(name = "SafeArea")
public class SafeAreaPlugin extends Plugin {
    private static final String viewportMetaJSFunction = """
            function capacitorSafeAreaCheckMetaViewport() {
                const meta = document.querySelectorAll("meta[name=viewport]");
                if (meta.length == 0) {
                    return false;
                }
                // get the last found meta viewport tag
                const metaContent = meta[meta.length - 1].content;
                return metaContent.includes("viewport-fit=cover");
            }
            
            capacitorSafeAreaCheckMetaViewport();
            """;

    private int webViewMajorVersion;

    // https://issues.chromium.org/issues/40699457
    private static final int WEBVIEW_VERSION_WITH_SAFE_AREA_CORE_FIX = 140;

    // https://issues.chromium.org/issues/457682720
    private static final int WEBVIEW_VERSION_WITH_SAFE_AREA_KEYBOARD_FIX = 144;

    private boolean hasMetaViewportCover = true;

    // Use an initial value of `null`, so this plugin doesn't override any existing behavior by default
    private SystemBarsStyle statusBarStyle = null;

    // Use an initial value of `null`, so this plugin doesn't override any existing behavior by default
    private SystemBarsStyle navigationBarStyle = null;

    @Override
    public void load() {
        super.load();

        webViewMajorVersion = getWebViewMajorVersion();

        String statusBarStyleString = getConfig().getConfigJSON().optString("statusBarStyle");
        if (!statusBarStyleString.isBlank()) {
            statusBarStyle = getSystemBarsStyleFromString(statusBarStyleString);
        }

        String navigationBarStyleString = getConfig().getConfigJSON().optString("navigationBarStyle");
        if (!navigationBarStyleString.isBlank()) {
            navigationBarStyle = getSystemBarsStyleFromString(navigationBarStyleString);
        }

        updateSystemBarsStyle();

        this.bridge.getWebView().evaluateJavascript(viewportMetaJSFunction, (res) -> {
            // @TODO: this doesn't work yet.
            // Seems like a bug in Chromium that the safe area insets do not get updated upon changing the return inside `setOnApplyWindowInsetsListener`
            // @TODO: ideally this should be rechecked in `onPageFinished` for example. Because some webpages might support edge-to-edge whereas others do not.
            // hasMetaViewportCover = res.equals("true");

            // Request new execution tree of `setOnApplyWindowInsetsListener`
            this.bridge.getWebView().requestApplyInsets();
        });

        setupSafeAreaInsets();
    }

    private int getWebViewMajorVersion() {
        PackageInfo packageInfo = WebViewCompat.getCurrentWebViewPackage(bridge.getContext());
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(packageInfo.versionName);

        if (!matcher.find()) {
            return 0;
        }

        String majorVersionStr = matcher.group(0);
        int majorVersion = Integer.parseInt(majorVersionStr);

        return majorVersion;
    }

    private void setupSafeAreaInsets() {
        View view = getActivity().getWindow().getDecorView();

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            boolean shouldPassthroughInsets = webViewMajorVersion >= WEBVIEW_VERSION_WITH_SAFE_AREA_CORE_FIX && hasMetaViewportCover;

            Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
            boolean keyboardVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime());

            if (shouldPassthroughInsets) {
                // We need to correct for a possible shown IME
                v.setPadding(0, 0, 0, keyboardVisible ? imeInsets.bottom : 0);

                return new WindowInsetsCompat.Builder(windowInsets).setInsets(
                        WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                        Insets.of(
                                systemBarsInsets.left,
                                systemBarsInsets.top,
                                systemBarsInsets.right,
                                getBottomInset(systemBarsInsets, keyboardVisible)
                        )
                ).build();
            }

            // Optionally, we could allow users to workaround the older webview issues by passing through the insets as custom vars
            // if (hasFixedWebView) {
            //     Insets safeArea = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            //     injectSafeAreaCSS(safeArea.top, safeArea.right, safeArea.bottom, safeArea.left);
            //     return windowInsets;
            // }

            // We need to correct for a possible shown IME
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, keyboardVisible ? imeInsets.bottom : systemBarsInsets.bottom);

            // Returning `WindowInsetsCompat.CONSUMED` breaks recalculation of safe area insets
            // So we have to explicitly set insets to `0`
            // See: https://issues.chromium.org/issues/461332423
            return new WindowInsetsCompat.Builder(windowInsets).setInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                    Insets.of(0, 0, 0, 0)
            ).build();
        });
    }

    private int getBottomInset(Insets systemBarsInsets, boolean keyboardVisible) {
        if (webViewMajorVersion < WEBVIEW_VERSION_WITH_SAFE_AREA_KEYBOARD_FIX) {
            // This is a workaround for webview versions that have a bug
            // that causes the bottom inset to be incorrect if the IME is visible
            // See: https://issues.chromium.org/issues/457682720

            if (keyboardVisible) {
                return 0;
            }
        }

        return systemBarsInsets.bottom;
    }

    public enum SystemBarsStyle {
        DARK("DARK"),
        LIGHT("LIGHT"),
        DEFAULT("DEFAULT");

        public final String value;

        SystemBarsStyle(String value) {
            this.value = value;
        }
    }

    private @NonNull SystemBarsStyle getSystemBarsStyleFromString(@Nullable String value) {
        if (value != null) {
            try {
                return SystemBarsStyle.valueOf(value.toUpperCase(Locale.US));
            } catch (IllegalArgumentException error) {
                // invalid value
            }
        }

        return SystemBarsStyle.DEFAULT;
    }

    public enum SystemBarsType {
        STATUS_BAR("STATUS_BAR"),
        NAVIGATION_BAR("NAVIGATION_BAR");

        public final String value;

        SystemBarsType(String value) {
            this.value = value;
        }
    }

    private @Nullable SystemBarsType getSystemBarsTypeFromString(@Nullable String value) {
        if (value != null) {
            try {
                return SystemBarsType.valueOf(value.toUpperCase(Locale.US));
            } catch (IllegalArgumentException error) {
                // invalid value
            }
        }

        return null;
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void setSystemBarsStyle(final PluginCall call) {
        String style = call.getString("style");
        String type = call.getString("type");

        SystemBarsStyle systemBarsStyle = getSystemBarsStyleFromString(style);
        SystemBarsType systemBarsType = getSystemBarsTypeFromString(type);

        if (systemBarsType == null || systemBarsType == SystemBarsType.STATUS_BAR) {
            statusBarStyle = systemBarsStyle;
        }

        if (systemBarsType == null || systemBarsType == SystemBarsType.NAVIGATION_BAR) {
            navigationBarStyle = systemBarsStyle;
        }

        getBridge().executeOnMainThread(() -> {
            updateSystemBarsStyle();
            call.resolve();
        });
    }

    @Override
    protected void handleOnConfigurationChanged(Configuration newConfig) {
        super.handleOnConfigurationChanged(newConfig);
        getBridge().executeOnMainThread(() -> {
            updateSystemBarsStyle();
        });
    }

    private void updateSystemBarsStyle() {
        if (statusBarStyle != null) {
            setSystemBarsStyle(getActivity(), statusBarStyle, SystemBarsType.STATUS_BAR);
        }
        if (navigationBarStyle != null) {
            setSystemBarsStyle(getActivity(), navigationBarStyle, SystemBarsType.NAVIGATION_BAR);
        }
    }

    public static void setSystemBarsStyle(Activity activity, SystemBarsStyle style) {
        setSystemBarsStyle(activity, style, null);
    }

    public static void setSystemBarsStyle(Activity activity, SystemBarsStyle style, @Nullable SystemBarsType type) {
        if (style == SystemBarsStyle.DEFAULT) {
            style = getStyleForTheme(activity);
        }

        Window window = activity.getWindow();
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());
        if (type == null || type == SystemBarsType.STATUS_BAR) {
            windowInsetsControllerCompat.setAppearanceLightStatusBars(style != SystemBarsStyle.DARK);
        }

        if (type == null || type == SystemBarsType.NAVIGATION_BAR) {
            windowInsetsControllerCompat.setAppearanceLightNavigationBars(style != SystemBarsStyle.DARK);
        }

        if (style == SystemBarsStyle.DARK) {
            window.getDecorView().setBackgroundColor(Color.BLACK);
        } else {
            window.getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    private static SystemBarsStyle getStyleForTheme(Activity activity) {
        int currentNightMode = activity.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            return SystemBarsStyle.LIGHT;
        }
        return SystemBarsStyle.DARK;
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void showSystemBars(final PluginCall call) {
        String type = call.getString("type");
        SystemBarsType systemBarsType = getSystemBarsTypeFromString(type);

        getBridge().executeOnMainThread(() -> {
            setSystemBarsHidden(false, systemBarsType);
            call.resolve();
        });
    }

    @PluginMethod(returnType = PluginMethod.RETURN_NONE)
    public void hideSystemBars(final PluginCall call) {
        String type = call.getString("type");
        SystemBarsType systemBarsType = getSystemBarsTypeFromString(type);

        getBridge().executeOnMainThread(() -> {
            setSystemBarsHidden(true, systemBarsType);
            call.resolve();
        });
    }

    private void setSystemBarsHidden(Boolean hidden, @Nullable SystemBarsType type) {
        Window window = getActivity().getWindow();
        WindowInsetsControllerCompat windowInsetsControllerCompat = WindowCompat.getInsetsController(window, window.getDecorView());

        if (hidden) {
            if (type == null || type == SystemBarsType.STATUS_BAR) {
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.statusBars());
            }
            if (type == null || type == SystemBarsType.NAVIGATION_BAR) {
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
            }
            return;
        }

        if (type == null || type == SystemBarsType.STATUS_BAR) {
            windowInsetsControllerCompat.show(WindowInsetsCompat.Type.systemBars());
        }
        if (type == null || type == SystemBarsType.NAVIGATION_BAR) {
            windowInsetsControllerCompat.show(WindowInsetsCompat.Type.navigationBars());
        }
    }
}
