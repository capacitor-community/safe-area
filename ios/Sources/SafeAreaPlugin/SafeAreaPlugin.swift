import Foundation
import Capacitor

@objc(SafeAreaPlugin)
public class SafeAreaPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "SafeAreaPlugin"
    public let jsName = "SafeArea"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "setSystemBarsStyle", returnType: CAPPluginReturnNone),
        CAPPluginMethod(name: "showSystemBars", returnType: CAPPluginReturnNone),
        CAPPluginMethod(name: "hideSystemBars", returnType: CAPPluginReturnNone)
    ]

    public private(set) var hideHomeIndicator: Bool = false

    enum SystemBarsStyle: String {
        case dark = "DARK"
        case light = "LIGHT"
        case defaultStyle = "DEFAULT"
    }

    private func getSystemBarsStyleFromString(_ value: String?) -> SystemBarsStyle {
        let systemBarsStyle = SystemBarsStyle(rawValue: value?.uppercased() ?? "")
        return systemBarsStyle ?? SystemBarsStyle.defaultStyle
    }

    enum SystemBarsType: String {
        case statusBar = "STATUS_BAR"
        case navigationBar = "NAVIGATION_BAR"
    }

    private func getSystemBarsTypeFromString(_ value: String?) -> SystemBarsType? {
        return SystemBarsType(rawValue: value?.uppercased() ?? "")
    }

    @objc func setSystemBarsStyle(_ call: CAPPluginCall) {
        let style = call.getString("style")

        let systemBarsStyle = getSystemBarsStyleFromString(style)

        setSystemBarsStyle(style: systemBarsStyle)
        call.resolve()
    }

    func setSystemBarsStyle(style: SystemBarsStyle) {
        bridge?.statusBarStyle = switch style {
            case .dark:
                UIStatusBarStyle.lightContent
            case .light:
                UIStatusBarStyle.darkContent
            case .defaultStyle:
                UIStatusBarStyle.default
        }
    }

    @objc func showSystemBars(_ call: CAPPluginCall) {
        let type = call.getString("type")
        let systemBarsType = getSystemBarsTypeFromString(type)

        DispatchQueue.main.async {
            self.setSystemBarsHidden(hidden: false, type: systemBarsType)
            call.resolve()
        }
    }

    @objc func hideSystemBars(_ call: CAPPluginCall) {
        let type = call.getString("type")
        let systemBarsType = getSystemBarsTypeFromString(type)

        DispatchQueue.main.async {
            self.setSystemBarsHidden(hidden: true, type: systemBarsType)
            call.resolve()
        }
    }

    private func setSystemBarsHidden(hidden: Bool, type: SystemBarsType?) {
        if hidden {
            if type == nil || type == SystemBarsType.statusBar {
                bridge?.statusBarVisible = false
            }
            if type == nil || type == SystemBarsType.navigationBar {
                hideHomeIndicator = true
                bridge?.viewController?.setNeedsUpdateOfHomeIndicatorAutoHidden()
            }
            return
        }

        if type == nil || type == SystemBarsType.statusBar {
            bridge?.statusBarVisible = true
        }
        if type == nil || type == SystemBarsType.navigationBar {
            hideHomeIndicator = false
            bridge?.viewController?.setNeedsUpdateOfHomeIndicatorAutoHidden()
        }
    }
}

extension CAPBridgeViewController {
    override public var prefersHomeIndicatorAutoHidden: Bool {
        if let systemBarPlugin = self.bridge?.plugin(withName: "SafeArea") as? SafeAreaPlugin {
            return systemBarPlugin.hideHomeIndicator
        }

        return false
    }
}
