package ui

sealed class Routing {
  object Home : Routing()
  object ScreenShot : Routing()
  object ScreenRecord : Routing()
  object IntentBuilder : Routing()
}