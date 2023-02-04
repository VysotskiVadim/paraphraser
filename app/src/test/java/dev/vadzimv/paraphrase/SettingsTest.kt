package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.settings.SettingsAction
import dev.vadzimv.paraphrase.settings.chatSettingsSelector
import dev.vadzimv.paraphrase.settings.settingsUiStateSelector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SettingsTest {
    @Test
    fun `user updates and saves token`() {
        val store = createTestStore()
        val testNewToken = "test123"

        store.dispatch(SettingsAction.TokenUpdated(testNewToken))
        assertEquals(testNewToken, store.state.settingsUiStateSelector().accessToken.value)
        assertNotEquals(testNewToken, store.state.chatSettingsSelector().openAIToken)

        store.dispatch(SettingsAction.Save)
        assertEquals(testNewToken, store.state.chatSettingsSelector().openAIToken)
    }
}
