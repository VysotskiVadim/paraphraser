package dev.vadzimv.paraphrase.redux

import org.junit.Assert.assertEquals
import org.junit.Test

internal class StoreTest {

    @Test
    fun `reducer updates state`() {
        val store = Store(TestState(subState1 = SubState1(1)), combineTestReducers())
        store.dispatch(Add1Action)
        assertEquals(2, store.state.subState1.test)
    }

    @Test
    fun `middleware transforms events`() {
        val store = Store(
            initialState = TestState(subState1 = SubState1(1)),
            reducer = combineTestReducers(),
            middlewares = listOf { _ ->
                { next ->
                    { action ->
                        next(action)
                        next(action)
                    }
                }
            }
        )
        store.dispatch(Add1Action)
        assertEquals(3, store.state.subState1.test)
    }

    @Test
    fun `middleware stops events`() {
        val store = Store(
            TestState(subState1 = SubState1(1)),
            combineTestReducers(),
            middlewares = listOf(createAllEventsFilteringMiddleware())
        )
        store.dispatch(Add1Action)
        assertEquals(1, store.state.subState1.test)
    }

    @Test
    fun `logging happens before filtering`() {
        val loggedActions = mutableListOf<Action>()
        val store = Store(
            TestState(subState1 = SubState1(0)),
            combineTestReducers(),
            middlewares = listOf(
                { _ ->
                    { next ->
                        { action ->
                            loggedActions.add(action)
                            next(action)
                        }
                    }
                },
                createAllEventsFilteringMiddleware()
            )
        )

        store.dispatch(Add1Action)
        store.dispatch(ToUpperCaseAction)

        assertEquals(0, store.state.subState1.test)
        assertEquals(listOf(Add1Action, ToUpperCaseAction), loggedActions)
    }

    private fun createAllEventsFilteringMiddleware() = { _: Store<TestState> ->
        { _ : Dispatcher ->
            { _ : Action ->
            }
        }
    }
}

