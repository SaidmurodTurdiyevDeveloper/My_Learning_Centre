package us.smt.mylearningcentre.ui.screen.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.ui.designs.MultilineTextView
import us.smt.mylearningcentre.ui.designs.SimpleTextView

class CreateTaskScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<CreateTaskViewModel>()
        val uiState by viewModel.state.collectAsState()
        AddNewClubScreenContent(state = uiState, onAction = viewModel::onAction)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNewClubScreenContent(
    state: CreateTaskState,
    onAction: (CreateTaskIntent) -> Unit
) {
    if (state.isLoading) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Create New Club") },
                    navigationIcon = {
                        IconButton(onClick = { onAction(CreateTaskIntent.Back) }) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    SimpleTextView(
                        hint = "Task title",
                        state = state.taskTitle,
                        onChange = { name -> onAction(CreateTaskIntent.TitleChanged(name)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    MultilineTextView(
                        hint = "Task description",
                        state = state.description,
                        onChange = { description -> onAction(CreateTaskIntent.DescriptionChanged(description)) }
                    )

                    Button(
                        onClick = { onAction(CreateTaskIntent.CreateTask) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Save Club",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun AddNewClubScreenContentPrevLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        AddNewClubScreenContent(state = CreateTaskState(), onAction = {})
    }
}

@Preview
@Composable
private fun AddNewClubScreenContentPrevDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        AddNewClubScreenContent(state = CreateTaskState(), onAction = {})
    }
}