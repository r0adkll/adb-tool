package di

//import com.russhwolf.settings.Settings

class AppSettings(
//  private val settings: Settings
) {

//  var screenShotDirectory: String?
//    get() = settings.getStringOrNull(SCREEN_SHOT_DIRECTORY)
//    set(value) {
//      value?.let { settings.putString(SCREEN_SHOT_DIRECTORY, it) }
//        ?: settings.remove(SCREEN_SHOT_DIRECTORY)
//    }

  companion object {
    private const val SCREEN_SHOT_DIRECTORY = "AdbTool.ScreenShots.Directory"
  }
}