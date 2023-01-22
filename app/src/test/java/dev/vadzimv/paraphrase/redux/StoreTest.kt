package dev.vadzimv.paraphrase.redux

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StoreTest {
    @Test
    fun `create store from slices`() {
        val store = Store(TestState(subState1 = SubState1(1)))
        store.addSlice(
            ::reducer1,
            subStateSelector = { s -> s.subState1 },
            updater = { rootState, subState -> rootState.copy(subState1 = subState)}
        )
        store.dispatch(Add1Action)
        assertEquals(2, store.state.subState1.test)
    }
}

data class TestState(
    val subState1: SubState1 = SubState1()
)


data class SubState1(val test: Int = 8)
object Add1Action: Action
fun reducer1(state: SubState1, action: Action) = when (action) {
    Add1Action -> state.copy(test = state.test + 1)
    else -> state
}