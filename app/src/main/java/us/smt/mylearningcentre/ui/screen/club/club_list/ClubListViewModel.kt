package us.smt.mylearningcentre.ui.screen.club.club_list

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import us.smt.mylearningcentre.data.model.ClubData
import us.smt.mylearningcentre.domen.use_case.ClubListUseCase
import us.smt.mylearningcentre.ui.screen.club.add_club.AddNewClubScreen
import us.smt.mylearningcentre.ui.screen.club.club_details.ClubDetailsScreen
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import javax.inject.Inject

@HiltViewModel
class ClubListViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: ClubListUseCase
) :
    BaseViewModel<ClubListState, ClubListIntent>(initializeData = ClubListState(), appNavigator = appNavigator) {

    override fun onAction(intent: ClubListIntent) {
        when (intent) {
            ClubListIntent.Back -> back()
            ClubListIntent.Init -> init()
            ClubListIntent.OpenCreateClub -> openCreateClub()
            is ClubListIntent.ClubClick -> openClubDetails(intent.clubId, intent.clubName)
        }
    }

    private fun init() {
        useCase.getClubList().onEach { result ->
            when (result) {
                is ResponseResult.Error -> {
                    if (result.error is NetworkError.InternetConnection<*>) {
                        val data = result.error.data
                        if (data != null && data is List<*>) {
                            val first = data.firstOrNull()
                            if (first != null && first is ClubData) {
                                update(state = state.value.copy(list = data as List<ClubData>))
                            }
                        } else {
                            update(state = state.value.copy(error = result.error))
                        }
                    } else {
                        update(state = state.value.copy(error = result.error))
                    }
                }

                is ResponseResult.Success -> {
                    update(state = state.value.copy(list = result.data))
                }
            }
        }.launchIn(viewModelScope)
    }


    private fun openCreateClub() {
        navigate(AddNewClubScreen())
    }

    private fun openClubDetails(
        id: String,
        name: String
    ) {
        navigate(
            ClubDetailsScreen(
                clubId = id,
                clubName = name
            )
        )
    }

}