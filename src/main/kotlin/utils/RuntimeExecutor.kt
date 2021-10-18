package utils

import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

interface RuntimeExecutor {

  suspend fun isAvailable(): Boolean

  suspend fun execute(cmd: String)

  fun String.runCommand(workingDir: File = File(".")): String? {
    return try {
      val parts = this.split("\\s".toRegex())
      val proc = ProcessBuilder(*parts.toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()

      proc.waitFor(60, TimeUnit.SECONDS)
      proc.inputStream.bufferedReader().readText()
    } catch(e: IOException) {
      e.printStackTrace()
      null
    }
  }
}