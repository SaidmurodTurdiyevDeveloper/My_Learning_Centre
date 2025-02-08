package us.smt.mylearningcentre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import us.smt.mylearningcentre.data.database.local.shared.LocalStorage
import us.smt.mylearningcentre.data.database.remote.FireBaseHelper
import us.smt.mylearningcentre.ui.screen.splash.SplashScreen
import us.smt.mylearningcentre.ui.theme.MyLearningCentreTheme
import us.smt.mylearningcentre.ui.utils.AppNavigator
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        var changeDayNightMode: MutableSharedFlow<Boolean>? = null
    }

    @Inject
    lateinit var localStorage: LocalStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        changeDayNightMode = MutableSharedFlow()
        FireBaseHelper.inject(
            Firebase.firestore(FirebaseApp.getInstance()),
            FirebaseAuth.getInstance(FirebaseApp.getInstance())
        )
        setContent {
            var state by remember {
                mutableStateOf(localStorage.isDarkTheme)
            }

            LaunchedEffect(Unit) {
                changeDayNightMode?.collect { state = it }
            }

            MyLearningCentreTheme(
                darkTheme = state,
                dynamicColor = false
            ) {
                Navigator(SplashScreen()) { navigate ->
                    LaunchedEffect(Unit) {
                        AppNavigator.navigatorState.collect { it.invoke(navigate) }
                    }
                    CurrentScreen()
                }
            }

        }
    }
}
