package com.codeblueprint.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.domain.model.ThemeMode
import com.codeblueprint.presentation.settings.SettingsEvent
import com.codeblueprint.presentation.settings.SettingsUiState
import com.codeblueprint.presentation.settings.SettingsViewModel
import com.codeblueprint.ui.navigation.SettingsComponent
import org.koin.compose.koinInject

/**
 * 설정 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    component: SettingsComponent,
    viewModel: SettingsViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = { component.onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is SettingsUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is SettingsUiState.Success -> {
                SettingsContent(
                    state = state,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
            is SettingsUiState.Error -> {
                ErrorContent(
                    message = state.message,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "설정을 불러오는 중...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState.Success,
    onEvent: (SettingsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 테마 설정 섹션
        SettingsSectionHeader(title = "화면")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            ThemeSettingItem(
                currentTheme = state.theme,
                onThemeChange = { onEvent(SettingsEvent.OnThemeChange(it)) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 코드 설정 섹션
        SettingsSectionHeader(title = "코드")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            LanguageSettingItem(
                currentLanguage = state.defaultCodeLanguage,
                onLanguageChange = { onEvent(SettingsEvent.OnLanguageChange(it)) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 알림 설정 섹션
        SettingsSectionHeader(title = "알림")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            NotificationSettingItem(
                isEnabled = state.notificationsEnabled,
                onToggle = { onEvent(SettingsEvent.OnNotificationsToggle(it)) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 앱 정보 섹션
        SettingsSectionHeader(title = "정보")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            AppInfoItem()
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun ThemeSettingItem(
    currentTheme: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "테마",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = currentTheme.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ThemeMode.entries.forEach { theme ->
                DropdownMenuItem(
                    text = { Text(theme.displayName) },
                    onClick = {
                        onThemeChange(theme)
                        expanded = false
                    },
                    trailingIcon = {
                        if (theme == currentTheme) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun LanguageSettingItem(
    currentLanguage: ProgrammingLanguage,
    onLanguageChange: (ProgrammingLanguage) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "기본 코드 언어",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = currentLanguage.displayName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ProgrammingLanguage.entries.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        onLanguageChange(language)
                        expanded = false
                    },
                    trailingIcon = {
                        if (language == currentLanguage) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationSettingItem(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "알림",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "학습 리마인더 및 새 콘텐츠 알림",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle
        )
    }
}

@Composable
private fun AppInfoItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "버전",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "1.0.0",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

        Text(
            text = "CodeBlueprint",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "디자인 패턴 학습을 위한 멀티플랫폼 앱",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
