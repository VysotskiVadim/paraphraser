package dev.vadzimv.paraphrase.redux

import org.junit.Assert
import org.junit.Test
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
data class SetTestValue(val value: String): Action
fun reducer2(state2: SubState2, action: Action) = when (action) {
    ToUpperCaseAction -> state2.copy(test = state2.test.uppercase(Locale.getDefault()))
    is SetTestValue -> state2.copy(test = action.value)
    else -> state2
}

fun combineTestReducers(): Reducer<TestState> =
    { state, action ->
        state.copy(
            subState1 = reducer1(state.subState1, action),
            subState2 = reducer2(state.subState2, action)
        )
    }

class CombineReducersTest {
    @Test
    fun `combine test reducers`() {
        val reducer: Reducer<TestState> = combineTestReducers()
        val testState = TestState(
            subState1 = SubState1(1),
            subState2 = SubState2("test")
        )

        val newState = reducer(reducer(testState, Add1Action), ToUpperCaseAction)

        Assert.assertEquals(2, newState.subState1.test)
        Assert.assertEquals("TEST", newState.subState2.test)
    }
}