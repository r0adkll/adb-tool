package ui.screenrecord

import di.AdbApplication
import domain.DeviceRepository
import domain.model.RecordingRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import ui.components.ComposePresenter
import java.util.*

class ScreenRecordPresenter(
  private val app: AdbApplication
) : ComposePresenter<State>(State.Setup) {
  private val deviceRepository by inject<DeviceRepository>()

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
        .onCompletion {
          _state.value = State.Processing
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
        val file = deviceRepository.pullRecordingFromDevice(selectedDevice.device.serial, activeFilename!!)
        if (file != null) {
          println("Recording pulled! ${file.absolutePath}")
          // TODO: Send to FFMPEG or Handbrake to compress the video

          // TODO: Send to tool to make a gif

          // Update state to processed
          _state.value = State.Recorded(
            videoPath = file.absolutePath,
            compressedVideoPath = null,
            gifPath = null,
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

  object Processing : State()

  data class Recorded(
    val videoPath: String,
    val compressedVideoPath: String?,
    val gifPath: String?,
  ) : State()
}