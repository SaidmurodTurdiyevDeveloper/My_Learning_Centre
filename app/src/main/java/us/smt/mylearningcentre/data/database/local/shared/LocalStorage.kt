package us.smt.mylearningcentre.data.database.local.shared

import android.app.Application
import javax.inject.Inject

class LocalStorage @Inject constructor(
    application: Application
) {
    private val sharedPreferences = application.getSharedPreferences(
        "my_learning_centre",
        0
    )
    var isLoggedIn: Boolean by BooleanPreference(sharedPreferences, false)
    var isUserCreated: Boolean by BooleanPreference(sharedPreferences, false)
    var userId: String by StringPreference(sharedPreferences, "")
    var clubId: String by StringPreference(sharedPreferences, "")
    var fcmToken: String by StringPreference(sharedPreferences, "")
    var email: String by StringPreference(sharedPreferences, "")
    var userName: String by StringPreference(sharedPreferences, "")
    var userSurname: String by StringPreference(sharedPreferences, "")
    var waitingApplicationId: String by StringPreference(sharedPreferences, "")
}
