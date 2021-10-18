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

    /*
     * For some reason trying to run the below command does not work
     * (maybe something to do with how it has an active input when you run it)
     * so instead we write it to a bash file and execute it from there, cleaning up
     * the file when finished.
     */
    val gifCmd = "ffmpeg -y -ss 0 -i ${videoFile.absolutePath} -filter_complex \"fps=30,scale=320:-1:flags=lanczos[x];[x]split[x1][x2];[x1]palettegen[p];[x2][p]paletteuse\" ${outputFile.absolutePath}"
    val shellFile = File(outputDir, "gif.sh")
    shellFile.writeText(gifCmd)
    shellFile.setExecutable(true)

    // Create the gif
    val command = "sh ${shellFile.absolutePath}"
    runtimeExecutor.execute(command)

    // Cleanup
    shellFile.delete()

    return outputFile
  }

  companion object {
    const val KEY = "gif-processor"
  }
}