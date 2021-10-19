package ui.screenrecord.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.screenrecord.State

@Composable
fun RecordingProgress(
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