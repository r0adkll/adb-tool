package domain.model

import com.malinskiy.adam.request.device.Device

typealias Props = Map<String, String>

data class DeviceWithProps(
  val device: Device,
  val props: Props,
) {

  val title: String
    get() = props["model"]
      ?.replace("_", " ")
      ?: device.serial
}