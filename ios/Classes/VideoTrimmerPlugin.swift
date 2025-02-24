import Flutter
import UIKit

public class VideoTrimmerPlugin: NSObject, FlutterPlugin {
    
    private var handlers: [MethodName: BaseMethodHandler] = [:]
    
    static let CHANNEL_NAME = "video_trimmer"

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: CHANNEL_NAME, binaryMessenger: registrar.messenger())
        let instance = VideoTrimmerPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        
        instance.setupHandlers()
    }
    
    private func setupHandlers() {
        handlers = [
            .loadVideo: LoadVideoHandler(),
            .trimVideo: TrimVideoHandler(),
            .getVideoThumbnail: GetVideoThumbnailHandler(),
            .getVideoInfo: GetVideoInfoHandler(),
            .clearTrimVideoCache: ClearTrimVideoCacheHandler()
        ]
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        guard let method = MethodName(rawValue: call.method) else {
            result(FlutterMethodNotImplemented)
            return
        }
        handlers[method]?.handle(call, result: result)
    }
}
