package data

import com.malinskiy.adam.extension.readProtocolString
import com.malinskiy.adam.request.ComplexRequest
import com.malinskiy.adam.request.HostTarget
import com.malinskiy.adam.request.device.Device
import com.malinskiy.adam.request.device.DeviceState
import com.malinskiy.adam.transport.Socket
import domain.model.DeviceWithProps

class CustomListDevicesRequest : ComplexRequest<List<DeviceWithProps>>(target = HostTarget) {

  override fun serialize() = createBaseRequest("devices-l")

  override suspend fun readElement(socket: Socket): List<DeviceWithProps> {
    return socket.readProtocolString().lines()
      .filter { it.isNotEmpty() }
      .map {
        val line = it.trim()
        val parts = line.split(Regex("\\s+"))

        DeviceWithProps(
          Device(
            serial = parts[0],
            state = DeviceState.from(parts[1]),
          ),
          parts.subList(2, parts.size).map {
            val split = it.split(":")
            split[0] to split[1]
          }.toMap()
        )
      }
  }
}