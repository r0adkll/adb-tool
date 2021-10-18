package utils

import java.io.File

/**
 * Compression and Quality presets
 *
 * High Quality / Larger Size -> Fast
 * Lower Quality / Smaller Size -> Slow
 */
enum class Preset(val value: String) {
  UltraFast("ultrafast"),
  SuperFast("superfast"),
  VeryFast("veryfast"),
  Faster("faster"),
  Fast("fast"),
  Medium("medium"),
  Slow("slow"),
  Slower("slower"),
  VerySlow("veryslow"),
}

class FFMpegCommand(
  val input: File,
  val output: File,
  val preset: Preset = Preset.Medium,
  val scale: String? = null,
) {

  @OptIn(ExperimentalStdlibApi::class)
  fun build(): String {
    val formats = buildList {
      add("fps=30")
      scale?.let { add("scale=$it") }
    }.joinToString(",")
    return "ffmpeg -i ${input.absolutePath} -vf $formats -preset ${preset.value} ${output.absolutePath}"
  }
}

object FFMpegRuntimeExecutor : RuntimeExecutor {

  override suspend fun isAvailable(): Boolean {
    return "ffmpeg -version".runCommand(File(".")) != null
  }

  override suspend fun execute(cmd: String) {
    val output = cmd.runCommand()
    println(output)
  }
}