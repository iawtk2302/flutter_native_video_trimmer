import Flutter

class MethodManager: BaseMethodHandler {
    private var handlers: [MethodName: BaseMethodHandler] = [:]

    init() {
        self.handlers = [
            .loadVideo: LoadVideoHandler(),
            .trimVideo: TrimVideoHandler(),
            .clearTrimVideoCache: ClearTrimVideoCacheHandler()
        ]
    }
    
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let method = MethodName(rawValue: call.method) else {
            result(FlutterMethodNotImplemented)
            return
        }
        handlers[method]?.handle(call, result: result) 
    }
}
