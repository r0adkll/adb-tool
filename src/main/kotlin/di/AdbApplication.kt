package di

import androidx.compose.runtime.staticCompositionLocalOf
import domain.DeviceRepository
import domain.model.DeviceWithProps
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdbApplication : KoinComponent {
  val selectedDevice = MutableStateFlow<DeviceWithProps?>(null)

  val deviceRepository by inject<DeviceRepository>()
  val appSettings by inject<AppSettings>()
}

val LocalApplication = staticCompositionLocalOf {
  AdbApplication()
}