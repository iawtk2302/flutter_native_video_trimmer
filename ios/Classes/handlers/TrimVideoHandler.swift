import Flutter

class TrimVideoHandler: BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let args = call.arguments as? [String: Any],
              let inputPath = args["inputPath"] as? String,
              let startTime = args["startTimeMs"] as? Int,
              let endTime = args["endTimeMs"] as? Int else {
            result(FlutterError(code: "INVALID_ARGUMENTS",
                              message: "Missing or invalid parameters. Required: inputPath, startTimeMs, endTimeMs",
                              details: nil))
            return
        }
        
        // Validate time range
        guard startTime >= 0 && endTime > startTime else {
            result(FlutterError(code: "INVALID_TIME_RANGE",
                              message: "Invalid time range. endTime must be greater than startTime and startTime must be non-negative",
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
