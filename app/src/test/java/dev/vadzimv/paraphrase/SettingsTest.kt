package dev.vadzimv.paraphrase

import dev.vadzimv.paraphrase.doubles.FakeKeyValueStorage
import dev.vadzimv.paraphrase.settings.SettingsAction
import dev.vadzimv.paraphrase.settings.chatSettingsSelector
import dev.vadzimv.paraphrase.settings.settingsUiStateSelector
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SettingsTest {
    @Test
    fun `initial token value`() {
        val store = createTestStore()
        assertEquals(BuildConfig.OPEN_AI_TOKEN, store.state.chatSettingsSelector().openAIToken)
        assertEquals(BuildConfig.OPEN_AI_TOKEN, store.state.settingsUiStateSelector().accessToken.value)
    }

    @Test
    fun `user updates token`() {
        val store = createTestStore()
        val testNewToken = "test123"

        store.dispatch(SettingsAction.TokenUpdated(testNewToken))
        assertEquals(testNewToken, store.state.settingsUiStateSelector().accessToken.value)
        assertNotEquals(testNewToken, store.state.chatSettingsSelector().openAIToken)

        store.dispatch(SettingsAction.Save)
        assertEquals(testNewToken, store.state.chatSettingsSelector().openAIToken)
    }

    @Test
    fun `user updates token and restarts app`() {
        val storage = FakeKeyValueStorage()
        val store = createTestStore(keyValueStorage = storage)
        val testNewToken = "test123"
        store.dispatch(SettingsAction.TokenUpdated(testNewToken))
        store.dispatch(SettingsAction.Save)

        val newStore = createTestStore(keyValueStorage = storage)
        assertEquals(testNewToken, newStore.state.settingsUiStateSelector().accessToken.value)
        assertEquals(testNewToken, newStore.state.chatSettingsSelector().openAIToken)
    }
}
