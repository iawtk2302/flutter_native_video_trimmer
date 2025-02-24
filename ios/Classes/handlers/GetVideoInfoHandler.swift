import Flutter

class GetVideoInfoHandler: BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        do {
            let info = try VideoManager.shared.getVideoInfo()
            result(info)
        } catch {
            result(FlutterError(code: "INFO_ERROR",
                              message: error.localizedDescription,
                              details: nil))
        }
    }
}
