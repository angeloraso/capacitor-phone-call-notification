import Foundation

@objc public class PhoneCallNotification: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
