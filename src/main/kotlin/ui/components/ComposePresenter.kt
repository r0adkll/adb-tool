package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.DisposableEffectScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent

abstract class ComposePresenter<State>(initialState: State) : KoinComponent {

  protected val _state = MutableStateFlow(initialState)
  val state: StateFlow<State> = _state

  protected lateinit var scope: CoroutineScope

  fun attach() {
    scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
  }

  fun detach() {
    scope.cancel()
  }

  @Composable
  fun BindToComposable(onBind: DisposableEffectScope.() -> Unit = { }) {
    DisposableEffect(Unit) {
      attach()
      onBind()
      onDispose {
        detach()
      }
    }
  }
}