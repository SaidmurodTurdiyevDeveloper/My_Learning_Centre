package us.smt.mylearningcentre.ui.screen.task

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.CreateTaskData
import us.smt.mylearningcentre.domen.use_case.CreateTaskUseCase
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.ResponseResult
import us.smt.mylearningcentre.util.TextViewError
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: CreateTaskUseCase
) :
    BaseViewModel<CreateTaskState, CreateTaskIntent>(initializeData = CreateTaskState(), appNavigator = appNavigator) {
    override fun onAction(intent: CreateTaskIntent) {
        when (intent) {
            CreateTaskIntent.CreateTask -> createTask()
            CreateTaskIntent.Back -> back()
            is CreateTaskIntent.TitleChanged -> changeTitle(intent.value)
            is CreateTaskIntent.DescriptionChanged -> changeDescription(intent.value)
        }
    }

    private fun changeDescription(description: String) {
        update(state = state.value.copy(description = TextFieldData(text = description, success = description.isBlank().not())))
    }

    private fun changeTitle(title: String) {
        update(
            state = state.value.copy(
                taskTitle = TextFieldData(
                    text = title,
                    success = title.isBlank().not()
                )
            )
        )
    }

    private fun createTask() {
        if (!state.value.taskTitle.success) {
            update(state = state.value.copy(taskTitle = state.value.taskTitle.copy(error = TextViewError.Empty)))
            return
        }
        if (!state.value.description.success) {
            update(state = state.value.copy(description = state.value.description.copy(error = TextViewError.Empty)))
            return
        }
        update(state = state.value.copy(isLoading = true))
        useCase.addTask(
            data = CreateTaskData(
                title = state.value.taskTitle.text,
                description = state.value.description.text
            )
        ).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(
                    state = state.value.copy(
                        error = result.error,
                        isLoading = false
                    )
                )

                is ResponseResult.Success -> {
                    back()
                }
            }
        }.launchIn(viewModelScope)
    }

}