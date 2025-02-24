import AVFoundation
import UIKit

class VideoManager {
    static let shared = VideoManager()
    private var currentAsset: AVAsset?
    private let fileManager = FileManager.default
    
    private init() {}
    
    func loadVideo(path: String) throws {
        guard fileManager.fileExists(atPath: path) else {
            throw VideoError.fileNotFound
        }
        let url = URL(fileURLWithPath: path)
        currentAsset = AVAsset(url: url)
    }
    
    func trimVideo(startTimeMs: Int64, endTimeMs: Int64, outputPath: String?, completion: @escaping (Result<String, Error>) -> Void) {
        guard let asset = currentAsset else {
            completion(.failure(VideoError.noVideoLoaded))
            return
        }
        
        let compatiblePresets = AVAssetExportSession.exportPresets(compatibleWith: asset)
        guard compatiblePresets.contains(AVAssetExportPresetHighestQuality) else {
            completion(.failure(VideoError.unsupportedFormat))
            return
        }
        
        guard let exportSession = AVAssetExportSession(asset: asset, presetName: AVAssetExportPresetHighestQuality) else {
            completion(.failure(VideoError.exportSessionFailed))
            return
        }
        
        let outputURL: URL
        if let outputPath = outputPath {
            outputURL = URL(fileURLWithPath: outputPath)
        } else {
            let timestamp = Int64(Date().timeIntervalSince1970 * 1000)
            outputURL = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent("video_trimmer_\(timestamp).mp4")
        }
        
        // Delete any existing file
        try? fileManager.removeItem(at: outputURL)
        
        exportSession.outputURL = outputURL
        exportSession.outputFileType = .mp4
        
        // Convert milliseconds to CMTime
        let startTime = CMTime(value: startTimeMs, timescale: 1000)
        let endTime = CMTime(value: endTimeMs, timescale: 1000)
        let timeRange = CMTimeRange(start: startTime, end: endTime)
        
        exportSession.timeRange = timeRange
        
        exportSession.exportAsynchronously {
            switch exportSession.status {
            case .completed:
                completion(.success(outputURL.path))
            case .failed:
                completion(.failure(exportSession.error ?? VideoError.exportFailed))
            case .cancelled:
                completion(.failure(VideoError.exportCancelled))
            default:
                completion(.failure(VideoError.unknown))
            }
        }
    }
    
    func generateThumbnail(atMs position: Int64, size: CGSize?, quality: Int) throws -> String {
        guard let asset = currentAsset else {
            throw VideoError.noVideoLoaded
        }
        
        let generator = AVAssetImageGenerator(asset: asset)
        generator.appliesPreferredTrackTransform = true
        if let size = size {
            generator.maximumSize = size
        }
        
        // Convert milliseconds to CMTime
        let time = CMTime(value: position, timescale: 1000)
        let imageRef = try generator.copyCGImage(at: time, actualTime: nil)
        let image = UIImage(cgImage: imageRef)
        
        let timestamp = Int64(Date().timeIntervalSince1970 * 1000)
        let outputURL = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent("video_trimmer_\(timestamp).jpg")
        
        guard let data = image.jpegData(compressionQuality: CGFloat(quality) / 100),
              let _ = try? data.write(to: outputURL) else {
            throw VideoError.thumbnailGenerationFailed
        }
        
        return outputURL.path
    }
    
    func getVideoInfo() throws -> [String: Any] {
        guard let asset = currentAsset else {
            throw VideoError.noVideoLoaded
        }
        
        let durationMs = Int64(asset.duration.seconds * 1000)
        var size: CGSize = .zero
        
        if let track = asset.tracks(withMediaType: .video).first {
            size = track.naturalSize
        }
        
        return [
            "duration": durationMs,
            "width": Int(size.width),
            "height": Int(size.height)
        ]
    }
    
    func clearCache() {
        let tempDirectory = URL(fileURLWithPath: NSTemporaryDirectory())
        let enumerator = fileManager.enumerator(at: tempDirectory, includingPropertiesForKeys: nil)
        
        while let url = enumerator?.nextObject() as? URL {
            if url.pathExtension == "mp4" || url.pathExtension == "jpg" {
                try? fileManager.removeItem(at: url)
            }
        }
    }
}

enum VideoError: LocalizedError {
    case fileNotFound
    case noVideoLoaded
    case unsupportedFormat
    case exportSessionFailed
    case exportFailed
    case exportCancelled
    case thumbnailGenerationFailed
    case unknown
    
    var errorDescription: String? {
        switch self {
        case .fileNotFound:
            return "Video file not found"
        case .noVideoLoaded:
            return "No video is currently loaded"
        case .unsupportedFormat:
            return "Video format is not supported"
        case .exportSessionFailed:
            return "Failed to create export session"
        case .exportFailed:
            return "Failed to export video"
        case .exportCancelled:
            return "Video export was cancelled"
        case .thumbnailGenerationFailed:
            return "Failed to generate thumbnail"
        case .unknown:
            return "An unknown error occurred"
        }
    }
}
