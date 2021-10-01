package domain

import domain.model.DeviceWithProps
import domain.model.RecordingRequest
import domain.model.RecordingSession
import kotlinx.coroutines.flow.Flow
import java.awt.image.BufferedImage
import java.io.File

interface DeviceRepository {

  suspend fun getDevices(): List<DeviceWithProps>

  suspend fun getDeviceScreenshot(serial: String): BufferedImage?

  suspend fun startDeviceScreenRecording(serial: String, request: RecordingRequest): Flow<String>

  suspend fun pullRecordingFromDevice(serial: String, filename: String): File?
}