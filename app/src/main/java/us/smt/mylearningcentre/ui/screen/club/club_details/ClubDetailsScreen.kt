package us.smt.mylearningcentre.ui.screen.club.club_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.data.model.ClubDetailsData
import us.smt.mylearningcentre.data.model.StudentData

class ClubDetailsScreen(private val clubId: String, private val clubName: String) : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<ClubDetailsViewModel>()
        val uiState by viewModel.state.collectAsState()
        LaunchedEffect(key1 = Unit) {
            viewModel.onAction(ClubDetailsIntent.Init(clubId))
        }
        ClubDetailsScreenContent(name = clubName, uiState = uiState, onAction = viewModel::onAction)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClubDetailsScreenContent(
    name: String,
    uiState: ClubDetailsState,
    onAction: (ClubDetailsIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = name) },
                navigationIcon = {
                    IconButton(onClick = {
                        onAction(ClubDetailsIntent.Back)
                    }) {
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
        content = { paddingValues ->
            if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        text = uiState.club?.name ?: "",
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    InfoRow(label = "Category", value = uiState.club?.category ?: "")
                    InfoRow(label = "President", value = "${uiState.president?.name ?: ""} ${uiState.president?.surname ?: ""}")

                    Text(
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Start,
                        text = "Members",
                        fontSize = 16.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 18.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    uiState.clubMembers.forEach {
                        Card(Modifier.padding(vertical = 4.dp)) {
                            Column(Modifier.padding(8.dp)) {
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    text = it.name,
                                    fontSize = 16.sp,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    style = MaterialTheme.typography.bodyMedium,
                                    text = it.surname,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Text(
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        textAlign = TextAlign.Start,
                        fontSize = 18.sp,
                        text = "Description",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp,
                        text = uiState.club?.description ?: "",
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onAction(ClubDetailsIntent.JoinClub)
                        },
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
                                color = Color.White, fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            text = label
        )
        Text(
            fontSize = 16.sp,
            style = MaterialTheme.typography.bodySmall,
            text = value
        )
    }
}


@Preview
@Composable
private fun ClubDetailsScreenContentPrev() {
    ClubDetailsScreenContent(name = "Title",
        uiState = ClubDetailsState(
            isLoading = false,
            club = ClubDetailsData(
                id = "1",
                name = "Club Name",
                category = "Activity",
                presidentId = "1",
                members = listOf("User 1", "User 2", "User 3"),
                description = "Description all something wrieted here and d d"
            ),
            president = StudentData(
                id = "1",
                name = "President",
                surname = "Name",
                email = "Email",
                clubId = "1",
                isPresident = true,
                isWaitingApplication = "",
                fcmToken = ""
            ),
            clubMembers = listOf(
                StudentData(
                    id = "1",
                    name = "User 1",
                    surname = "Name",
                    email = "Email",
                    clubId = "1",
                    isPresident = false,
                    isWaitingApplication = "",
                    fcmToken = ""
                ),
                StudentData(
                    id = "2",
                    name = "User 2",
                    surname = "Name",
                    email = "Email",
                    clubId = "1",
                    isPresident = false,
                    isWaitingApplication = "",
                    fcmToken = ""
                ),
                StudentData(
                    id = "3",
                    name = "User 3",
                    surname = "Name",
                    email = "Email",
                    clubId = "1",
                    isPresident = false,
                    isWaitingApplication = "",
                    fcmToken = ""
                )
            )
        ), onAction = {})
}