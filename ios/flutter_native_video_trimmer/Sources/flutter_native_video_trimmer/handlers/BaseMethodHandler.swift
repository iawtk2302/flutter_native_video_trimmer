import Flutter

protocol BaseMethodHandler {
    func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult)
}
