package dev.vadzimv.paraphrase

import org.junit.Assert.assertNotEquals
import org.junit.Ignore
import org.junit.Test

class SettingsTest {
    @Test
    @Ignore
    fun `initial state`() {
        val store = createTestStore()
        assertNotEquals("", store.state.settingsState.chatSettings.openAIToken)
    }
}
