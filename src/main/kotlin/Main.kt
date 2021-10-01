// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberTrayState
import com.malinskiy.adam.interactor.StartAdbInteractor
import di.AdbApplication
import di.LocalApplication
import di.appModule
import domain.model.DeviceWithProps
import org.koin.core.context.startKoin
import router.BackPressHandler
import router.LocalBackPressHandler
import router.Router
import ui.Routing
import ui.actions.ActionScreen
import ui.devices.Devices
import ui.screenrecord.ScreenRecordScreen
import ui.screenshot.ScreenShotScreen
import java.io.File
import javax.imageio.ImageIO

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
//  StartAdbInteractor().execute()
  startKoin {
    printLogger()
    modules(appModule)
  }
  val app = AdbApplication()
  application {
    val trayState = rememberTrayState()

    Tray(
      state = trayState,
      icon = ImageIO.read(File("src/main/resources/baseline_android_white_24dp.png").inputStream()),
      menu = {
        Item(
          "Do Stuff",
          onClick = {
          }
        )
        Item(
          "Do More stuff",
          onClick = {
          }
        )
        Separator()
        Item(
          "Refresh Devices",
          onClick = {

          }
        )
      },
      onAction = {
        // Do Stuff?
      }
    )

    val backPressHandler = BackPressHandler()

    Window {
      CompositionLocalProvider(
        LocalApplication provides app,
        LocalBackPressHandler provides backPressHandler,
      ) {
        MaterialTheme {
          Row {
            Surface(
              elevation = 4.dp
            ) {
              Column {
                Devices(
                  modifier = Modifier
                    .weight(1f)
                    .width(256.dp)
                )
                Row(
                  modifier = Modifier
                    .size(256.dp, 56.dp)
                    .clickable {

                    },
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                  Text(
                    "Connect via ADB Wireless",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 16.dp),
                  )
                  Icon(
                    Icons.Rounded.QrCode,
                    modifier = Modifier.padding(16.dp),
                    contentDescription = null,
                  )
                }
              }
            }

            Column(
              Modifier.weight(1f)
            ) {
              Router<Routing>(Routing.Home) { backStack ->
                when (val routing = backStack.last()) {
                  is Routing.Home -> ActionScreen(backStack)
                  is Routing.ScreenShot -> ScreenShotScreen(backStack)
                  is Routing.ScreenRecord -> ScreenRecordScreen(backStack)
                  is Routing.IntentBuilder -> {

                  }
                }
              }
            }
          }
        }
      }
    }
  }
}