package com.getcapacitor.community.safearea;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
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
    private boolean offsetForKeyboardInsetBug = false;

    @Override
    public void load() {
        super.load();

        webViewMajorVersion = getWebViewMajorVersion();

        offsetForKeyboardInsetBug = getConfig().getConfigJSON().optBoolean("offsetForKeyboardInsetBug", false);

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
        // View view = shouldPassthroughInsets ? getActivity().getWindow().getDecorView() : bridge.getWebView();
        View view = getActivity().getWindow().getDecorView();

        View parent = (ViewGroup) getBridge().getWebView().getParent();

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            boolean shouldPassthroughInsets = webViewMajorVersion >= WEBVIEW_VERSION_WITH_SAFE_AREA_CORE_FIX && hasMetaViewportCover;

            if (shouldPassthroughInsets) {
                // In case `shouldPassthroughInsets` was `false` before,
                // we need to reset the view margins of `parent`
                setViewMargin(parent, 0, 0, 0, 0);

                // We need to correct for a possible shown IME
                // NOTE: using `getBridge().getWebView()` instead of `parent` here, to prevent infinite loops
                setViewMargin(getBridge().getWebView(), 0, 0, getBottomMargin(windowInsets, parent), 0);

                return windowInsets;
            }

            // Optionally, we could allow users to workaround the older webview issues by passing through the insets as custom vars
            // if (hasFixedWebView) {
            //     Insets safeArea = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            //     injectSafeAreaCSS(safeArea.top, safeArea.right, safeArea.bottom, safeArea.left);
            //     return windowInsets;
            // }

            Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
            boolean keyboardVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime());

            setViewMargin(parent, systemBarsInsets.top, systemBarsInsets.right, keyboardVisible ? imeInsets.bottom : systemBarsInsets.bottom, systemBarsInsets.left);

            return WindowInsetsCompat.CONSUMED;
        });
    }

    private void setViewMargin(View view, @Nullable Integer top, @Nullable Integer right, @Nullable Integer bottom, @Nullable Integer left) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (top != null) {
            mlp.topMargin = top;
        }
        if (right != null) {
            mlp.rightMargin = right;
        }
        if (bottom != null) {
            mlp.bottomMargin = bottom;
        }
        if (left != null) {
            mlp.leftMargin = left;
        }
        view.setLayoutParams(mlp);
    }

    private int getBottomMargin(WindowInsetsCompat windowInsets, View parent) {
        Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
        Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
        boolean keyboardVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime());

        if (!keyboardVisible) {
            return 0;
        }

        // Shrink the view so that it's shown in the area available right above the IME
        int bottomMargin = imeInsets.bottom;

        if (offsetForKeyboardInsetBug && systemBarsInsets.bottom > 0 && webViewMajorVersion < WEBVIEW_VERSION_WITH_SAFE_AREA_KEYBOARD_FIX) {
            // https://issues.chromium.org/issues/457682720
            // this is a workaround to push the webview behind the keyboard for webview versions that have a bug
            // that causes the bottom inset to be incorrect if the IME is visible
            bottomMargin = imeInsets.bottom - systemBarsInsets.bottom;
        }

        if (bottomMargin > parent.getHeight()) {
            // This is needed to workaround a bug that when an IME is visible before app start,
            // setting the MarginLayoutParams causes issues.
            bottomMargin = parent.getHeight();
        }

        if (bottomMargin < 0) {
            bottomMargin = 0;
        }

        return bottomMargin;
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

        getBridge().executeOnMainThread(() -> {
            setSystemBarsStyle(getActivity(), systemBarsStyle, systemBarsType);
            call.resolve();
        });
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
