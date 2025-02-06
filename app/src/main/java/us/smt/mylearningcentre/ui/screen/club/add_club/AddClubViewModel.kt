package us.smt.mylearningcentre.ui.screen.club.add_club

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.ClubCategoryData
import us.smt.mylearningcentre.data.model.CreateUpdateClubData
import us.smt.mylearningcentre.domen.use_case.CreateNewClubUseCase
import us.smt.mylearningcentre.ui.screen.tab.MainTabScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.ResponseResult
import us.smt.mylearningcentre.util.TextViewError
import javax.inject.Inject

@HiltViewModel
class AddClubViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: CreateNewClubUseCase
) :
    BaseViewModel<AddClubState, AddClubIntent>(initializeData = AddClubState(), appNavigator = appNavigator) {
    override fun onAction(intent: AddClubIntent) {
        when (intent) {
            AddClubIntent.Save -> addClub()
            AddClubIntent.Back -> back()
            is AddClubIntent.ChangeClubCategory -> changeCategory(intent.category)
            is AddClubIntent.ChangeClubName -> changeClubName(intent.name)
            is AddClubIntent.ChangeDescription -> changeDescription(intent.description)
            AddClubIntent.CloseCreateNewClubCategoryDialog -> closeCreateNewClubCategoryDialog()
            is AddClubIntent.CreateNewClubCategory -> createNewClubCategory(intent.name)
            AddClubIntent.OpenCreateNewClubCategoryDialog -> openCreateNewClubCategoryDialog()
        }
    }

    init {
        getCategories()
    }

    private fun getCategories() {
        useCase.getClubCategoriesList().onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> update(state = state.value.copy(categoryList = result.data))
            }
        }.launchIn(viewModelScope)
    }

    private fun closeCreateNewClubCategoryDialog() {
        update(state = state.value.copy(isOpenCreateNewCategoryDialog = false))
    }

    private fun openCreateNewClubCategoryDialog() {
        update(state = state.value.copy(isOpenCreateNewCategoryDialog = true))
    }

    private fun createNewClubCategory(name: String) {
        useCase.createClubCategories(name).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(
                        state = state.value.copy(
                            isOpenCreateNewCategoryDialog = false,
                            categoryList = state.value.categoryList + ClubCategoryData(name = name, id = "")
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun changeClubName(name: String) {
        update(state = state.value.copy(clubName = TextFieldData(text = name, success = name.isBlank().not())))
    }

    private fun changeDescription(description: String) {
        update(state = state.value.copy(clubDescription = TextFieldData(text = description, success = description.isBlank().not())))
    }

    private fun changeCategory(category: ClubCategoryData) {
        update(
            state = state.value.copy(
                clubCategory = TextFieldData(
                    text = category.name,
                    success = category.name.isBlank().not()
                ),
                selectedCategory = category
            )
        )
    }

    private fun addClub() {
        if (!state.value.clubName.success) {
            update(state = state.value.copy(clubName = state.value.clubName.copy(error = TextViewError.Empty)))
            return
        }
        if (!state.value.clubDescription.success) {
            update(state = state.value.copy(clubDescription = state.value.clubDescription.copy(error = TextViewError.Empty)))
            return
        }
        if (!state.value.clubCategory.success) {
            update(state = state.value.copy(clubCategory = state.value.clubCategory.copy(error = TextViewError.Empty)))
            return
        }
        useCase.createClubDetails(
            data = CreateUpdateClubData(
                name = state.value.clubName.text,
                description = state.value.clubDescription.text,
                category = state.value.selectedCategory?.name ?: ""
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    navigate(MainTabScreen())
                }
            }
        }.launchIn(viewModelScope)
    }

}