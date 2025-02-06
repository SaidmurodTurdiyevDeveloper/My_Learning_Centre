package us.smt.mylearningcentre.ui.screen.club.club_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import us.smt.mylearningcentre.data.model.ClubData

class ClubListScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = getViewModel<ClubListViewModel>()
        val state by viewModel.state.collectAsState()
        LaunchedEffect(key1 = Unit) {
            viewModel.onAction(ClubListIntent.Init)
        }
        ClubListScreenContent(state = state, onAction = viewModel::onAction)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClubListScreenContent(state: ClubListState, onAction: (ClubListIntent) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Club List") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onAction(ClubListIntent.OpenCreateClub)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(state.list) { club ->
                ClubItem(club, onAction = onAction)
            }
        }
    }
}

@Composable
private fun ClubItem(club: ClubData, onAction: (ClubListIntent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onAction(ClubListIntent.ClubClick(club.id, club.name)) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = club.name, fontSize = 20.sp)
            Text(text = club.category, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Preview
@Composable
private fun PreviewClubListScreen() {
    ClubListScreenContent(
        state = ClubListState(
            list = listOf(
                ClubData(
                    id = "1",
                    name = "Club A",
                    category = "Category A"
                ),
                ClubData(
                    id = "2",
                    name = "Club B",
                    category = "Category B"
                )
            )
        ),
        onAction = {}
    )
}
