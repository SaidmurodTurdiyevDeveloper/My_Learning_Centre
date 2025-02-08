package us.smt.mylearningcentre.ui.screen.setting.setting_tab

import us.smt.mylearningcentre.data.model.ClubData
import us.smt.mylearningcentre.data.model.StudentData

data class SettingState(
    val isPresident: Boolean = false,
    val isNight: Boolean = false,
    val clubData: ClubData? = null,
    val isOpenChangePresidentDialog: Boolean = false,
    val isLavingClub: Boolean = false,
    val members: List<StudentData> = emptyList(),
    val user: StudentData? = null
)
