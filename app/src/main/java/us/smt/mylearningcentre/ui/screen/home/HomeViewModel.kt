package us.smt.mylearningcentre.ui.screen.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.domen.use_case.HomeUseCase
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.util.ResponseResult
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    navigator: AppNavigator,
    private val useCase: HomeUseCase
) : BaseViewModel<HomeState, HomeIntent>(HomeState(), navigator) {

    override fun onAction(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Init -> initData()
            HomeIntent.OpenAddTaskScreen -> {}
        }
    }

    init {
        initData()
    }

    private fun initData() {
        useCase.getUser().onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(user = result.data))
                    if (result.data.isPresident) {
                        getApplicationForm(clubId = result.data.clubId)
                    } else {
                        getTasks(clubId = result.data.clubId)
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getTasks(clubId: String) {
        useCase.getAllClubTasks(clubId = clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(tasks = result.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getApplicationForm(clubId: String) {
        useCase.getClubApplications(clubId = clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> {
                    update(state = state.value.copy(appForm = result.data))
                    getTasks(clubId = clubId)
                }
            }
        }.launchIn(viewModelScope)
    }
}