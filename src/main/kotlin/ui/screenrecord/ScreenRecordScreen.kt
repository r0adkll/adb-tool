package ui.screenrecord

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.LocalApplication
import domain.model.VideoSize
import router.BackStack
import ui.Routing
import ui.widgets.EmptyState
import videoprocessing.PipelineResult

@Composable
fun ScreenRecordScreen(backStack: BackStack<Routing>) {
  val app = LocalApplication.current
  val presenter = remember {
    ScreenRecordPresenter(app)
  }
  presenter.BindToComposable()

  val state = presenter.state.collectAsState().value
  when (state) {
    is State.Setup -> RecordingSetup(
      backStack = backStack,
      onStartRecordingClick = {
        presenter.startRecording()
      },
    )
    is State.Error -> EmptyState(
      icon = Icons.Rounded.ErrorOutline,
      text = "Something went wrong with trying to record screen"
    )
    is State.Recording -> RecordingInProgress(state) {
      presenter.stopRecording()
    }
    is State.Processing -> Processing(state.message)
    is State.Recorded -> RecordingFinished(state.result)
  }
}

@Composable
private fun RecordingSetup(
  backStack: BackStack<Routing>,
  onStartRecordingClick: () -> Unit,
) {
  // View State
  var verbose by remember { mutableStateOf(true) }
  var rotate by remember { mutableStateOf(false) }
  var timeLimitInSeconds by remember { mutableStateOf<Int?>(null) }
  var bitRate by remember { mutableStateOf<Int?>(null) }
  var size by remember { mutableStateOf<VideoSize>(VideoSize.Original) }

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    TopAppBar(
      title = { Text("Recording In Progress") },
      navigationIcon = {
        IconButton(onClick = {
          backStack.pop()
        }) {
          Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null
          )
        }
      }
    )

    Row(
      Modifier.padding(16.dp)
    ) {
      Text(
        "Video Size:"
      )

      if (size == VideoSize.Original) {
        Text(
          "Original",
          modifier = Modifier
            .padding(start = 4.dp)
            .clickable {
              size = VideoSize.Custom.DEFAULT
            }
        )
      } else if (size is VideoSize.Custom){
        TextField(
          modifier = Modifier.width(100.dp),
          value = (size as VideoSize.Custom).width.takeIf { it != 0 }?.toString() ?: "",
          onValueChange = {
            it.toIntOrNull()?.let { newWidth ->
              size = (size as VideoSize.Custom).copy(width = newWidth)
            }
          },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
          ),
          maxLines = 1
        )
        Text(
          " x "
        )
        TextField(
          modifier = Modifier.width(100.dp),
          value = (size as VideoSize.Custom).height.takeIf { it != 0 }?.toString() ?: "",
          onValueChange = {
            it.toIntOrNull()?.let { newHeight ->
              size = (size as VideoSize.Custom).copy(height = newHeight)
            }
          },
          keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
          ),
          maxLines = 1
        )
      }
    }

    Button(
      onClick = onStartRecordingClick
    ) {
      Text("Start Recording")
    }
  }
}

@Composable
private fun RecordingInProgress(
  state: State.Recording,
  onStopRecordingClick: () -> Unit,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text("Recording in progress…")
    Button(onClick = onStopRecordingClick) {
      Text("Stop Recording")
    }
    Divider()
    state.currentOutput?.let {
      Text(it)
    }
  }
}

@Composable
private fun Processing(message: String) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = message,
    )
  }
}

@Composable
private fun RecordingFinished(result: PipelineResult) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    result.steps.forEach { step ->
      Text(
        modifier = Modifier.padding(16.dp),
        text = "${step.key} => ${step.output?.absolutePath ?: step.error}",
        fontSize = 16.sp,
      )
    }
  }
}