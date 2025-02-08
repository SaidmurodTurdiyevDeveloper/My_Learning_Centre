package us.smt.mylearningcentre.ui.screen.setting.setting_tab

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import us.smt.mylearningcentre.MainActivity
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.ui.screen.setting.about.AboutScreen
import us.smt.mylearningcentre.ui.screen.setting.help.HelpScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    navigator: AppNavigator,
    private val localStorage: LocalStorage
) : BaseViewModel<SettingState, SettingIntent>(SettingState(), navigator) {

    override fun onAction(intent: SettingIntent) {
        when (intent) {
            SettingIntent.OpenAbout -> openAbout()
            SettingIntent.OpenHelp -> openHelp()
            SettingIntent.Leave -> leaveClub()
            is SettingIntent.ChangePresident -> changePresident()
            SettingIntent.CloseChangePresident -> closeChangePresident()
            SettingIntent.OpenChangePresident -> openChangePresident()
            is SettingIntent.ChangeDarkMode -> changeDarkMode(intent.isDark)
        }
    }

    init {
        update(state = state.value.copy(isNight = localStorage.isDarkTheme))
        Log.d("TagTagTag", "username: ${localStorage.userName}")
    }

    private fun leaveClub() {

    }

    private fun changePresident() {

    }

    private fun openChangePresident() {

    }

    private fun closeChangePresident() {

    }

    private fun changeDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            localStorage.isDarkTheme = isDark
            update(state = state.value.copy(isNight = isDark))
            MainActivity.changeDayNightMode?.emit(isDark)
        }
    }

    private fun openAbout() {
        navigate(AboutScreen())
    }

    private fun openHelp() {
        navigate(HelpScreen())
    }

}