package com.codeblueprint.ui.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

/**
 * 루트 컴포넌트 인터페이스
 */
interface RootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        data class PatternList(val component: PatternListComponent) : Child()
        data class PatternDetail(val component: PatternDetailComponent) : Child()
        data class Search(val component: SearchComponent) : Child()
        data class Bookmarks(val component: BookmarksComponent) : Child()
        data class Settings(val component: SettingsComponent) : Child()
        data class ArchitectureList(val component: ArchitectureListComponent) : Child()
        data class ArchitectureDetail(val component: ArchitectureDetailComponent) : Child()
        data class AIAdvisor(val component: AIAdvisorComponent) : Child()
        data class CodePlayground(val component: CodePlaygroundComponent) : Child()
        data class AlgorithmList(val component: AlgorithmListComponent) : Child()
        data class AlgorithmDetail(val component: AlgorithmDetailComponent) : Child()
    }
}

/**
 * 패턴 목록 화면 컴포넌트
 */
interface PatternListComponent {
    fun onPatternClick(patternId: String)
    fun onSearchClick()
    fun onBookmarksClick()
    fun onSettingsClick()
    fun onArchitectureClick()
    fun onAIAdvisorClick()
    fun onAlgorithmClick()
}

/**
 * 패턴 상세 화면 컴포넌트
 */
interface PatternDetailComponent {
    val patternId: String
    fun onBackClick()
    fun onRelatedPatternClick(patternId: String)
    fun onCodePlaygroundClick()
}

/**
 * 검색 화면 컴포넌트
 */
interface SearchComponent {
    fun onBackClick()
    fun onPatternClick(patternId: String)
}

/**
 * 북마크 화면 컴포넌트
 */
interface BookmarksComponent {
    fun onBackClick()
    fun onPatternClick(patternId: String)
}

/**
 * 설정 화면 컴포넌트
 */
interface SettingsComponent {
    fun onBackClick()
}

/**
 * 아키텍처 목록 화면 컴포넌트
 */
interface ArchitectureListComponent {
    fun onBackClick()
    fun onArchitectureClick(architectureId: String)
}

/**
 * 아키텍처 상세 화면 컴포넌트
 */
interface ArchitectureDetailComponent {
    val architectureId: String
    fun onBackClick()
}

/**
 * AI 어드바이저 화면 컴포넌트
 */
interface AIAdvisorComponent {
    fun onBackClick()
    fun onPatternClick(patternId: String)
}

/**
 * 코드 플레이그라운드 화면 컴포넌트
 */
interface CodePlaygroundComponent {
    fun onBackClick()
}

/**
 * 알고리즘 목록 화면 컴포넌트
 */
interface AlgorithmListComponent {
    fun onBackClick()
    fun onAlgorithmClick(algorithmId: String)
    fun onSearchClick()
}

/**
 * 알고리즘 상세 화면 컴포넌트
 */
interface AlgorithmDetailComponent {
    val algorithmId: String
    fun onBackClick()
    fun onRelatedAlgorithmClick(algorithmId: String)
    fun onCodePlaygroundClick()
}

/**
 * 루트 컴포넌트 기본 구현
 */
class DefaultRootComponent(
    componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.PatternList,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): RootComponent.Child {
        return when (config) {
            is Config.PatternList -> RootComponent.Child.PatternList(
                DefaultPatternListComponent(componentContext, navigation)
            )
            is Config.PatternDetail -> RootComponent.Child.PatternDetail(
                DefaultPatternDetailComponent(componentContext, config.patternId, navigation)
            )
            is Config.Search -> RootComponent.Child.Search(
                DefaultSearchComponent(componentContext, navigation)
            )
            is Config.Bookmarks -> RootComponent.Child.Bookmarks(
                DefaultBookmarksComponent(componentContext, navigation)
            )
            is Config.Settings -> RootComponent.Child.Settings(
                DefaultSettingsComponent(componentContext, navigation)
            )
            is Config.ArchitectureList -> RootComponent.Child.ArchitectureList(
                DefaultArchitectureListComponent(componentContext, navigation)
            )
            is Config.ArchitectureDetail -> RootComponent.Child.ArchitectureDetail(
                DefaultArchitectureDetailComponent(componentContext, config.architectureId, navigation)
            )
            is Config.AIAdvisor -> RootComponent.Child.AIAdvisor(
                DefaultAIAdvisorComponent(componentContext, navigation)
            )
            is Config.CodePlayground -> RootComponent.Child.CodePlayground(
                DefaultCodePlaygroundComponent(componentContext, navigation)
            )
            is Config.AlgorithmList -> RootComponent.Child.AlgorithmList(
                DefaultAlgorithmListComponent(componentContext, navigation)
            )
            is Config.AlgorithmDetail -> RootComponent.Child.AlgorithmDetail(
                DefaultAlgorithmDetailComponent(componentContext, config.algorithmId, navigation)
            )
        }
    }

    @Serializable
    sealed class Config {
        @Serializable
        data object PatternList : Config()

        @Serializable
        data class PatternDetail(val patternId: String) : Config()

        @Serializable
        data object Search : Config()

        @Serializable
        data object Bookmarks : Config()

        @Serializable
        data object Settings : Config()

        @Serializable
        data object ArchitectureList : Config()

        @Serializable
        data class ArchitectureDetail(val architectureId: String) : Config()

        @Serializable
        data object AIAdvisor : Config()

        @Serializable
        data object CodePlayground : Config()

        @Serializable
        data object AlgorithmList : Config()

        @Serializable
        data class AlgorithmDetail(val algorithmId: String) : Config()
    }
}

