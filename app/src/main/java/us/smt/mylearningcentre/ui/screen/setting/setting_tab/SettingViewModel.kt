package us.smt.mylearningcentre.ui.screen.setting.setting_tab

import dagger.hilt.android.lifecycle.HiltViewModel
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.ui.screen.setting.about.AboutScreen
import us.smt.mylearningcentre.ui.screen.setting.help.HelpScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(navigator: AppNavigator, localStorage: LocalStorage) : BaseViewModel<SettingState, SettingIntent>(SettingState(), navigator) {
    init {
    }

    override fun onAction(intent: SettingIntent) {
        when (intent) {
            SettingIntent.OpenAbout -> openAbout()
            SettingIntent.OpenHelp -> openHelp()
            SettingIntent.Leave -> TODO()
            is SettingIntent.ChangePresident -> TODO()
            SettingIntent.CloseChangePresident -> TODO()
            SettingIntent.OpenChangePresident -> TODO()
        }
    }

    private fun openAbout() {
        navigate(AboutScreen())
    }

    private fun openHelp() {
        navigate(HelpScreen())
    }

}