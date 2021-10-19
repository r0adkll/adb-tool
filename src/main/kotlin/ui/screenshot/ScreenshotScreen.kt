package ui.screenshot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import di.LocalApplication
import org.jetbrains.skija.Image.makeFromEncoded
import router.BackStack
import ui.Routing
import ui.widgets.EmptyState
import ui.widgets.Loading
import utils.toByteArray
import java.awt.image.BufferedImage

@Composable
fun ScreenShotScreen(backStack: BackStack<Routing>) {
  val app = LocalApplication.current
  val presenter = remember {
    ScreenShotPresenter(app.selectedDevice)
  }
  presenter.bindToComposable {
    presenter.takeScreenshot()
  }

  val state = presenter.state.collectAsState()

  Column {
    TopAppBar(
      title = { Text("Screenshot") },
      navigationIcon = {
        IconButton(
          onClick = {
            backStack.pop()
          }
        ) {
          Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null
          )
        }
      },
      actions = {
        IconButton(
          onClick = {
            presenter.takeScreenshot()
          }
        ) {
          Icon(
            Icons.Rounded.Refresh,
            contentDescription = null
          )
        }
        IconButton(
          onClick = {
            presenter.saveCurrentScreenShot()
          }
        ) {
          Icon(
            Icons.Rounded.Save,
            contentDescription = null
          )
        }
      }
    )

    when (val s = state.value) {
      is State.Loading -> Loading()
      is State.Empty -> EmptyState(
        icon = Icons.Rounded.Screenshot,
        text = "No screenshot taken"
      )
      is State.Error -> EmptyState(
        icon = Icons.Rounded.ErrorOutline,
        text = s.reason,
      )
      is State.ScreenShot -> ScreenshotImage(
        image = s.image
      )
    }
  }
}

@Composable
private fun ScreenshotImage(
  image: BufferedImage
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center,
  ) {
    Image(
      modifier = Modifier.padding(16.dp),
      bitmap = makeFromEncoded(
        toByteArray(image)
      ).asImageBitmap(),
      contentDescription = null,
    )
  }
}