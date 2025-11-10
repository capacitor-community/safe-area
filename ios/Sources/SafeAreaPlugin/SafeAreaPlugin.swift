import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SafeAreaPlugin)
public class SafeAreaPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "SafeAreaPlugin"
    public let jsName = "SafeArea"
    public let pluginMethods: [CAPPluginMethod] = []
}
