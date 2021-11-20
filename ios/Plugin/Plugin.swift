import Foundation
import Capacitor

func makeSafeArea(_ insets: CGRect) -> [String :[String: Int]] {
    return [
        "insets": [
            "top": Int(insets.minY),
            "bottom": Int(insets.height),
            "right": Int(insets.width),
            "left": Int(insets.minX)
        ]
    ];
}

func getInsets(controller: UIViewController) -> CGRect {
    let keyWindow = UIApplication.shared.windows
        .filter { window in window.rootViewController == controller }
        .first
    
    if (keyWindow == nil) {
        return CGRect.zero;
    }
    
    let safeFrame = keyWindow!.safeAreaLayoutGuide.layoutFrame
    
    return CGRect(
        x: safeFrame.minX,
        y: safeFrame.minY,
        width: keyWindow!.frame.maxX - safeFrame.maxX,
        height: keyWindow!.frame.maxY - safeFrame.maxY
    )
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
    
    private var safeArea = makeSafeArea(CGRect.zero)
    
    override public func load() {
        NotificationCenter.default.addObserver(
            self,
            selector: #selector(self.onDidBecomeActive),
            name: UIApplication.didBecomeActiveNotification,
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
        let insets = getInsets(controller: self.bridge.viewController)
        self.changeSafeArea(insets);
        call.success()
    }

    @objc func getSafeAreaInsets(_ call: CAPPluginCall) {
        call.success(self.safeArea)
    }
    
    @objc func onDidBecomeActive() {
        let insets = getInsets(controller: self.bridge.viewController)
        self.changeSafeArea(insets)
    }
    
    @objc func onWillChangeStatusBarFrameNotification(newFrame: CGRect) {
        let insets = getInsets(controller: self.bridge.viewController)
        let insetsGuess = CGRect(
            x: insets.minX,
            y: newFrame.height,
            width: insets.width,
            height: insets.height
        )
        self.changeSafeArea(insetsGuess)
    }
    
    @objc func onViewWillTransitionTo(sizeWithCoordinator: SizeWithCoordinator) {
        let insets = getInsets(controller: self.bridge.viewController)
        let insetsGuess = CGRect(
            x: insets.minX,
            y: sizeWithCoordinator.size.height,
            width: insets.width,
            height: insets.height
        )
        self.changeSafeArea(insetsGuess)
    }
    
    func changeSafeArea(_ insets: CGRect) {
        self.safeArea = makeSafeArea(insets)
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
