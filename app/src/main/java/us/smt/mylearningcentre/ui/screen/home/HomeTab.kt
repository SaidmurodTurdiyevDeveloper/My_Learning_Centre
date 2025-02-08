package us.smt.mylearningcentre.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import us.smt.mylearningcentre.data.model.ApplicationFormData
import us.smt.mylearningcentre.data.model.TaskData

class HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Home",
            icon = rememberVectorPainter(Icons.Default.Home)
        )

    @Composable
    override fun Content() {
        val viewModel = getViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()
        LaunchedEffect(key1 = Unit) {
            viewModel.onAction(HomeIntent.Init)
        }
        HomContent(uiState = state, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomContent(
    uiState: HomeState,
    onAction: (HomeIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Learning Centre")
                },
                actions = {
                    if (uiState.user?.isPresident == true) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Home"
                            )
                        }
                    }
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp)
        ) {
            if (uiState.appForm.isNotEmpty()) {
                item {
                    Text(
                        text = "Application Form",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(16.dp))
                }
                items(uiState.appForm.size) {
                    ApplicationFormItem(
                        applicationForm = uiState.appForm[it],
                        onAcceptClick = {
                            onAction(HomeIntent.AcceptApplication(uiState.appForm[it]))
                        },
                        onRejectClick = {
                            onAction(HomeIntent.DeclineApplication(uiState.appForm[it]))
                        }
                    )
                }
                item {
                    Spacer(Modifier.height(16.dp))
                }
            }
            items(uiState.tasks.size) {
                TaskItem(
                    task = uiState.tasks[it],
                    isPresident = uiState.user?.isPresident ?: false,
                    joinTask = {
                        onAction(HomeIntent.AcceptTask(uiState.tasks[it]))
                    },
                    rejectTask = {
                        onAction(HomeIntent.DeclineTask(uiState.tasks[it]))
                    },
                    completeTask = {
                        onAction(HomeIntent.CompleteTask(uiState.tasks[it]))
                    },
                    deleteTask = {
                        onAction(HomeIntent.DeleteTask(uiState.tasks[it]))
                    }
                )
            }

        }
    }
}

@Composable
private fun ApplicationFormItem(
    applicationForm: ApplicationFormData,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Student: ${applicationForm.studentName}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "Description: ${applicationForm.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onAcceptClick() },
                    enabled = !applicationForm.isAccepted
                ) {
                    Text("Accept")
                }
                Button(
                    onClick = { onRejectClick() },
                    enabled = applicationForm.isAccepted
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    isPresident: Boolean,
    task: TaskData,
    joinTask: () -> Unit,
    rejectTask: () -> Unit,
    completeTask: () -> Unit,
    deleteTask: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Task Title
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            if (task.acceptedMembers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Accepted Members:", style = MaterialTheme.typography.bodySmall)
                TaskMembersList(members = task.acceptedMemberNames)
            }
            if (task.rejectedMembers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Rejected Members:", style = MaterialTheme.typography.bodySmall)
                TaskMembersList(members = task.rejectedMemberNames)
            }
            if (shouldShowButtons(task, isPresident)) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (canJoinOrReject(task)) {
                        ActionButton(text = "Join", onClick = { joinTask() })
                        ActionButton(text = "Reject", onClick = { rejectTask() })
                    }

                    if (isPresident) {
                        ActionButton(
                            text = "Complete",
                            onClick = { completeTask() }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { deleteTask() }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text)
    }
}

private fun shouldShowButtons(task: TaskData, isPresident: Boolean) =
    (!task.isCompleted && !task.isYouRejected && !task.isYouJoined) || (isPresident && !task.isCompleted)

private fun canJoinOrReject(task: TaskData) =
    !task.isYouRejected && !task.isYouJoined

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TaskMembersList(members: List<String>) {
    if (members.isNotEmpty()) {
        FlowRow {
            members.forEach { member ->
                TextButton(
                    onClick = { },
                    content = { Text(text = member) }
                )
            }
        }
    } else {
        Text("No members.", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
    }
}


