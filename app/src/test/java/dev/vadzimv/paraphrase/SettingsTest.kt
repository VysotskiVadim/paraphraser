package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.settings.SettingsAction
import dev.vadzimv.paraphrase.settings.SettingsSlice
import dev.vadzimv.paraphrase.settings.SettingsState
import dev.vadzimv.paraphrase.settings.createSettingsSlice
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SettingsTest {
    @Test
    fun `initial state`() {
        val store = createSettingsSlice().toStore()
        assertNotEquals("", store.state.value.chatSettings.openAIToken)
    }
}

fun SettingsSlice.toStore() = TestStore<SettingsState, SettingsAction>(
    settingsSlice = this
) {
    it.settingsState
}