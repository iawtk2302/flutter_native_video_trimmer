import Flutter

class LoadVideoHandler: BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any],
              let path = args["path"] as? String else {
            result(FlutterError(code: "INVALID_ARGUMENTS",
                              message: "Missing or invalid path parameter",
                              details: nil))
            return
        }
        
        do {
            try VideoManager.shared.loadVideo(path: path)
            result(nil)
        } catch {
            result(FlutterError(code: "LOAD_ERROR",
                              message: error.localizedDescription,
                              details: nil))
        }
    }
}
