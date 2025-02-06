package us.smt.mylearningcentre.ui.screen.setting.setting_tab

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.hilt.getViewModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import us.smt.mylearningcentre.data.model.StudentData

class SettingTab : Tab {
    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 2u, title = "Profile", icon = rememberVectorPainter(Icons.Default.Person)
        )

    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingViewModel>()
        val state by viewModel.state.collectAsState()
        ProfileScreen(
            state = state, onAction = viewModel::onAction
        )
    }
}


@Composable
private fun ProfileScreen(
    state: SettingState, onAction: (SettingIntent) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background) // Orqa fonni moslashtirish
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(
                top = 24.dp, bottom = 16.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp, top = 12.dp, bottom = 4.dp),
            text = "${state.user?.name} ${state.user?.surname}".ifBlank { "No Name" },
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colors.primary // Asosiy rang
        )
        Text(
            modifier = Modifier.padding(horizontal = 24.dp), text = state.user?.email?.ifBlank { "empty" } ?: "empty", fontSize = 22.sp, color = colors.secondary
        )

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp), horizontalAlignment = Alignment.Start
        ) {
            SwitchDarkLightMode()
            HorizontalDivider(thickness = 0.5.dp, color = colors.onSurface.copy(alpha = 0.5f))
            ActionItem(text = "Change president", onClick = { onAction(SettingIntent.OpenAbout) })
            HorizontalDivider(thickness = 0.5.dp, color = colors.onSurface.copy(alpha = 0.5f))
            ActionItem(text = "About", onClick = { onAction(SettingIntent.OpenAbout) })
            HorizontalDivider(thickness = 0.5.dp, color = colors.onSurface.copy(alpha = 0.5f))
            ActionItem(text = "Help", onClick = { onAction(SettingIntent.OpenHelp) })
            HorizontalDivider(thickness = 0.5.dp, color = colors.onSurface.copy(alpha = 0.5f))
            ActionItem(text = "Leave club", onClick = { onAction(SettingIntent.Leave) })
        }
    }
}

@Composable
private fun ActionItem(text: String, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, interactionSource = remember { MutableInteractionSource() }, indication = ripple())
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text, fontSize = 18.sp, color = colors.onBackground, // Matn rangi Dark mode uchun moslashgan
            fontWeight = FontWeight.Medium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next", tint = colors.primary
        )
    }
}

@Composable
private fun SwitchDarkLightMode() {
    val colors = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()
    val isChecked = remember {
        mutableStateOf(
            isDark
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Dark mode", fontSize = 18.sp, color = colors.onBackground, // Matn rangi Dark mode uchun moslashgan
            fontWeight = FontWeight.Medium
        )
        Switch(
            checked = isDark, onCheckedChange = {
                isChecked.value = it
                val mode = if (it) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ProfileScreen(state = SettingState(), onAction = {})
    }
}


@Preview
@Composable
private fun ProfileScreenPreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ProfileScreen(state = SettingState(), onAction = {})
    }
}

