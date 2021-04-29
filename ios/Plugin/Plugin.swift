import Foundation
import Capacitor

func makeSafeArea(top: Int, bottom: Int, right: Int, left: Int) -> [String :[String: Int]] {
    return [
        "insets": [
            "top": top,
            "bottom": bottom,
            "right": right,
            "left": left
        ]
    ];
}

func getStatusBarFrame() -> CGRect {
    if #available(iOS 13.0, *) {
        let keyWindow = UIApplication.shared.windows.filter { window in window.isKeyWindow }.first
        return keyWindow?.windowScene?.statusBarManager?.statusBarFrame ?? CGRect.zero
    } else {
        return UIApplication.shared.statusBarFrame
    }
}

let EVENT_ON_INSETS_CHANGED = "safeAreaPluginsInsetChange"

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SafeAreaPlugin)
public class SafeAreaPlugin: CAPPlugin {
    private var safeArea = makeSafeArea(top: 0, bottom: 0, right: 0, left: 0)
    
    override public func load() {
        if #available(iOS 13.0, *) {
            // TODO: Figure out the replacement for status bar frame notifications
        } else {
            NotificationCenter.default.addObserver(self, selector: #selector(self.onWillChangeStatusBarFrameNotification),
                                                   name: UIApplication.willChangeStatusBarFrameNotification, object: nil)
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func refresh(_ call: CAPPluginCall) {
        let frame = getStatusBarFrame()
        self.changeSafeArea(top: Int(frame.size.height));
        call.success()
    }

    @objc func getSafeAreaInsets(_ call: CAPPluginCall) {
        call.success(self.safeArea)
    }
    
    @objc func onWillChangeStatusBarFrameNotification(newFrame: CGRect) {
        self.changeSafeArea(top: Int(newFrame.height))
    }
    
    func changeSafeArea(top: Int) {
        self.safeArea = makeSafeArea(top: top, bottom: 0, right: 0, left: 0)
        self.notifyListeners(EVENT_ON_INSETS_CHANGED, data: self.safeArea)
    }
}
