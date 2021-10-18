package data

import com.malinskiy.adam.AndroidDebugBridgeClient
import com.malinskiy.adam.request.framebuffer.RawImageScreenCaptureAdapter
import com.malinskiy.adam.request.framebuffer.ScreenCaptureRequest
import com.malinskiy.adam.request.shell.v1.ChanneledShellCommandRequest
import com.malinskiy.adam.request.sync.PullRequest
import domain.DeviceRepository
import domain.model.DeviceWithProps
import domain.model.RecordingRequest
import domain.model.RecordingSession
import domain.model.ShellCommandOutput
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*

class RedditDeviceRepository(
  private val adb: AndroidDebugBridgeClient
) : DeviceRepository {

  override suspend fun getDevices(): List<DeviceWithProps> = withContext(Dispatchers.IO) {
    adb.execute(CustomListDevicesRequest())
  }

  override suspend fun getDeviceScreenshot(serial: String): BufferedImage = withContext(Dispatchers.IO) {
    val adapter = RawImageScreenCaptureAdapter()
    adb.execute(
      request = ScreenCaptureRequest(adapter),
      serial = serial,
    ).toBufferedImage()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  override suspend fun startDeviceScreenRecording(serial: String, request: RecordingRequest): Flow<String> {
    return flow {
      val cmd = request.buildCmd()

      println("Sending Shell Cmd: $cmd")
      val receiveChannel = adb.execute(ChanneledShellCommandRequest(cmd), CoroutineScope(currentCoroutineContext()), serial)

      for (update in receiveChannel) {
        println("Recording: $update")
        emit(update)
      }
    }
  }

  override suspend fun pullRecordingFromDevice(serial: String, filename: String): File? = withContext(Dispatchers.IO) {
    val outputDirectory = File("./recordings").apply { mkdirs() }
    val outputFile = File(outputDirectory, filename)
    if (adb.execute(PullRequest("/sdcard/$filename", outputDirectory, emptyList()), serial)) {
      outputFile
    } else {
      null
    }
  }
}