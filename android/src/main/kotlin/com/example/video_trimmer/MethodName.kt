enum class MethodName(val method: String) {
    LOAD_VIDEO("loadVideo"),
    TRIM_VIDEO("trimVideo"),
    GET_VIDEO_THUMBNAIL("getVideoThumbnail"),
    GET_VIDEO_INFO("getVideoInfo"),
    CLEAR_TRIM_VIDEO_CACHE("clearTrimVideoCache");

    companion object {
        fun fromString(method: String): MethodName? {
            return values().find { it.method == method }
        }
    }
}