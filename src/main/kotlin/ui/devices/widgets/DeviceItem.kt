package ui.devices.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import domain.model.DeviceWithProps

@Composable
fun DeviceItem(
  deviceWithProps: DeviceWithProps,
  isSelected: Boolean = false,
  onDeviceClick: () -> Unit = {},
) {
  Row(
    Modifier
      .clickable { onDeviceClick() }
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = deviceWithProps.title,
      modifier = Modifier
        .weight(1f)
    )
    RadioButton(
      modifier = Modifier
        .padding(start = 16.dp),
      selected = isSelected,
      onClick = { onDeviceClick() }
    )
  }
}