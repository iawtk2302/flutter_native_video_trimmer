import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaItem.ClippingConfiguration
import androidx.media3.common.util.UnstableApi
import androidx.media3.transformer.Composition
import androidx.media3.transformer.EditedMediaItem
import androidx.media3.transformer.ExportException
import androidx.media3.transformer.ExportResult
import androidx.media3.transformer.Transformer
import java.io.File

class VideoTrimmer {
    companion object{
        @UnstableApi
        fun trimVideo(
            context: Context,
            videoPath: String,
            startTimeMs: Int,
            endTimeMs: Int,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit
        ) {

            val videoFile = File(videoPath)

            // Check if the input file exists
            if (!videoFile.exists()) {
                onError("Input video file does not exist.")
                return
            }

            // Validate startTimeMs and endTimeMs
            if (startTimeMs < 0 || endTimeMs < 0) {
                onError("Start time and end time must be non-negative.")
                return
            }
            if (endTimeMs <= startTimeMs) { // Ensure endTimeMs is greater than startTimeMs
                onError("End time must be greater than start time.")
                return
            }

            // Extract file extension from inputPath
            val fileExtension = videoFile.extension.ifEmpty { "mp4" }

            val videoUri = Uri.fromFile(videoFile)
            val timestamp = System.currentTimeMillis() // Get currentTimeMillis
            val trimmedVideoFile = File(context.cacheDir, "video_trimmer_${timestamp}.${fileExtension}")

            // Initialize MediaItem with video trimming configuration
            val mediaItem = MediaItem.Builder()
                .setUri(videoUri)
                .setClippingConfiguration(
                    ClippingConfiguration.Builder()
                        .setStartPositionMs(startTimeMs.toLong())
                        .setEndPositionMs(endTimeMs.toLong())
                        .build()
                )
                .build()

            // Configure the trimmed video from startTimeMs to endTimeMs
            val editedMediaItem = EditedMediaItem.Builder(mediaItem).build()

            // Initialize Transformer
            val transformer = Transformer.Builder(context)
                .addListener(object : Transformer.Listener {
                    override fun onCompleted(composition: Composition, result: ExportResult) {
                        onSuccess(trimmedVideoFile.absolutePath)
                    }

                    override fun onError(
                        composition: Composition,
                        exportResult: ExportResult,
                        exportException: ExportException
                    ) {
                        onError("Error trimming video: ${exportException.message}")
                    }
                })
                .build()

            // Start trimming the video
            transformer.start(editedMediaItem, trimmedVideoFile.path)
        }

        fun clearTrimmedVideosCache(context: Context) {
            val cacheDir = context.cacheDir
            cacheDir.listFiles()?.forEach { file ->
                if (file.name.startsWith("video_trimmer")) {
                    file.delete()
                }
            }
        }
    }
}