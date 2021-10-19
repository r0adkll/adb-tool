package ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xml.sax.InputSource
import java.io.File
import java.io.IOException

@Composable
fun <T> AsyncImage(
  load: suspend () -> T,
  painterFor: @Composable (T) -> Painter,
  contentDescription: String,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Fit,
) {
  val image: T? by produceState<T?>(null) {
    value = withContext(Dispatchers.IO) {
      try {
        load()
      } catch (e: IOException) {
        e.printStackTrace()
        null
      }
    }
  }

  if (image != null) {
    Image(
      painter = painterFor(image!!),
      contentDescription = contentDescription,
      contentScale = contentScale,
      modifier = modifier
    )
  }
}

fun loadImageBitmap(file: File): ImageBitmap =
  file.inputStream().buffered().use {
    org.jetbrains.skija.Image.makeFromEncoded(it.readAllBytes()).asImageBitmap()
  }
