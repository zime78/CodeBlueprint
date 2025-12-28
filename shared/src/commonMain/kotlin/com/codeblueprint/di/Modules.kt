package com.codeblueprint.di

import com.codeblueprint.data.local.AlgorithmDataProvider
import com.codeblueprint.data.local.DatabaseDriverFactory
import com.codeblueprint.data.local.PatternDataInitializer
import com.codeblueprint.data.mapper.AlgorithmLearningProgressMapper
import com.codeblueprint.data.mapper.AlgorithmMapper
import com.codeblueprint.data.mapper.LearningProgressMapper
import com.codeblueprint.data.mapper.PatternMapper
import com.codeblueprint.data.mapper.SettingsMapper
import com.codeblueprint.data.repository.AlgorithmRepositoryImpl
import com.codeblueprint.data.repository.ArchitectureRepositoryImpl
import com.codeblueprint.data.repository.CodeExecutionRepositoryImpl
import com.codeblueprint.data.repository.PatternRepositoryImpl
import com.codeblueprint.data.repository.SettingsRepositoryImpl
import com.codeblueprint.db.CodeBlueprintDatabase
import com.codeblueprint.domain.repository.AlgorithmRepository
import com.codeblueprint.domain.repository.ArchitectureRepository
import com.codeblueprint.domain.repository.CodeExecutionRepository
import com.codeblueprint.domain.repository.PatternRepository
import com.codeblueprint.domain.repository.SettingsRepository
import com.codeblueprint.domain.usecase.ExecuteCodeUseCase
import com.codeblueprint.domain.usecase.GetAlgorithmDetailUseCase
import com.codeblueprint.domain.usecase.GetAlgorithmsUseCase
import com.codeblueprint.domain.usecase.GetArchitectureDetailUseCase
import com.codeblueprint.domain.usecase.GetArchitecturesUseCase
import com.codeblueprint.domain.usecase.GetPatternDetailUseCase
import com.codeblueprint.domain.usecase.GetPatternsUseCase
import com.codeblueprint.domain.usecase.GetSettingsUseCase
import com.codeblueprint.domain.usecase.SaveSettingsUseCase
import com.codeblueprint.domain.usecase.SearchAlgorithmsUseCase
import com.codeblueprint.domain.usecase.SearchPatternsUseCase
import com.codeblueprint.domain.usecase.ToggleAlgorithmBookmarkUseCase
import com.codeblueprint.domain.usecase.ToggleBookmarkUseCase
import com.codeblueprint.presentation.algorithm.detail.AlgorithmDetailViewModel
import com.codeblueprint.presentation.algorithm.list.AlgorithmListViewModel
import com.codeblueprint.presentation.architecture.detail.ArchitectureDetailViewModel
import com.codeblueprint.presentation.architecture.list.ArchitectureListViewModel
import com.codeblueprint.presentation.bookmarks.BookmarksViewModel
import com.codeblueprint.presentation.pattern.detail.PatternDetailViewModel
import com.codeblueprint.presentation.pattern.list.PatternListViewModel
import com.codeblueprint.presentation.playground.CodePlaygroundViewModel
import com.codeblueprint.presentation.search.SearchViewModel
import com.codeblueprint.presentation.settings.SettingsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Domain Layer 모듈
 *
 * UseCase 의존성 정의
 */
val domainModule = module {
    factory { GetPatternsUseCase(get()) }
    factory { GetPatternDetailUseCase(get()) }
    factory { SearchPatternsUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
    factory { GetSettingsUseCase(get()) }
    factory { SaveSettingsUseCase(get()) }

    // Architecture UseCases
    factory { GetArchitecturesUseCase(get()) }
    factory { GetArchitectureDetailUseCase(get()) }

    // Code Playground UseCases
    factory { ExecuteCodeUseCase(get()) }

    // Algorithm UseCases
    factory { GetAlgorithmsUseCase(get()) }
    factory { GetAlgorithmDetailUseCase(get()) }
    factory { SearchAlgorithmsUseCase(get()) }
    factory { ToggleAlgorithmBookmarkUseCase(get()) }
}

/**
 * Data Layer 모듈
 *
 * Repository, Mapper, DataSource 의존성 정의
 */
val dataModule = module {
    // Mappers
    single { PatternMapper() }
    single { LearningProgressMapper() }
    single { SettingsMapper() }
    single { AlgorithmMapper() }
    single { AlgorithmLearningProgressMapper() }

    // Database
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        CodeBlueprintDatabase(driver)
    }

    // Data Initializer
    single { PatternDataInitializer(get(), get()) }
    single { AlgorithmDataProvider(get(), get()) }

    // Repository
    single<PatternRepository> {
        PatternRepositoryImpl(
            database = get(),
            patternMapper = get(),
            progressMapper = get()
        )
    }
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            database = get(),
            mapper = get()
        )
    }

    // Architecture Repository (인메모리)
    single<ArchitectureRepository> { ArchitectureRepositoryImpl() }

    // Code Execution Repository (Mock)
    single<CodeExecutionRepository> { CodeExecutionRepositoryImpl() }

    // Algorithm Repository
    single<AlgorithmRepository> {
        AlgorithmRepositoryImpl(
            database = get(),
            algorithmMapper = get(),
            progressMapper = get<AlgorithmLearningProgressMapper>()
        )
    }
}

/**
 * Presentation Layer 모듈
 *
 * ViewModel 의존성 정의
 */
val presentationModule = module {
    // ViewModels
    factory { PatternListViewModel(get(), get()) }
    factory { (patternId: String) ->
        PatternDetailViewModel(patternId, get(), get(), get())
    }
    factory { SearchViewModel(get(), get(), get(), get()) }
    factory { BookmarksViewModel(get(), get()) }
    factory { SettingsViewModel(get(), get()) }

    // Architecture ViewModels
    factory { ArchitectureListViewModel(get()) }
    factory { (architectureId: String) ->
        ArchitectureDetailViewModel(architectureId, get())
    }

    // Code Playground ViewModel
    factory { CodePlaygroundViewModel(get()) }

    // Algorithm ViewModels
    factory { AlgorithmListViewModel(get(), get()) }
    factory { (algorithmId: String) ->
        AlgorithmDetailViewModel(
            algorithmId = algorithmId,
            getAlgorithmDetailUseCase = get(),
            getAlgorithmsUseCase = get(),
            toggleBookmarkUseCase = get<ToggleAlgorithmBookmarkUseCase>()
        )
    }
}

/**
 * 공통 모듈 목록
 *
 * 플랫폼 모듈에서 platformModule을 추가하여 사용합니다.
 */
fun commonModules(): List<Module> = listOf(
    domainModule,
    dataModule,
    presentationModule
)
