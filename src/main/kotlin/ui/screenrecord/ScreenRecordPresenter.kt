package ui.screenrecord

import di.AdbApplication
import domain.DeviceRepository
import domain.model.RecordingRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import ui.components.ComposePresenter
import videoprocessing.PipelineResult
import videoprocessing.pipeline
import videoprocessing.processors.CompressionProcessor
import videoprocessing.processors.GifProcessor
import java.io.File
import java.util.*

class ScreenRecordPresenter(
  private val app: AdbApplication,
  private val outputDir: File = File("./recordings"),
) : ComposePresenter<State>(State.Setup) {
  private val deviceRepository by inject<DeviceRepository>()

  // TODO: Maybe inject this
  private val pipeline = pipeline(
    CompressionProcessor(outputDir),
    GifProcessor(outputDir),
  )

  private var recordingOutputJob: Job? = null
  private var activeFilename: String? = null


  fun startRecording() {
    scope.launch {
      val selectedDevice = app.selectedDevice.value ?: return@launch
      recordingOutputJob?.cancel()
      activeFilename = "recording-${UUID.randomUUID()}.mp4"
      recordingOutputJob = deviceRepository.startDeviceScreenRecording(
        serial = selectedDevice.device.serial,
        request = RecordingRequest(
          filename = activeFilename!!,
          verbose = true
        )
      )
        .scan("") { acc, value -> acc + value }
        .onEach { output ->
          print("Output: $output")
          _state.value = State.Recording(output)
        }
        .catch {
          activeFilename = null
          _state.value = State.Error
        }
        .launchIn(scope)
    }
  }

  fun stopRecording() {
    recordingOutputJob?.cancel()

    // Now attempt to pull the request
    if (activeFilename != null) {
      scope.launch {
        val selectedDevice = app.selectedDevice.value ?: return@launch
        _state.value = State.Processing("Waiting for device...")

        // Artificial delay giving the device time to process and finish screen recording
        delay(5000L)

        val file = deviceRepository.pullRecordingFromDevice(selectedDevice.device.serial, activeFilename!!)
        if (file != null) {
          _state.value = State.Processing("Processing recording...")

          println("Recording pulled! ${file.absolutePath}")
          val output = pipeline.process(file)

          // Update state to processed
          _state.value = State.Recorded(
            video = output.final,
            result = output,
          )
        }
      }
    }
  }
}

sealed class State {
  object Setup : State()

  object Error : State()

  class Recording(
    val currentOutput: String? = null
  ) : State()

  class Processing(
    val message: String
  ) : State()

  data class Recorded(
    val video: File,
    val result: PipelineResult,
  ) : State()
}