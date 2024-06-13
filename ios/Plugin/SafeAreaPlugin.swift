import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(SafeAreaPlugin)
public class SafeAreaPlugin: CAPPlugin {
    @objc func enable(_ call: CAPPluginCall) {
        call.resolve()
    }

    @objc func disable(_ call: CAPPluginCall) {
        call.resolve()
    }
}
