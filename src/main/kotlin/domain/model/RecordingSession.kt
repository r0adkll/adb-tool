package domain.model

import kotlinx.coroutines.flow.Flow

data class RecordingSession(
  val filename: String,
  val outputFlow: Flow<String>,
)

data class ShellCommandOutput(
  val stdout: String = "",
  val stderr: String = "",
  val exitCode: Int? = null,
)