/**
 * 패턴 목록 컴포넌트 기본 구현
 */
class DefaultPatternListComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : PatternListComponent, ComponentContext by componentContext {

    override fun onPatternClick(patternId: String) {
        navigation.push(DefaultRootComponent.Config.PatternDetail(patternId))
    }

    override fun onSearchClick() {
        navigation.push(DefaultRootComponent.Config.Search)
    }

    override fun onBookmarksClick() {
        navigation.push(DefaultRootComponent.Config.Bookmarks)
    }

    override fun onSettingsClick() {
        navigation.push(DefaultRootComponent.Config.Settings)
    }

    override fun onArchitectureClick() {
        navigation.push(DefaultRootComponent.Config.ArchitectureList)
    }

    override fun onAIAdvisorClick() {
        navigation.push(DefaultRootComponent.Config.AIAdvisor)
    }

    override fun onAlgorithmClick() {
        navigation.push(DefaultRootComponent.Config.AlgorithmList)
    }
}

/**
 * 패턴 상세 컴포넌트 기본 구현
 */
class DefaultPatternDetailComponent(
    componentContext: ComponentContext,
    override val patternId: String,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : PatternDetailComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onRelatedPatternClick(patternId: String) {
        navigation.push(DefaultRootComponent.Config.PatternDetail(patternId))
    }

    override fun onCodePlaygroundClick() {
        navigation.push(DefaultRootComponent.Config.CodePlayground)
    }
}

/**
 * 검색 컴포넌트 기본 구현
 */
class DefaultSearchComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : SearchComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onPatternClick(patternId: String) {
        navigation.push(DefaultRootComponent.Config.PatternDetail(patternId))
    }
}

/**
 * 북마크 컴포넌트 기본 구현
 */
class DefaultBookmarksComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : BookmarksComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onPatternClick(patternId: String) {
        navigation.push(DefaultRootComponent.Config.PatternDetail(patternId))
    }
}

/**
 * 설정 컴포넌트 기본 구현
 */
class DefaultSettingsComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : SettingsComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }
}

/**
 * 아키텍처 목록 컴포넌트 기본 구현
 */
class DefaultArchitectureListComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : ArchitectureListComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onArchitectureClick(architectureId: String) {
        navigation.push(DefaultRootComponent.Config.ArchitectureDetail(architectureId))
    }
}

/**
 * 아키텍처 상세 컴포넌트 기본 구현
 */
class DefaultArchitectureDetailComponent(
    componentContext: ComponentContext,
    override val architectureId: String,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : ArchitectureDetailComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }
}

/**
 * AI 어드바이저 컴포넌트 기본 구현
 */
class DefaultAIAdvisorComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : AIAdvisorComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onPatternClick(patternId: String) {
        navigation.push(DefaultRootComponent.Config.PatternDetail(patternId))
    }
}

/**
 * 코드 플레이그라운드 컴포넌트 기본 구현
 */
class DefaultCodePlaygroundComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : CodePlaygroundComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }
}

/**
 * 알고리즘 목록 컴포넌트 기본 구현
 */
class DefaultAlgorithmListComponent(
    componentContext: ComponentContext,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : AlgorithmListComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onAlgorithmClick(algorithmId: String) {
        navigation.push(DefaultRootComponent.Config.AlgorithmDetail(algorithmId))
    }

    override fun onSearchClick() {
        navigation.push(DefaultRootComponent.Config.Search)
    }
}

/**
 * 알고리즘 상세 컴포넌트 기본 구현
 */
class DefaultAlgorithmDetailComponent(
    componentContext: ComponentContext,
    override val algorithmId: String,
    private val navigation: StackNavigation<DefaultRootComponent.Config>
) : AlgorithmDetailComponent, ComponentContext by componentContext {

    override fun onBackClick() {
        navigation.pop()
    }

    override fun onRelatedAlgorithmClick(algorithmId: String) {
        navigation.push(DefaultRootComponent.Config.AlgorithmDetail(algorithmId))
    }

    override fun onCodePlaygroundClick() {
        navigation.push(DefaultRootComponent.Config.CodePlayground)
    }
}
