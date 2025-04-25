package com.app.di

import com.app.data.repository.UserFirestoreRepository
import com.app.domain.repository.UserRepository
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.ExistUsernameUseCase
import com.app.domain.usecase.user.GetEmailByNameUseCase
import com.app.domain.usecase.user.GetUserByIdUseCase
import com.app.domain.usecase.user.SaveUserUseCase
import com.app.presentation.viewmodel.LoginScreenViewModel
import com.app.presentation.viewmodel.RegisterScreenViewModel
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single { FirebaseFirestore.getInstance() }

    // data.repository repositorios
    single<UserRepository> { UserFirestoreRepository(get()) }

    // UseCase
    factory { GetUserByIdUseCase(get()) }
    factory { ExistUsernameUseCase(get()) }
    factory { SaveUserUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { GetEmailByNameUseCase(get()) }

    // ViewModel
    viewModel{RegisterScreenViewModel(get(),get())}
    viewModel{LoginScreenViewModel(get())}
}