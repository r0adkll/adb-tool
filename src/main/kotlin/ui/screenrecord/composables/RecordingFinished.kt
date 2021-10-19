package ui.screenrecord.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.widgets.AsyncImage
import ui.widgets.loadImageBitmap
import videoprocessing.PipelineResult
import videoprocessing.processors.CompressionProcessor
import videoprocessing.processors.GifProcessor
import java.awt.Desktop
import java.io.File

@Composable
fun RecordingFinished(result: PipelineResult) {
  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    // Find gif
    val gifResult = result.stepFor(GifProcessor.KEY)
    when {
      gifResult.output != null -> {
        GifImage(
          file = gifResult.output,
          modifier = Modifier
            .weight(1f)
        )
        result.steps.forEach {
          FileOutput(
            modifier = Modifier
              .clickable {
                it.output?.let { output ->
                  Desktop.getDesktop().browseFileDirectory(output)
                }
              },
            result = it
          )
        }
      }
      else -> {
        Box(
          modifier = Modifier
            .weight(1f)
            .fillMaxSize(),
          contentAlignment = Alignment.Center,
        ) {
          Text(
            text = "Unable to create gif",
            fontSize = 16.sp,
            color = Color.Red
          )
        }
      }
    }
  }
}

@Composable
fun FileOutput(
  modifier: Modifier,
  result: PipelineResult.Step,
) {
  Column(
    modifier = modifier
      .padding(vertical = 8.dp, horizontal = 16.dp)
      .wrapContentWidth()
      .background(
        color = Color.Black.copy(alpha = .12f),
        shape = RoundedCornerShape(8.dp)
      )
      .padding(16.dp),
    verticalArrangement = Arrangement.Center,
  ) {
    Text(
      text = when (result.key) {
        GifProcessor.KEY -> "GIF"
        CompressionProcessor.KEY -> "COMPRESSED"
        else -> "ORIGINAL"
      },
      fontSize = 12.sp,
      color = Color.Black,
      fontWeight = FontWeight.SemiBold,
    )
    Text(
      modifier = Modifier.padding(top = 4.dp),
      text = when {
        result.output != null -> result.output.path
        result.error != null -> result.error
        else -> "N/A"
      },
      fontSize = 16.sp,
      color = Color.Black.copy(.56f),
    )
  }
}

@Composable
fun GifImage(
  file: File,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .then(modifier),
    contentAlignment = Alignment.Center,
  ) {
    AsyncImage(
      modifier = Modifier.padding(16.dp),
      load = { loadImageBitmap(file) },
      painterFor = { remember { BitmapPainter(it) } },
      contentDescription = "Recording GIF",
    )
  }
}