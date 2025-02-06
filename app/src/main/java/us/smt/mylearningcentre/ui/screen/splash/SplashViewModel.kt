package us.smt.mylearningcentre.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.ui.screen.auth.login.LoginScreen
import us.smt.mylearningcentre.ui.screen.club.club_list.ClubListScreen
import us.smt.mylearningcentre.ui.screen.tab.MainTabScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val navigator: AppNavigator, private val localStorage: LocalStorage) : ViewModel() {
    fun start() {
        if (localStorage.isLoggedIn) {
            if (localStorage.clubId.isBlank()) {
                viewModelScope.launch {
                    delay(100)
                    navigator.replace(ClubListScreen())
                }
            } else {
                viewModelScope.launch {
                    delay(100)
                    navigator.replace(MainTabScreen())
                }
            }
        } else {
            viewModelScope.launch {
                delay(100)
                navigator.replace(LoginScreen())
            }
        }
    }
}