package videoprocessing.processors

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utils.FFMpegCommand
import utils.FFMpegRuntimeExecutor
import utils.Preset
import utils.RuntimeExecutor
import videoprocessing.VideoProcessor
import java.io.File

class CompressionProcessor(
  private val outputDir: File,
  private val runtimeExecutor: RuntimeExecutor = FFMpegRuntimeExecutor,
  private val preset: Preset = Preset.VeryFast,
  private val scale: String = "-1:1080"
) : VideoProcessor {

  override val key: String = KEY

  override suspend fun process(videoFile: File): File = withContext(Dispatchers.IO) {
    val outputFileName = "${videoFile.nameWithoutExtension}-optimized.${videoFile.extension}"
    val outputFile = File(outputDir, outputFileName)
    val command = FFMpegCommand(
      input = videoFile,
      output = outputFile,
      preset = preset,
      scale = scale,
    )

    runtimeExecutor.execute(command.build())

    outputFile
  }

  companion object {
    const val KEY = "compression-processor"
  }
}