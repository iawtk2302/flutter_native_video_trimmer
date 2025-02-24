package com.example.video_trimmer

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.effect.Presentation
import androidx.media3.transformer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class VideoManager private constructor(private val context: Context) {
    private var currentVideoPath: String? = null
    private var transformer: Transformer? = null
    private val mediaMetadataRetriever = MediaMetadataRetriever()

    companion object {
        @Volatile
        private var instance: VideoManager? = null

        fun getInstance(context: Context): VideoManager {
            return instance ?: synchronized(this) {
                instance ?: VideoManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun loadVideo(path: String) {
        if (!File(path).exists()) {
            throw VideoException("Video file not found")
        }
        currentVideoPath = path
        mediaMetadataRetriever.setDataSource(path)
    }

    suspend fun trimVideo(
        startTimeMs: Long,
        endTimeMs: Long,
    ): String = withContext(Dispatchers.IO) {
        val videoPath = currentVideoPath ?: throw VideoException("No video loaded")
        
        val timestamp = System.currentTimeMillis()
        val outputFile = File(context.cacheDir, "video_trimmer_$timestamp.mp4")

        // Delete existing file if it exists
        if (outputFile.exists()) {
            outputFile.delete()
        }

        suspendCancellableCoroutine { continuation ->
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.fromFile(File(videoPath)))
                .setClippingConfiguration(
                    MediaItem.ClippingConfiguration.Builder()
                        .setStartPositionMs(startTimeMs)
                        .setEndPositionMs(endTimeMs)
                        .build()
                )
                .build()

            val transformerBuilder = Transformer.Builder(context)
                .setVideoMimeType(MimeTypes.VIDEO_H264)
                .addListener(
                    object : Transformer.Listener {
                        override fun onTransformationCompleted(inputMediaItem: MediaItem, result: TransformationResult) {
                            continuation.resume(outputFile.absolutePath)
                        }

                        override fun onTransformationError(
                            inputMediaItem: MediaItem,
                            exception: TransformationException
                        ) {
                            continuation.resumeWithException(VideoException("Failed to trim video", exception))
                        }
                    }
                )

            transformer = transformerBuilder.build()
            transformer?.start(mediaItem, outputFile.absolutePath)

            continuation.invokeOnCancellation {
                transformer?.cancel()
            }
        }
    }

    suspend fun generateThumbnail(
        positionMs: Long,
        width: Int? = null,
        height: Int? = null,
        quality: Int
    ): String = withContext(Dispatchers.IO) {
        if (currentVideoPath == null) {
            throw VideoException("No video loaded")
        }

        val bitmap = mediaMetadataRetriever.getFrameAtTime(
            positionMs * 1000, // Convert to microseconds
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        ) ?: throw VideoException("Failed to generate thumbnail")

        val scaledBitmap = if (width != null && height != null) {
            Bitmap.createScaledBitmap(bitmap, width, height, true)
        } else {
            bitmap
        }

        val timestamp = System.currentTimeMillis()
        val outputFile = File(context.cacheDir, "video_trimmer_$timestamp.jpg")
        
        FileOutputStream(outputFile).use { out ->
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)
        }

        if (scaledBitmap != bitmap) {
            scaledBitmap.recycle()
        }
        bitmap.recycle()

        outputFile.absolutePath
    }

    fun getVideoInfo(): Map<String, Any> {
        val videoPath = currentVideoPath ?: throw VideoException("No video loaded")

        val duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0
        val width = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt() ?: 0
        val height = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt() ?: 0
        val mimeType = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)

        return mapOf(
            "duration" to duration,
            "width" to width,
            "height" to height,
            "mimeType" to mimeType
        )
    }

    fun clearCache() {
        context.cacheDir.listFiles()?.forEach { file ->
            if (file.name.startsWith("video_trimmer_") && 
                (file.extension == "mp4" || file.extension == "jpg")) {
                file.delete()
            }
        }
    }

    fun release() {
        transformer?.cancel()
        transformer = null
        mediaMetadataRetriever.release()
        instance = null
    }
}

class VideoException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
