package us.smt.mylearningcentre.ui.screen.application_form

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.CreateApplicationFormData
import us.smt.mylearningcentre.domen.use_case.CreateApplicationUseCase
import us.smt.mylearningcentre.ui.screen.waiting.WaitingScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.ResponseResult
import us.smt.mylearningcentre.util.TextViewError
import javax.inject.Inject

@HiltViewModel
class CreateApplicationViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: CreateApplicationUseCase
) : BaseViewModel<CreateApplicationState, CreateApplicationIntent>(initializeData = CreateApplicationState(), appNavigator = appNavigator) {
    override fun onAction(intent: CreateApplicationIntent) {
        when (intent) {
            is CreateApplicationIntent.Save -> save(intent.clubId)
            CreateApplicationIntent.Back -> back()
            is CreateApplicationIntent.ChangeDescription -> changeDescription(intent.description)
        }
    }

    private fun changeDescription(description: String) {
        update(state = state.value.copy(description = TextFieldData(text = description, success = description.isBlank().not())))
    }

    private fun save(clubId: String) {
        if (!state.value.description.success) {
            update(state = state.value.copy(description = state.value.description.copy(error = TextViewError.Empty)))
            return
        }
        useCase.createApplicationToJoinClub(
            data = CreateApplicationFormData(
                clubId = clubId,
                description = state.value.description.text
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    navigate(WaitingScreen())
                }
            }
        }.launchIn(viewModelScope)
    }

}