package domain.model

data class RecordingRequest(
  val filename: String,
  val size: VideoSize = VideoSize.Original,
  val bitRate: Int? = null, // 4Mbps
  val timeLimitInSeconds: Int? = null,
  val rotate: Boolean = false,
  val verbose: Boolean = false,
) {

  fun buildCmd(): String = buildString {
    append("screenrecord ")
    if (verbose) {
      append("--verbose ")
    }
    if (rotate) {
      append("--rotate ")
    }
    if (size is VideoSize.Custom) {
      append("--size ${size.width}x${size.height} ")
    }
    if (bitRate != null) {
      append("--bit-rate $bitRate ")
    }
    if (timeLimitInSeconds != null) {
      append("--time-limit ${timeLimitInSeconds.coerceIn(0..180)} ")
    }
    append("/sdcard/$filename")
  }
}

sealed class VideoSize {
  object Original : VideoSize()
  data class Custom(
    val width: Int,
    val height: Int,
  ) : VideoSize() {

    companion object {
      val DEFAULT = Custom(1280, 720)
    }
  }
}