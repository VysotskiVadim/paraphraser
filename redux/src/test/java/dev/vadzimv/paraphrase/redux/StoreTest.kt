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
                        next.dispatch(action)
                        next.dispatch(action)
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
            middlewares = listOf { _ ->
                { _ ->
                    { _ ->
                    }
                }
            })
        store.dispatch(Add1Action)
        assertEquals(1, store.state.subState1.test)
    }
}

