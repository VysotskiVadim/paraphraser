package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.TestStore
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.NavigationSlice
import dev.vadzimv.paraphrase.navigation.NavigationState
import dev.vadzimv.paraphrase.navigation.Screen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Ignore
import org.junit.Test

class NavigationTest {
    @Test
    fun `initial state`() {
        val store = createTestNavigationSlice().toStore()
        assertEquals(Screen.Main, store.state.value.currentScreen)
        assertFalse(store.state.value.handleBackButton)
    }

    @Test
    fun `go to settings screen and back`() {
        val store = createTestNavigationSlice().toStore()
        store.processAction(NavigationAction.OpenSettings)
        assertEquals(Screen.Settings, store.state.value.currentScreen)
        assertTrue(store.state.value.handleBackButton)
        store.processAction(NavigationAction.Back)
        assertEquals(Screen.Main, store.state.value.currentScreen)
        assertFalse(store.state.value.handleBackButton)
    }
}

fun NavigationSlice.toStore() = TestStore<NavigationState, NavigationAction>(
    navigationScreenSlice = this
) { it.navigationState }