package videoprocessing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

/**
 * A way to queue multiple processors to process a video file. Each successive processor
 * will get fed the output from the previous one
 */
class VideoPipeline(
  private val processors: List<VideoProcessor>
){

  suspend fun process(videoFile: File): PipelineResult = withContext(Dispatchers.IO) {
    processors.fold(PipelineResult.Builder(videoFile)) { acc, processor ->
      try {
        val output = processor.process(acc.currentOutput)
        acc.apply { addStep(processor, output = output) }
      } catch (e: Exception) {
        acc.apply { addStep(processor, error = e) }
      }
    }.build()
  }
}

data class PipelineResult(
  val final: File,
  val steps: List<Step>
) {

  fun stepFor(key: String): Step {
    return steps.first { it.key == key }
  }

  data class Step(
    val key: String,
    val output: File? = null,
    val error: String? = null,
  )

  class Builder(private val initial: File) {
    var currentOutput: File = initial
      private set
    private val steps = mutableListOf<Step>()

    fun addStep(processor: VideoProcessor, output: File): Builder {
      steps += Step(processor.key, output)
      return this
    }

    fun addStep(processor: VideoProcessor, error: Throwable): Builder {
      steps += Step(processor.key, error = error.localizedMessage ?: "Unable to run '${processor.key}'")
      return this
    }

    fun build(): PipelineResult = PipelineResult(
      final = currentOutput,
      steps = steps,
    )
  }
}

fun pipeline(vararg processors: VideoProcessor) = VideoPipeline(processors.toList())