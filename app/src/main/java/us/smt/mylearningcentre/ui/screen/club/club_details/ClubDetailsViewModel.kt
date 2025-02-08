package us.smt.mylearningcentre.ui.screen.club.club_details

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.domen.use_case.ClubDetailsUseCase
import us.smt.mylearningcentre.ui.screen.application_form.CreateApplicationScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import javax.inject.Inject

@HiltViewModel
class ClubDetailsViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: ClubDetailsUseCase
) :
    BaseViewModel<ClubDetailsState, ClubDetailsIntent>(initializeData = ClubDetailsState(), appNavigator = appNavigator) {
    private var clubId: String = ""

    override fun onAction(intent: ClubDetailsIntent) {
        when (intent) {
            ClubDetailsIntent.JoinClub -> joinClub()
            ClubDetailsIntent.Back -> back()
            is ClubDetailsIntent.Init -> getClubInformation(intent.clubId)
        }
    }

    private fun getClubInformation(clubId: String) {
        this.clubId = clubId
        useCase.getClubDetails(clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> {
                    if (result.error is NetworkError.InternetConnection<*>) {
                        val data = result.error.data
                        if (data != null && data is ClubDetailsData) {
                            update(state = state.value.copy(club = data, isLoading = false))
                        } else {
                            update(state = state.value.copy(error = result.error, isLoading = false))
                        }
                    } else {
                        update(state = state.value.copy(error = result.error, isLoading = false))
                    }
                }

                is ResponseResult.Success -> {
                    update(state = state.value.copy(club = result.data, isLoading = false))
                    getClubPresident(result.data.presidentId)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getClubMembers() {
        useCase.getAllClubStudent(clubId = clubId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> {
                    if (result.error is NetworkError.InternetConnection<*>) {
                        val data = result.error.data
                        if (data != null && data is List<*>) {
                            val first = data.firstOrNull()
                            if (first != null && first is StudentData) {
                                update(state = state.value.copy(clubMembers = data as List<StudentData>, isLoading = false))
                            }
                        } else {
                            update(state = state.value.copy(error = result.error, isLoading = false))
                        }
                    } else {
                        update(state = state.value.copy(error = result.error, isLoading = false))
                    }
                }

                is ResponseResult.Success -> {
                    update(state = state.value.copy(clubMembers = result.data, isLoading = false))
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getClubPresident(presidentId: String) {
        useCase.getStudent(studentId = presidentId).onEach { result ->
            when (result) {
                is ResponseResult.Error -> {
                    if (result.error is NetworkError.InternetConnection<*>) {
                        val data = result.error.data
                        if (data != null && data is StudentData) {
                            update(state = state.value.copy(president = data, isLoading = false))
                        } else {
                            update(state = state.value.copy(error = result.error, isLoading = false))
                        }
                    } else {
                        update(state = state.value.copy(error = result.error, isLoading = false))
                    }
                }

                is ResponseResult.Success -> {
                    update(state = state.value.copy(president = result.data, isLoading = false))
                    getClubMembers()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun joinClub() {
        navigate(CreateApplicationScreen(clubId = clubId, clubName = state.value.club?.name ?: "unknown"))
    }

}