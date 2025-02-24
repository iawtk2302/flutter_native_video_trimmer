import Flutter

class TrimVideoHandler: BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any],
              let startTime = args["startTimeMs"] as? Int,
              let endTime = args["endTimeMs"] as? Int else {
            result(FlutterError(code: "INVALID_ARGUMENTS",
                              message: "Missing or invalid startTime/endTime parameters",
                              details: nil))
            return
        }
        
        let outputPath = args["outputPath"] as? String
        
        VideoManager.shared.trimVideo(
            startTimeMs: Int64(startTime),
            endTimeMs: Int64(endTime),
            outputPath: outputPath
        ) { trimResult in
            switch trimResult {
            case .success(let path):
                result(path)
            case .failure(let error):
                result(FlutterError(code: "TRIM_ERROR",
                                  message: error.localizedDescription,
                                  details: nil))
            }
        }
    }
}
