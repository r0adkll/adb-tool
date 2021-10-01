package ui.screenshot

import di.AppSettings
import domain.DeviceRepository
import domain.model.DeviceWithProps
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import ui.components.ComposePresenter
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.ImageIcon

class ScreenShotPresenter(
  private val selectedDevice: StateFlow<DeviceWithProps?>,
) : ComposePresenter<State>(State.Loading) {

  private val deviceRepository by inject<DeviceRepository>()
  private val settings by inject<AppSettings>()

  fun takeScreenshot() {
    scope.launch {
      val serial = selectedDevice.value?.device?.serial
      if (serial != null) {
        _state.value = State.Loading
        val image = deviceRepository.getDeviceScreenshot(serial)
        if (image != null) {
          _state.value = State.ScreenShot(image)
        } else {
          _state.value = State.Empty
        }
      } else {
        _state.value = State.Error("No device selected")
      }
    }
  }

  fun saveCurrentScreenShot() {
    scope.launch {
      (_state.value as? State.ScreenShot)?.image.let { image ->
        val dir = /*settings.screenShotDirectory?.let { File(it) } ?:*/ File("./screenshots").apply { mkdirs() }
        ImageIO.write(image, "png", File(dir, "screenshot-${UUID.randomUUID()}.png"))
        // TODO: Notify User
      } ?: kotlin.run {
        // TODO: Notify User
      }
    }
  }
}

sealed class State {
  object Loading : State()
  object Empty : State()
  class ScreenShot(val image: BufferedImage) : State()
  class Error(val reason: String) : State()
}