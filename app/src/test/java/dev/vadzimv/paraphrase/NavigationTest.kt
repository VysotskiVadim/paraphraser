package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.Screen
import dev.vadzimv.paraphrase.navigation.selectNavigationState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NavigationTest {
    @Test
    fun `initial state`() {
        val store = createTestStore()
        assertEquals(Screen.Chat, store.state.selectNavigationState().currentScreen)
        assertFalse(store.state.selectNavigationState().handleBackButton)
    }

    @Test
    fun `go to settings screen and back`() {
        val store = createTestStore()
        store.dispatch(NavigationAction.OpenSettings)
        assertEquals(Screen.Settings, store.state.selectNavigationState().currentScreen)
        assertTrue(store.state.selectNavigationState().handleBackButton)
        store.dispatch(NavigationAction.Back)
        assertEquals(Screen.Chat, store.state.selectNavigationState().currentScreen)
        assertFalse(store.state.selectNavigationState().handleBackButton)
    }
}