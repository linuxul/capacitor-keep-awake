import XCTest
import Capacitor
@testable import KeepAwakePlugin

final class KeepAwakeTests: XCTestCase {
    func testMethodsAreRegisteredAsPromises() {
        let methods = KeepAwakePlugin().pluginMethods
        XCTAssertEqual(methods.map(\.name), ["keepAwake", "allowSleep", "isSupported", "isKeptAwake"])
        XCTAssertTrue(methods.allSatisfy { $0.returnType == .promise })
    }

    func testIsSupportedResolvesTrue() throws {
        var resolved: PluginCallResultData?
        let call = CAPPluginCall(callbackId: "test", methodName: "isSupported", options: [:], success: { result, _ in
            resolved = result.data
        }, error: { _ in
            XCTFail("isSupported never rejects")
        })

        KeepAwakePlugin().isSupported(call)

        XCTAssertEqual(try XCTUnwrap(resolved)["isSupported"] as? Bool, true)
    }
}
