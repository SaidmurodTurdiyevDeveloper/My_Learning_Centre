package us.smt.mylearningcentre.ui.screen.chat

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.data.model.MessageData
import us.smt.mylearningcentre.ui.utils.TextFieldData
import us.smt.mylearningcentre.util.getErrorMessage

class ChatScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<ChatViewModel>()
        val uiState by viewModel.state.collectAsState()
        ChatScreenContent(uiState = uiState, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreenContent(
    uiState: ChatState,
    onAction: (ChatIntent) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.error) {
        if (uiState.error != null) {
            Toast.makeText(context, getErrorMessage(uiState.error), Toast.LENGTH_LONG).show()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Club Chat") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ChatIntent.Back) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom,
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.messages) { item ->
                    ChatBubble(item.senderName, item.text, item.isYourMessage)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.text.text,
                    onValueChange = {
                        onAction(ChatIntent.ChangeText(it))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    placeholder = {
                        Text(
                            "Type a message...",
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedTextColor = MaterialTheme.colorScheme.outline
                    )
                )
                Button(
                    onClick = {
                        if (!uiState.isLoading)
                            onAction(ChatIntent.SendMessage(uiState.text.text))
                        else {
                            Toast.makeText(context, "Please wait...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Send", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }

}

@Composable
private fun ChatBubble(sender: String, message: String, isMe: Boolean) {
    val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Cyan, Color.Magenta)
    val senderInitial = sender.firstOrNull()?.uppercaseChar() ?: '?'
    val colorIndex = senderInitial.code % colors.size
    val senderColor = colors[colorIndex]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
    ) {
        if (!isMe) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(senderColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = senderInitial.toString(),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Column(modifier = Modifier.padding(start = if (isMe) 32.dp else 0.dp, end = if (isMe) 0.dp else 32.dp)) {
            Text(
                modifier = Modifier.align(if (isMe) Alignment.End else Alignment.Start),
                text = sender, fontSize = 12.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .background(
                        if (isMe) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message,
                    color = if (isMe) Color.White else Color.Black,
                    fontSize = 16.sp
                )
            }
        }
        if (isMe) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(senderColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = senderInitial.toString(),
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenContentPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ChatScreenContent(uiState = ChatState(
            messages = listOf(
                MessageData(
                    senderName = "Saidmurod",
                    text = "Salom",
                    isYourMessage = true,
                    senderId = "1",
                    id = "1",
                    clubId = "1"
                ),
                MessageData(
                    senderName = "Baxrom",
                    text = "ishlaring yaxshimi og`ayni",
                    isYourMessage = false,
                    senderId = "1",
                    id = "1",
                    clubId = "1"
                )
            ),
            text = TextFieldData(text = "Ha yaxshi", success = true)
        ), onAction = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatScreenContentPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ChatScreenContent(uiState = ChatState(
            messages = listOf(
                MessageData(
                    senderName = "Saidmurod",
                    text = "Salom",
                    isYourMessage = true,
                    senderId = "1",
                    id = "1",
                    clubId = "1"
                ),
                MessageData(
                    senderName = "Baxrom",
                    text = "ishlaring yaxshimi og`ayni, ko`rinmaysan",
                    isYourMessage = false,
                    senderId = "1",
                    id = "1",
                    clubId = "1"
                )
            ),
            text = TextFieldData(text = "Ha yaxshi", success = true)
        ), onAction = {})
    }
}

