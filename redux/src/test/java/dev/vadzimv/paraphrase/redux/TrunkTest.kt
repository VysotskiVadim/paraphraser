package dev.vadzimv.paraphrase.redux

import org.junit.Assert.assertEquals
import org.junit.Test

class TrunkTest {

    @Test
    fun `trunk action dispatches new actions`() {
        val store = Store(
            TestState(subState1 = SubState1(0)),
            combineTestReducers(),
            listOf(trunkMiddleware())
        )

        store.dispatch(trunk<TestState> { dispatch, getState ->
            dispatch(Add1Action)
            dispatch(Add1Action)
        })

        assertEquals(2, store.state.subState1.test)
    }

    @Test
    fun `trunk actions are processed by other middlewares`() {
        val testAction = object : Action {}
        val triggerIncrementActionOnTestAction =
            middleware { _: Store<TestState>, next: Dispatcher, action: Action ->
                if (action == testAction) {
                    next(Add1Action)
                }
                next(action)
            }
        val store = Store(
            TestState(subState1 = SubState1(0)),
            combineTestReducers(),
            listOf(
                triggerIncrementActionOnTestAction,
                trunkMiddleware(),
                triggerIncrementActionOnTestAction
            )
        )

        store.dispatch(trunk<TestState> { dispatch, _ ->
            dispatch(testAction)
        })

        assertEquals(2, store.state.subState1.test)
    }
}

