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
        val store = Store(TestState(subState1 = SubState1(1)), combineTestReducers())
        store.addMiddleware { store ->
            { next ->
                { action ->
                    next.dispatch(action)
                    next.dispatch(action)
                }
            }
        }
        store.dispatch(Add1Action)
        assertEquals(3, store.state.subState1.test)
    }

    @Test
    fun `middleware stops events`() {
        val store = Store(TestState(subState1 = SubState1(1)), combineTestReducers())
        store.addMiddleware { store ->
            { _ ->
                { _ ->
                }
            }
        }
        store.dispatch(Add1Action)
        assertEquals(1, store.state.subState1.test)
    }
}

