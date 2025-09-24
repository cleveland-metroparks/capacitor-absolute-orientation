import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AbsoluteOrientationPlugin)
public class AbsoluteOrientationPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "AbsoluteOrientationPlugin"
    public let jsName = "AbsoluteOrientation"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = AbsoluteOrientation()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
