package us.smt.mylearningcentre.ui.screen.setting.help

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator

class HelpScreen : Screen {
    @Composable
    override fun Content() {
        HelpScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HelpScreenContent() {
    val navigator = LocalNavigator.current
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigator?.pop()
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E88E5), // Blue color for the app bar
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                title = { Text(text = "Help", style = MaterialTheme.typography.headlineMedium) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                openEmailClient(context = context, "")
            }) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Contact Support")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "How can we help you?",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Find answers to frequently asked questions or contact our support team.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            HelpTopicsList(
                topics = listOf(
                    "How to use the app?",
                    "Account settings",
                    "Privacy and security",
                    "Other issues"
                )
            )
        }
    }
}

@Composable
private fun HelpTopicsList(topics: List<String>) {
    val context = LocalContext.current
    LazyColumn {
        items(topics) { topic ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        openEmailClient(
                            context, topic
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = topic,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

private fun openEmailClient(
    context: Context,
    topic: String
) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps should handle this
        putExtra(Intent.EXTRA_EMAIL, arrayOf("saidmurod98developer@gmail.com")) // Set recipient
        putExtra(Intent.EXTRA_SUBJECT, topic.ifBlank { "Help" })
        putExtra(Intent.EXTRA_TEXT, "Help me")
    }
    try {
        startActivity(
            context, emailIntent, null
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpScreenPreview() {
    HelpScreenContent()
}
