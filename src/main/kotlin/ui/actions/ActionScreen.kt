package ui.actions

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.LocalApplication
import router.BackStack
import ui.Routing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActionScreen(backStack: BackStack<Routing>) {
  val app = LocalApplication.current
  val selectedDevice = app.selectedDevice.collectAsState()

  if (selectedDevice.value != null) {
    LazyVerticalGrid(
      cells = GridCells.Adaptive(200.dp),
      contentPadding = PaddingValues(8.dp),
    ) {
      item {
        Action(
          title = "Screenshot",
          icon = {
            Icon(
              Icons.Rounded.Screenshot,
              contentDescription = null,
            )
          },
          onClick = {
            backStack.push(Routing.ScreenShot)
          }
        )
      }

      item {
        Action(
          title = "Record Video",
          icon = {
            Icon(
              Icons.Rounded.VideoCall,
              contentDescription = null,
            )
          },
          onClick = {
            backStack.push(Routing.ScreenRecord)
          }
        )
      }

      item {
        Action(
          title = "Send Intent",
          icon = {
            Icon(
              Icons.Rounded.SendToMobile,
              contentDescription = null,
            )
          },
          onClick = {
            backStack.push(Routing.IntentBuilder)
          }
        )
      }
    }
  } else {
    Empty()
  }
}

@Composable
private fun Empty() {
  val strokeWidth = with(LocalDensity.current) { 3.dp.toPx() }
  val cornerRadius = with(LocalDensity.current) { 16.dp.toPx() }
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Canvas(Modifier.fillMaxSize()) {
      drawRoundRect(
        color = Color.Black.copy(.54f),
        style = Stroke(
          width = strokeWidth,
          pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
        ),
        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
      )
    }

    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Icon(
        Icons.Rounded.Settings,
        modifier = Modifier.size(48.dp),
        tint = Color.Black.copy(.54f),
        contentDescription = null,
      )
      Text(
        text = "Select a device in the panel to the left",
        modifier = Modifier.padding(top = 16.dp),
        color = Color.Black.copy(.54f),
      )
    }
  }
}

@Composable
private fun Action(
  title: String,
  icon: (@Composable () -> Unit)? = null,
  onClick: () -> Unit = { }
) {
  Card(
    modifier = Modifier
      .padding(8.dp),
    elevation = 4.dp,
    shape = RoundedCornerShape(16.dp),
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 150.dp)
        .clickable(onClick = onClick),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
    ) {
      icon?.invoke()

      Text(
        title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(top = 16.dp)
      )
    }
  }
}