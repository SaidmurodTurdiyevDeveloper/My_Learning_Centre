package us.smt.mylearningcentre.ui.screen.club.add_club

import us.smt.mylearningcentre.data.model.ClubCategoryData
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.UserError

data class AddClubState(
    val loading: Boolean = false,
    val error: UserError? = null,
    val categoryList: List<ClubCategoryData> = emptyList(),
    val selectedCategory: ClubCategoryData? = null,
    val isOpenCreateNewCategoryDialog: Boolean = false,
    val clubName: TextFieldData = TextFieldData(),
    val clubCategory: TextFieldData = TextFieldData(),
    val clubDescription: TextFieldData = TextFieldData()
)
