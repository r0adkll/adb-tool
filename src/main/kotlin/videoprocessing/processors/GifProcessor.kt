package videoprocessing.processors

import utils.FFMpegRuntimeExecutor
import utils.RuntimeExecutor
import videoprocessing.VideoProcessor
import java.io.File

class GifProcessor(
  private val outputDir: File,
  private val runtimeExecutor: RuntimeExecutor = FFMpegRuntimeExecutor,
) : VideoProcessor {

  override val key: String = KEY

  override suspend fun process(videoFile: File): File {
    val outputFileName = "${videoFile.nameWithoutExtension}.gif"
    val outputFile = File(outputDir, outputFileName)

    // Generage GIF
    val command = "ffmpeg -y -i ${videoFile.absolutePath} -filter_complex \"fps=30,scale=320:-1:flags=lanczos[x];[x]split[x1][x2];[x1]palettegen[p];[x2][p]paletteuse\" ${outputFile.absolutePath}"
    println(command)
    runtimeExecutor.execute(command)

    return outputFile
  }

  companion object {
    const val KEY = "gif-processor"
  }
}