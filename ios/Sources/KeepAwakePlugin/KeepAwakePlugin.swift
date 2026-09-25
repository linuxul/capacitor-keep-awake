import Foundation
import UIKit
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(KeepAwakePlugin)
public class KeepAwakePlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "KeepAwakePlugin"
    public let jsName = "KeepAwake"
    public let pluginMethods: [CAPPluginMethod] = [
        .promise("keepAwake", KeepAwakePlugin.keepAwake),
        .promise("allowSleep", KeepAwakePlugin.allowSleep),
        .promise("isSupported", KeepAwakePlugin.isSupported),
        .async("isKeptAwake", KeepAwakePlugin.isKeptAwake)
    ]

    // keepAwake and allowSleep stay synchronous: the bridge queue runs them in the order of the calls and each hands
    // its UIKit work to the main queue in that order, so the last call wins. Async methods would not keep that order.

    func keepAwake(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            if !UIApplication.shared.isIdleTimerDisabled {
                UIApplication.shared.isIdleTimerDisabled = true
            }
            call.resolve()
        }
    }

    func allowSleep(_ call: CAPPluginCall) {
        DispatchQueue.main.async {
            if UIApplication.shared.isIdleTimerDisabled {
                UIApplication.shared.isIdleTimerDisabled = false
            }
            call.resolve()
        }
    }

    func isSupported(_ call: CAPPluginCall) {
        call.resolve([
            "isSupported": true
        ])
    }

    /// The idle timer is UIKit state: the method runs on the main actor. It starts after the UIKit work of the
    /// keepAwake and allowSleep calls made before it, which was queued on the main queue first.
    @MainActor
    func isKeptAwake(_ call: CAPPluginCall) async -> JSObject {
        return [
            "isKeptAwake": UIApplication.shared.isIdleTimerDisabled
        ]
    }
}
