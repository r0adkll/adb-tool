package ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyState(
  icon: ImageVector,
  text: String,
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Icon(
      icon,
      contentDescription = null,
      tint = Color.Black.copy(alpha = .27f),
      modifier = Modifier.size(48.dp)
    )
    Text(
      text,
      color = Color.Black.copy(alpha = .27f),
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium,
      textAlign = TextAlign.Center,
      modifier = Modifier
        .padding(
          top = 16.dp,
          start = 16.dp,
          end = 16.dp,
        )
    )
  }
}