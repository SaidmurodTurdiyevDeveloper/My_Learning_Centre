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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.Alignment
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
fun HomContent(
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
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Home"
                        )
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
                        onAcceptClick = { id -> },
                        onRejectClick = { id -> }
                    )
                }
            }
            items(uiState.tasks.size) {
                TaskItem(task = uiState.tasks[it], onCompletionChange = { _, _ -> })
            }

        }
    }
}

@Composable
fun ApplicationFormItem(
    applicationForm: ApplicationFormData,
    onAcceptClick: (String) -> Unit,
    onRejectClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Title/Student Info
            Text(
                text = "Student ID: ${applicationForm.studentId}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Club ID
            Text(
                text = "Club ID: ${applicationForm.clubId}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "Description: ${applicationForm.description}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Application Status (Accepted/Rejected)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Accepted:")
                Icon(
                    imageVector = if (applicationForm.isAccepted) Icons.Default.Check else Icons.Default.Clear,
                    contentDescription = if (applicationForm.isAccepted) "Accepted" else "Rejected",
                    tint = if (applicationForm.isAccepted) Color.Green else Color.Red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Accept/Reject buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { onAcceptClick(applicationForm.id) },
                    enabled = !applicationForm.isAccepted
                ) {
                    Text("Accept")
                }
                Button(
                    onClick = { onRejectClick(applicationForm.id) },
                    enabled = applicationForm.isAccepted
                ) {
                    Text("Reject")
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskData, onCompletionChange: (String, Boolean) -> Unit) {
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

            // Task Description
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display accepted members
            Text(text = "Accepted Members:", style = MaterialTheme.typography.bodySmall)
            TaskMembersList(members = task.acceptedMembers)

            Spacer(modifier = Modifier.height(8.dp))

            // Display rejected members
            Text(text = "Rejected Members:", style = MaterialTheme.typography.bodySmall)
            TaskMembersList(members = task.rejectedMembers)

            Spacer(modifier = Modifier.height(16.dp))

            // Completed Task Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Completed")
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { isChecked ->
                        onCompletionChange(task.id, isChecked)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskMembersList(members: List<String>) {
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
        Text("No members.")
    }
}


