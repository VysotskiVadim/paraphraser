package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.BuildConfig
import dev.vadzimv.paraphrase.ChatSettings
import dev.vadzimv.paraphrase.KeyValueStorage

fun AppState.chatSettingsSelector() = settingsState.chatSettings

fun createSettingsInitialState(keyValueStorage: KeyValueStorage): SettingsState {
    val chatSettings = ChatSettings(openAIToken = keyValueStorage.read("accessToken") ?: BuildConfig.OPEN_AI_TOKEN)
    return SettingsState(
        chatSettings,
        chatSettings.toSettingsUIState()
    )
}

data class SettingsState(
    val chatSettings: ChatSettings,
    val settingsUIState: SettingsUIState
)

data class SettingsUIState(
    val accessToken: SettingFiled
)

data class SettingFiled(
    val value: String
)

fun previewSettingsUIState() = SettingsUIState(
    SettingFiled("test")
)

private fun ChatSettings.toSettingsUIState() = SettingsUIState(
    SettingFiled(openAIToken)
)