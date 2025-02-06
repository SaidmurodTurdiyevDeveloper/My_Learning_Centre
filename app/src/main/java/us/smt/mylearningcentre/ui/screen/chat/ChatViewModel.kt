package us.smt.mylearningcentre.ui.screen.chat

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.data.model.StudentData
import us.smt.mylearningcentre.domen.use_case.ChatUseCase
import us.smt.mylearningcentre.ui.utils.AppNavigator
import us.smt.mylearningcentre.ui.utils.BaseViewModel
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.NetworkError
import us.smt.mylearningcentre.util.ResponseResult
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    appNavigator: AppNavigator,
    private val useCase: ChatUseCase
) : BaseViewModel<ChatState, ChatIntent>(initializeData = ChatState(), appNavigator = appNavigator) {
    private var isContinue = true
    override fun onAction(intent: ChatIntent) {
        when (intent) {
            ChatIntent.Back -> back()
            is ChatIntent.SendMessage -> sendMessage(intent.message)
            is ChatIntent.ChangeText -> changeText(intent.text)
        }
    }

    init {
        getUser()
    }

    private fun changeText(text: String) = update(state = state.value.copy(text = TextFieldData(text = text)))

    private fun getUser() {
        useCase.getUser().onEach { result ->
            when (result) {
                is ResponseResult.Error -> {
                    if (result.error is NetworkError.InternetConnection<*>) {
                        val data = result.error.data
                        if (data != null && data is StudentData) {
                            update(state = state.value.copy(user = data))
                        } else {
                            update(state = state.value.copy(error = result.error))
                        }
                    } else {
                        update(state = state.value.copy(error = result.error))
                    }
                }

                is ResponseResult.Success -> {
                    update(state = state.value.copy(user = result.data))
                    loadChat()
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun loadChat() {
        viewModelScope.launch(Dispatchers.IO) {
            async {
                while (isContinue && state.value.user != null) {
                    useCase.getMessages(state.value.user!!.clubId).collectLatest { result ->
                        when (result) {
                            is ResponseResult.Error -> {
                                if (result.error is NetworkError.InternetConnection<*>) {
                                    val data = result.error.data
                                    if (data != null && data is List<*>) {
                                        val first = data.firstOrNull()
                                        if (first!=null&&first is MessageData) {
                                            update(state = state.value.copy(messages = data as List<MessageData>))
                                        }
                                    } else {
                                        update(state = state.value.copy(error = result.error))
                                    }
                                } else {
                                    update(state = state.value.copy(error = result.error))
                                }
                            }

                            is ResponseResult.Success -> {
                                if (state.value.messages != result.data) {
                                    withContext(Dispatchers.Main) {
                                        update(state = state.value.copy(messages = result.data))
                                    }
                                }
                            }
                        }
                    }
                    delay(1000)
                }
            }.join()
        }
    }

    override fun onCleared() {
        super.onCleared()
        isContinue = false
    }

    private fun sendMessage(message: String) {
        if (message.isBlank()) return
        useCase.sendMessage(message).onEach { result ->
            when (result) {
                is ResponseResult.Error -> update(state = state.value.copy(error = result.error))
                is ResponseResult.Success -> update(state = state.value.copy(text = TextFieldData()))
            }
        }.launchIn(viewModelScope)

    }
}