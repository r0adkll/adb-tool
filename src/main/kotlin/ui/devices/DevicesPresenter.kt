package ui.devices

import domain.DeviceRepository
import domain.model.DeviceWithProps
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ui.components.ComposePresenter

class DevicesPresenter(
  private val selectedDeviceState: MutableStateFlow<DeviceWithProps?>,
  private val repository: DeviceRepository
) : ComposePresenter<State>(State.Loading) {

  val selectedDevice: StateFlow<DeviceWithProps?> = selectedDeviceState

  fun loadDevices() {
    scope.launch {
      _state.value = State.Loading
      val devices = repository.getDevices()
      if (devices.isEmpty()) {
        _state.value = State.Empty
      } else {
        _state.value = State.Devices(devices)
      }
    }
  }

  fun selectDevice(device: DeviceWithProps) {
    if (selectedDeviceState.value == device) {
      selectedDeviceState.value = null
    } else {
      selectedDeviceState.value = device
    }
  }
}

sealed class State {
  object Loading : State()
  object Empty : State()

  data class Devices(
    val devices: List<DeviceWithProps>
  ) : State()
}