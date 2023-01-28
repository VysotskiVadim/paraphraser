package dev.vadzimv.paraphrase.redux

import org.junit.Assert
import org.junit.Test

class CombineReducersTest {
    @Test
    fun `combine reducers`() {
        val reducer: Reducer<TestState> = combineReducers {
            reducer(
                ::reducer1,
                subStateSelector = { s -> s.subState1 },
                updater = { rootState, subState -> rootState.copy(subState1 = subState) }
            )
            reducer(
                ::reducer2,
                subStateSelector = { s -> s.subState2 },
                updater = { rootState, subState -> rootState.copy(subState2 = subState) }
            )
        }
        val testState = TestState(
            subState1 = SubState1(1),
            subState2 = SubState2("test")
        )

        val newState = reducer(reducer(testState, Add1Action), ToUpperCaseAction)

        Assert.assertEquals(2, newState.subState1.test)
        Assert.assertEquals("TEST", newState.subState2.test)
    }
}