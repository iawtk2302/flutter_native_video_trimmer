import Flutter
import UIKit

public class VideoTrimmerPlugin: NSObject, FlutterPlugin {
    
    private var methodManager: MethodManager
    
    static let CHANNEL_NAME = "flutter_native_video_trimmer"

    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: CHANNEL_NAME, binaryMessenger: registrar.messenger())
        let instance = VideoTrimmerPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        
    
    }
    
    override init() {
        self.methodManager = MethodManager()
        super.init()
    }

    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
       methodManager.handle(call, result: result)
    }
}
