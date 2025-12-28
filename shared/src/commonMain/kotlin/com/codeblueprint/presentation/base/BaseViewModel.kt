package com.codeblueprint.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * KMP 호환 Base ViewModel
 *
 * 플랫폼 독립적인 ViewModel 기반 클래스입니다.
 * Android에서는 lifecycle과 연동하여 사용하고,
 * iOS/Desktop에서는 직접 lifecycle을 관리합니다.
 */
abstract class BaseViewModel {
    /**
     * ViewModel의 CoroutineScope
     * SupervisorJob을 사용하여 하위 코루틴 실패가 다른 코루틴에 영향을 주지 않습니다.
     */
    protected val viewModelScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    /**
     * ViewModel 정리
     * 플랫폼에서 적절한 시점에 호출해야 합니다.
     */
    open fun onCleared() {
        viewModelScope.cancel()
    }
}
