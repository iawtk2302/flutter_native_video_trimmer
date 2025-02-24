import Flutter

class ClearTrimVideoCacheHandler: BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        VideoManager.shared.clearCache()
        result(nil)
    }
}
