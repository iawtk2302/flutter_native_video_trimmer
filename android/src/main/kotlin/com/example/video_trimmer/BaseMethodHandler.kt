import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

interface BaseMethodHandler {
    fun handle(call: MethodCall, result: MethodChannel.Result)
}