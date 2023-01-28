package dev.vadzimv.paraphrase.redux

import java.util.Locale

data class TestState(
    val subState1: SubState1 = SubState1(),
    val subState2: SubState2 = SubState2()
)

data class SubState1(val test: Int = 8)
object Add1Action: Action
fun reducer1(state: SubState1, action: Action) = when (action) {
    Add1Action -> state.copy(test = state.test + 1)
    else -> state
}

data class SubState2(val test: String = "test")
object ToUpperCaseAction: Action
fun reducer2(state2: SubState2, action: Action) = when (action) {
    ToUpperCaseAction -> state2.copy(test = state2.test.uppercase(Locale.getDefault()))
    else -> state2
}