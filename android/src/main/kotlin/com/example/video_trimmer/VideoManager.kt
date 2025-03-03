package com.example.video_trimmer

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.lang.ref.WeakReference



@UnstableApi
class VideoManager {
    private var currentVideoPath: String? = null
    private var transformer: Transformer? = null
    private val mediaMetadataRetriever = MediaMetadataRetriever()

    companion object {
        @Volatile
        private var instance: VideoManager? = null

        fun getInstance(): VideoManager {
            return instance ?: synchronized(this) {
                instance ?: VideoManager().also { instance = it }
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
        context: Context,
        startTimeMs: Long,
        endTimeMs: Long,
    ): String {
        val videoPath = currentVideoPath ?: throw VideoException("No video loaded")
        
        // Create output file on IO thread
        val outputFile = withContext(Dispatchers.IO) {
            val timestamp = System.currentTimeMillis()
            val file = File(context.cacheDir, "video_trimmer_$timestamp.mp4")
            if (file.exists()) {
                file.delete()
            }
            file
        }

        // Switch to Main thread for Transformer operations
        return withContext(Dispatchers.Main) {
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
                    .addListener(
                        object : Transformer.Listener {
                            override fun onCompleted(
                                composition: Composition,
                                exportResult: ExportResult
                            ) {
                                continuation.resume(outputFile.absolutePath)
                            }

                            override fun onError(
                                composition: Composition,
                                exportResult: ExportResult,
                                exportException: ExportException
                            ) {
                                continuation.resumeWithException(VideoException("Failed to trim video", exportException))
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
    }

    suspend fun generateThumbnail(
        context: Context,
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

    fun clearCache(context: Context) {
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
        currentVideoPath = null
        synchronized(this) {
            instance = null
        }
    }
}

class VideoException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
