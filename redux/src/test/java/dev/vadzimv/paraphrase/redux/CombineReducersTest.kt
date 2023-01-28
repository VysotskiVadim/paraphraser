package dev.vadzimv.paraphrase.redux

import org.junit.Assert
import org.junit.Test

class CombineReducersTest {
    @Test
    fun `combine reducers`() {
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