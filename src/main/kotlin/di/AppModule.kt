package di

import com.malinskiy.adam.AndroidDebugBridgeClientFactory
import data.RedditDeviceRepository
import domain.DeviceRepository
import org.koin.dsl.module

val appModule = module {

  // Provide the ADB client
  single {
    AndroidDebugBridgeClientFactory().build()
  }

  // Provide Repository
  single<DeviceRepository> { RedditDeviceRepository(get()) }

//  single {
//    val delegate = Preferences.userRoot()
//    val settings = JvmPreferencesSettings(delegate)
//    AppSettings(settings)
//  }
}