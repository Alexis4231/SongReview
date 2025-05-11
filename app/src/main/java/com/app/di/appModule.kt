package com.app.di

import com.app.data.repository.UserFirestoreRepository
import com.app.domain.repository.UserRepository
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.ExistUsernameUseCase
import com.app.domain.usecase.user.GetEmailByNameUseCase
import com.app.domain.usecase.user.GetUserByIdUseCase
import com.app.domain.usecase.user.SaveUserUseCase
import com.app.presentation.viewmodel.DeezerAlbumViewModel
import com.app.presentation.viewmodel.DeezerArtistsViewModel
import com.app.presentation.viewmodel.DeezerGenreViewModel
import com.app.presentation.viewmodel.DeezerGenresViewModel
import com.app.presentation.viewmodel.DeezerSongsViewModel
import com.app.presentation.viewmodel.NavBarViewModel
import com.app.presentation.viewmodel.SpotifyTokenViewModel
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import GetUserDetailsViewModel
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
    viewModel{UserViewModel(get(),get(),get(),get())}
    viewModel{SpotifyTokenViewModel()}
    viewModel{NavBarViewModel()}
    viewModel{GetUserDetailsViewModel()}
    viewModel{DeezerSongsViewModel()}
    viewModel{DeezerGenreViewModel()}
    viewModel{DeezerGenresViewModel()}
    viewModel{DeezerArtistsViewModel()}
    viewModel{DeezerAlbumViewModel()}

}