package videoprocessing

import java.io.File

interface VideoProcessor {

  /**
   * A unique key to identify this processor
   */
  val key: String

  /**
   * Process a video file
   * @param videoFile the video file to process
   * @return the processed video file
   */
  suspend fun process(videoFile: File): File
}