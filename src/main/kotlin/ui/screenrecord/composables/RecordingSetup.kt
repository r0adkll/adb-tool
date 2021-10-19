package ui.screenrecord.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import domain.model.VideoSize

@Composable
fun RecordingSetup(
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