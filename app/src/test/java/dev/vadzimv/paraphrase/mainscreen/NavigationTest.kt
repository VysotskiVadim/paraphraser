package dev.vadzimv.paraphrase.mainscreen

import dev.vadzimv.paraphrase.TestStore
import dev.vadzimv.paraphrase.navigation.NavigationAction
import dev.vadzimv.paraphrase.navigation.NavigationSlice
import dev.vadzimv.paraphrase.navigation.NavigationState
import dev.vadzimv.paraphrase.navigation.Screen
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

class NavigationTest {
    @Test
    fun `initial state`() {
        val store = createTestNavigationSlice().toStore()
        assertEquals(Screen.Main, store.state.value.currentScreen)
    }

    @Test
    @Ignore("TODO: implement")
    fun `go to settings screen `() {
        val store = createTestNavigationSlice().toStore()
        store.processAction(NavigationAction.OpenSettings)
        assertEquals(Screen.Settings, store.state.value.currentScreen)
    }
}

fun NavigationSlice.toStore() = TestStore<NavigationState, NavigationAction>(
    navigationScreenSlice = this
) { it.navigationState }