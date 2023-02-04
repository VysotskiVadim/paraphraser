package dev.vadzimv.paraphrase.settings

import dev.vadzimv.paraphrase.AppState
import dev.vadzimv.paraphrase.ChatSettings

fun AppState.chatSettingsSelector() = settingsState.chatSettings

fun createSettingsInitialState() = SettingsState(ChatSettings(), defaultSettingsUiState())

data class SettingsState(
    val chatSettings: ChatSettings,
    val settingsUIState: SettingsUIState
)

data class SettingsUIState(
    val accessToken: SettingFiled,
    val temperature: SettingFiled,
    val maxTokens: SettingFiled
)

data class SettingFiled(
    val value: String
)

fun defaultSettingsUiState() = SettingsUIState(
    SettingFiled("test"), SettingFiled("0.5"), SettingFiled("200")
)