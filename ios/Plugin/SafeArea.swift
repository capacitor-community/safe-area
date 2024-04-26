import Foundation

@objc public class SafeArea: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
