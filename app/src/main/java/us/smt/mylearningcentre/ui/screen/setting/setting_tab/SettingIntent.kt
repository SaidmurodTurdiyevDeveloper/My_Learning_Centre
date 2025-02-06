package us.smt.mylearningcentre.ui.screen.setting.setting_tab

sealed interface SettingIntent {
    data object OpenAbout : SettingIntent
    data object OpenHelp : SettingIntent
    data object Leave : SettingIntent
    data object OpenChangePresident : SettingIntent
    data object CloseChangePresident : SettingIntent
    data class ChangePresident(val presidentId: String) : SettingIntent
}