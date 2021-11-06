package ui.screenrecord

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import di.LocalApplication
import router.BackStack
import ui.Routing
import ui.screenrecord.composables.RecordingFinished
import ui.screenrecord.composables.RecordingProcessing
import ui.screenrecord.composables.RecordingProgress
import ui.screenrecord.composables.RecordingSetup
import ui.widgets.EmptyState

@Composable
fun ScreenRecordScreen(backStack: BackStack<Routing>) {
  val app = LocalApplication.current
  val presenter = remember {
    ScreenRecordPresenter(app)
  }
  presenter.bindToComposable()

  val state = presenter.state.collectAsState().value
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    TopAppBar(
      title = { Text("Record Video") },
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
    RecordContent(
      state = state,
      onStartRecordingClick = presenter::startRecording,
      onStopRecordingClick = presenter::stopRecording,
    )
  }
}

@Composable
private fun RecordContent(
  state: State,
  onStartRecordingClick: () -> Unit,
  onStopRecordingClick: () -> Unit,
) {
  when (state) {
    is State.Setup -> RecordingSetup(
      onStartRecordingClick = onStartRecordingClick,
    )
    is State.Error -> EmptyState(
      icon = Icons.Rounded.ErrorOutline,
      text = "Something went wrong with trying to record screen"
    )
    is State.Recording -> RecordingProgress(
      state = state,
      onStopRecordingClick = onStopRecordingClick,
    )
    is State.Processing -> RecordingProcessing(state.message)
    is State.Recorded -> RecordingFinished(state.result)
  }
}

