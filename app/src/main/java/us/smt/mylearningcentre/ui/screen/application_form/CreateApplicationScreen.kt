package us.smt.mylearningcentre.ui.screen.application_form

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.ui.designs.MultilineTextView

class CreateApplicationScreen(private val clubId: String, private val clubName: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<CreateApplicationViewModel>()
        val state by viewModel.state.collectAsState()
        CreateApplicationScreenContent(
            state = state,
            clubName = clubName,
            clubId = clubId,
            onAction = viewModel::onAction
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateApplicationScreenContent(
    state: CreateApplicationState,
    clubName: String,
    clubId: String,
    onAction: (CreateApplicationIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Create New Application") },
                navigationIcon = {
                    IconButton(onClick = { onAction(CreateApplicationIntent.Back) }) {
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

                Text(
                    text = "You ar joining this club $clubName.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                MultilineTextView(
                    hint = "Description",
                    state = state.description,
                    onChange = { description -> onAction(CreateApplicationIntent.ChangeDescription(description)) }
                )

                // Save Button
                Button(
                    onClick = { onAction(CreateApplicationIntent.Save(clubId)) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Join Club",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    )

}

@Preview
@Composable
private fun CreateApplicationScreenContentPrevLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        CreateApplicationScreenContent(state = CreateApplicationState(), clubName = "Club Name", clubId = "Club Id", onAction = {})
    }
}

@Preview
@Composable
private fun CreateApplicationScreenContentPrevDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        CreateApplicationScreenContent(state = CreateApplicationState(), clubName = "Club Name", clubId = "Club Id", onAction = {})
    }
}
