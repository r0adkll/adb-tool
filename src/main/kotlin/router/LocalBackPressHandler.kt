package router

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf

val LocalBackPressHandler: ProvidableCompositionLocal<BackPressHandler> =
        compositionLocalOf { throw IllegalStateException("backPressHandler is not initialized") }

class BackPressHandler(
    val id: String = "Root"
) {
    var children = mutableListOf<() -> Boolean>()

    fun handle(): Boolean =
        children.reversed().any { it() }
}
