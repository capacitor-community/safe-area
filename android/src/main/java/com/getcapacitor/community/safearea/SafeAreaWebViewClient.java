package com.getcapacitor.community.safearea;

import android.webkit.WebView;

import com.getcapacitor.Bridge;
import com.getcapacitor.BridgeWebViewClient;
import com.getcapacitor.PluginHandle;

public class SafeAreaWebViewClient extends BridgeWebViewClient {
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

    private final Bridge bridge;

    public SafeAreaWebViewClient(Bridge bridge) {
        super(bridge);
        this.bridge = bridge;
    }

    @Override
    public void onPageCommitVisible(WebView view, String url) {
        SafeAreaPlugin safeAreaPlugin = getSafeAreaInstance();

        if (safeAreaPlugin != null) {
            bridge.getWebView().evaluateJavascript(viewportMetaJSFunction, (res) -> {
                safeAreaPlugin.hasMetaViewportCover = res.equals("true");

                // Request new execution tree of `setOnApplyWindowInsetsListener`
                bridge.getWebView().requestApplyInsets();
            });
        }

        super.onPageCommitVisible(view, url);
    }

    private SafeAreaPlugin getSafeAreaInstance() {
        if (bridge != null && bridge.getWebView() != null) {
            PluginHandle handle = bridge.getPlugin("SafeArea");
            if (handle == null) {
                return null;
            }
            return (SafeAreaPlugin) handle.getInstance();
        }
        return null;
    }
}
