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

func getStatusBarFrame(controller: UIViewController) -> CGRect {
    if #available(iOS 13.0, *) {
        let keyWindow = UIApplication.shared.windows
            .filter { window in window.rootViewController == controller }
            .first
        return keyWindow?.windowScene?.statusBarManager?.statusBarFrame ?? CGRect.zero
    } else {
        return UIApplication.shared.statusBarFrame
    }
}

@objc
public class SizeWithCoordinator: NSObject {
    public var size: CGSize;
    public var coordinator: UIViewControllerTransitionCoordinator;
    
    init(size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        self.size = size;
        self.coordinator = coordinator;
    }
}

let EVENT_ON_INSETS_CHANGED = "safeAreaPluginsInsetChange"

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SafeAreaPlugin)
public class SafeAreaPlugin: CAPPlugin {
    public static let ViewWillTransitionToSizeWithCoordinatorNotification = NSNotification.Name(rawValue: "SafeAreaPlugin.ViewWillTransitionToSizeWithCoordinator");
    
    private var safeArea = makeSafeArea(top: 0, bottom: 0, right: 0, left: 0)
    
    override public func load() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
            object: nil
        )
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onWillResignActive),
            name: UIApplication.willResignActiveNotification,
            object: nil
        )
        
        if #available(iOS 13.0, *) {
            NotificationCenter.default.addObserver(
                self,
                selector: #selector(self.onViewWillTransitionTo),
                name: SafeAreaPlugin.ViewWillTransitionToSizeWithCoordinatorNotification,
                object: nil
            )
        } else {
            NotificationCenter.default.addObserver(
                self,
                selector: #selector(self.onWillChangeStatusBarFrameNotification),
                name: UIApplication.willChangeStatusBarFrameNotification,
                object: nil
            )
        }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    @objc func refresh(_ call: CAPPluginCall) {
        let frame = getStatusBarFrame(controller: self.bridge.viewController)
        self.changeSafeArea(top: Int(frame.size.height));
        call.success()
    }

    @objc func getSafeAreaInsets(_ call: CAPPluginCall) {
        call.success(self.safeArea)
    }
    
    @objc func onDidBecomeActive() {
        let frame = getStatusBarFrame(controller: self.bridge.viewController)
        self.changeSafeArea(top: Int(frame.size.height))
    }
    
    @objc func onWillResignActive() {}
    
    @objc func onWillChangeStatusBarFrameNotification(newFrame: CGRect) {
        self.changeSafeArea(top: Int(newFrame.height))
    }
    
    @objc func onViewWillTransitionTo(sizeWithCoordinator: SizeWithCoordinator) {
        self.changeSafeArea(top: Int(sizeWithCoordinator.size.height))
    }
    
    func changeSafeArea(top: Int) {
        self.safeArea = makeSafeArea(top: top, bottom: 0, right: 0, left: 0)
        self.notifyListeners(EVENT_ON_INSETS_CHANGED, data: self.safeArea)
    }
}

extension CAPBridgeViewController {
    public override func viewWillTransition(to size: CGSize, with coordinator: UIViewControllerTransitionCoordinator) {
        super.viewWillTransition(to: size, with: coordinator)
        if #available(iOS 13.0, *) {
            NotificationCenter.default.post(
                name: SafeAreaPlugin.ViewWillTransitionToSizeWithCoordinatorNotification,
                object: SizeWithCoordinator(size: size, with: coordinator)
            )
        }
    }
}
