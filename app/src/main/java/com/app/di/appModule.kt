package com.app.di

import com.app.data.repository.UserFirestoreRepository
import com.app.domain.repository.UserRepository
import com.app.domain.usecase.user.DeleteUserUseCase
import com.app.domain.usecase.user.ExistUsernameUseCase
import com.app.domain.usecase.user.GetEmailByNameUseCase
import com.app.domain.usecase.user.GetUserByCodeUseCase
import com.app.presentation.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import GetUserDetailsViewModel
import com.app.data.repository.RequestFirestoreRepository
import com.app.data.repository.ReviewFirestoreRepository
import com.app.data.repository.SongFirestoreRepository
import com.app.domain.repository.RequestRepository
import com.app.domain.repository.ReviewRepository
import com.app.domain.repository.SongRepository
import com.app.domain.usecase.request.AcceptRequestUseCase
import com.app.domain.usecase.request.CancelRequestUseCase
import com.app.domain.usecase.request.DeleteRequestUseCase
import com.app.domain.usecase.request.GetFollowersUseCase
import com.app.domain.usecase.request.GetRequestFollowersUseCase
import com.app.domain.usecase.request.GetStatusUseCase
import com.app.domain.usecase.request.SaveRequestUseCase
import com.app.domain.usecase.review.GetReviewsByCodeSongUseCase
import com.app.domain.usecase.review.GetReviewsByCodeUserUseCase
import com.app.domain.usecase.review.GetReviewsByUsernameFollowerUseCase
import com.app.domain.usecase.review.GetReviewsUseCase
import com.app.domain.usecase.review.SaveReviewUseCase
import com.app.domain.usecase.song.GetCodeByTitleAndArtistUseCase
import com.app.domain.usecase.song.GetSongByCodeUseCase
import com.app.domain.usecase.song.GetSongsUseCase
import com.app.domain.usecase.song.SaveSongUseCase
import com.app.domain.usecase.user.GetUsersUseCase
import com.app.domain.usecase.user.UpdateFcmTokenUseCase
import com.app.presentation.viewmodel.DeezerAlbumViewModel
import com.app.presentation.viewmodel.DeezerArtistsViewModel
import com.app.presentation.viewmodel.DeezerGenreViewModel
import com.app.presentation.viewmodel.DeezerGenresViewModel
import com.app.presentation.viewmodel.RequestViewModel
import com.app.presentation.viewmodel.ReviewViewModel
import com.app.presentation.viewmodel.SongDBViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module{
    single { FirebaseFirestore.getInstance() }

    // Repository
    single<UserRepository> { UserFirestoreRepository(get()) }
    single<SongRepository> { SongFirestoreRepository(get()) }
    single<ReviewRepository> { ReviewFirestoreRepository(get()) }
    single<RequestRepository> { RequestFirestoreRepository(get()) }

    // UseCase
    factory { GetUserByCodeUseCase(get()) }
    factory { ExistUsernameUseCase(get()) }
    factory { DeleteUserUseCase(get()) }
    factory { GetEmailByNameUseCase(get()) }
    factory { GetUsersUseCase(get()) }
    factory { UpdateFcmTokenUseCase(get()) }
    factory { SaveSongUseCase(get()) }
    factory { GetSongsUseCase(get()) }
    factory { GetSongByCodeUseCase(get()) }
    factory { SaveReviewUseCase(get()) }
    factory { GetReviewsUseCase(get()) }
    factory { GetReviewsByCodeSongUseCase(get()) }
    factory { GetReviewsByCodeUserUseCase(get()) }
    factory { GetReviewsByUsernameFollowerUseCase(get()) }
    factory { GetCodeByTitleAndArtistUseCase(get()) }
    factory { SaveRequestUseCase(get()) }
    factory { GetStatusUseCase(get()) }
    factory { AcceptRequestUseCase(get()) }
    factory { DeleteRequestUseCase(get())}
    factory { CancelRequestUseCase(get()) }
    factory { GetFollowersUseCase(get()) }
    factory { GetRequestFollowersUseCase(get()) }


    // ViewModel
    viewModel { UserViewModel(get(),get(),get(),get(),get(),get()) }
    viewModel { GetUserDetailsViewModel() }
    viewModel { SongDBViewModel(get(),get(),get(),get()) }
    viewModel { ReviewViewModel(get(),get(),get(),get(),get()) }
    viewModel { RequestViewModel(get(),get(),get(),get(),get(),get(),get()) }
}