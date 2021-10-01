package ui.devices

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import di.LocalApplication
import domain.model.DeviceWithProps
import ui.devices.widgets.DeviceItem
import ui.widgets.EmptyState
import ui.widgets.Loading

@Composable
fun Devices(
  modifier: Modifier = Modifier
) {
  val app = LocalApplication.current
  val presenter = remember {
    DevicesPresenter(app.selectedDevice, app.deviceRepository)
  }
  DisposableEffect(Unit) {
    presenter.attach()
    presenter.loadDevices()
    onDispose {
      presenter.detach()
    }
  }

  val state = presenter.state.collectAsState()
  val selectedDevice = presenter.selectedDevice.collectAsState()

  Column(modifier) {
    TopAppBar(
      title = {
        Text(
          text = "Devices",
          fontSize = 20.sp,
          fontWeight = FontWeight.Medium,
        )
      },
      elevation = 0.dp,
      contentColor = Color.Black.copy(alpha = 0.87f),
      backgroundColor = Color.Transparent,
      actions = {
        IconButton(
          onClick = { presenter.loadDevices() }
        ) {
          Icon(
            Icons.Rounded.Refresh,
            contentDescription = null,
          )
        }
      }
    )
    Divider()

    when (val s = state.value) {
      State.Empty -> EmptyState(
        icon = Icons.Rounded.Smartphone,
        text = "No devices found! Check your USB connections and try again.",
      )
      State.Loading -> Loading()
      is State.Devices -> {
        DeviceList(
          state = s,
          selectedDevice = selectedDevice,
          onDeviceClick = {
            presenter.selectDevice(it)
          }
        )
      }
    }
  }
}

@Composable
private fun DeviceList(
  state: State.Devices,
  selectedDevice: androidx.compose.runtime.State<DeviceWithProps?>,
  onDeviceClick: (DeviceWithProps) -> Unit = { },
) {
  LazyColumn {
    items(state.devices) { device ->
      DeviceItem(
        deviceWithProps = device,
        isSelected = device.device.serial == selectedDevice.value?.device?.serial,
        onDeviceClick = { onDeviceClick(device) },
      )
    }
  }
}