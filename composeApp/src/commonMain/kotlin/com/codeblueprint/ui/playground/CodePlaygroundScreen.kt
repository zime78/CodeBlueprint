package com.codeblueprint.ui.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codeblueprint.domain.model.ProgrammingLanguage
import com.codeblueprint.presentation.playground.CodePlaygroundEvent
import com.codeblueprint.presentation.playground.CodePlaygroundUiState
import com.codeblueprint.presentation.playground.CodePlaygroundViewModel
import com.codeblueprint.ui.navigation.CodePlaygroundComponent
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

/**
 * 코드 플레이그라운드 화면
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodePlaygroundScreen(
    component: CodePlaygroundComponent,
    viewModel: CodePlaygroundViewModel = koinInject {
        parametersOf(component.code, component.languageName, component.expectedOutput)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    val code by viewModel.code.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val copyMessage by viewModel.copyMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    // 복사 메시지 Snackbar 표시
    LaunchedEffect(copyMessage) {
        copyMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(CodePlaygroundEvent.OnClearCopyMessage)
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("코드 플레이그라운드") },
                navigationIcon = {
                    IconButton(onClick = { component.onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                },
                actions = {
                    // 초기화 버튼
                    IconButton(
                        onClick = { viewModel.onEvent(CodePlaygroundEvent.OnResetCode) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "초기화"
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 언어 선택 및 실행 버튼
            ControlBar(
                selectedLanguage = selectedLanguage,
                isExecuting = uiState is CodePlaygroundUiState.Executing,
                onLanguageChange = { viewModel.onEvent(CodePlaygroundEvent.OnLanguageChange(it)) },
                onExecute = { viewModel.onEvent(CodePlaygroundEvent.OnExecute) }
            )

            // 코드 에디터
            CodeEditor(
                code = code,
                onCodeChange = { viewModel.onEvent(CodePlaygroundEvent.OnCodeChange(it)) },
                onCopyCode = { viewModel.onEvent(CodePlaygroundEvent.OnCopyCode) },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            // 출력 패널
            OutputPanel(
                uiState = uiState,
                onCopyOutput = { viewModel.onEvent(CodePlaygroundEvent.OnCopyOutput) },
                onClear = { viewModel.onEvent(CodePlaygroundEvent.OnClearOutput) },
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
            )
        }
    }
}

/**
 * 컨트롤 바 (언어 선택, 실행 버튼)
 */
@Composable
private fun ControlBar(
    selectedLanguage: ProgrammingLanguage,
    isExecuting: Boolean,
    onLanguageChange: (ProgrammingLanguage) -> Unit,
    onExecute: () -> Unit
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 언어 선택 드롭다운
            Box {
                OutlinedButton(
                    onClick = { languageMenuExpanded = true }
                ) {
                    Text(selectedLanguage.displayName)
                }

                DropdownMenu(
                    expanded = languageMenuExpanded,
                    onDismissRequest = { languageMenuExpanded = false }
                ) {
                    ProgrammingLanguage.entries.forEach { language ->
                        DropdownMenuItem(
                            text = { Text(language.displayName) },
                            onClick = {
                                onLanguageChange(language)
                                languageMenuExpanded = false
                            }
                        )
                    }
                }
            }

            // 실행 버튼
            FilledTonalButton(
                onClick = onExecute,
                enabled = !isExecuting
            ) {
                if (isExecuting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("실행 중...")
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("실행")
                }
            }
        }
    }
}

/**
 * 코드 에디터
 */
@Composable
private fun CodeEditor(
    code: String,
    onCodeChange: (String) -> Unit,
    onCopyCode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "소스",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                IconButton(
                    onClick = onCopyCode,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "코드 복사",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 코드 영역
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                val scrollState = rememberScrollState()
                val horizontalScrollState = rememberScrollState()

                BasicTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .horizontalScroll(horizontalScrollState),
                    decorationBox = { innerTextField ->
                        Row {
                            // 라인 넘버
                            LineNumbers(
                                lineCount = code.lines().size,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                            // 코드 영역
                            Box(modifier = Modifier.weight(1f)) {
                                if (code.isEmpty()) {
                                    Text(
                                        text = "코드를 입력하세요...",
                                        style = TextStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 14.sp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )
            }
        }
    }
}

/**
 * 라인 넘버
 */
@Composable
private fun LineNumbers(
    lineCount: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        repeat(maxOf(lineCount, 1)) { index ->
            Text(
                text = (index + 1).toString().padStart(3),
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
        }
    }
}

/**
 * 출력 패널
 */
@Composable
private fun OutputPanel(
    uiState: CodePlaygroundUiState,
    onCopyOutput: () -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "출력",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row {
                    IconButton(
                        onClick = onCopyOutput,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "출력 복사",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onClear,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "출력 지우기",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 출력 내용
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                when (uiState) {
                    is CodePlaygroundUiState.Idle -> {
                        if (uiState.output.isNotEmpty()) {
                            OutputText(text = uiState.output)
                        } else {
                            Text(
                                text = "실행 결과가 여기에 표시됩니다.",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }
                    is CodePlaygroundUiState.Executing -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "실행 중...",
                                style = TextStyle(
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                    is CodePlaygroundUiState.Success -> {
                        Column {
                            OutputText(text = uiState.result.output)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "✓ 실행 완료 (${uiState.result.executionTimeMs}ms)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                    is CodePlaygroundUiState.Error -> {
                        Text(
                            text = "❌ ${uiState.message}",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * 출력 텍스트
 */
@Composable
private fun OutputText(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    )
}
