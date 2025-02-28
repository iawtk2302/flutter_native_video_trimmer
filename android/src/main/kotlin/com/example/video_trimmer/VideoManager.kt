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
class VideoManager private constructor(context: Context) {
    private val contextRef = WeakReference(context.applicationContext)
    private val context: Context
        get() = contextRef.get() ?: throw IllegalStateException("Context was garbage collected")
    private var currentVideoPath: String? = null
    private var transformer: Transformer? = null
    private val mediaMetadataRetriever = MediaMetadataRetriever()

    companion object {
        @Volatile
        private var instance: WeakReference<VideoManager>? = null

        fun getInstance(context: Context): VideoManager {
            val currentInstance = instance?.get()
            if (currentInstance != null) {
                return currentInstance
            }
            
            return synchronized(this) {
                instance?.get()
                    ?: VideoManager(context.applicationContext).also {
                        instance = WeakReference(it)
                    }
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
        synchronized(VideoManager) {
            instance?.clear()
            instance = null
        }
    }
}

class VideoException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
