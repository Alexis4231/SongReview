package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.PublicReview
import com.app.domain.model.Review
import com.app.domain.usecase.review.DeleteReviewUseCase
import com.app.domain.usecase.review.GetReviewsByCodeSongUseCase
import com.app.domain.usecase.review.GetReviewsByCodeUserUseCase
import com.app.domain.usecase.review.GetReviewsByUsernameFollowerUseCase
import com.app.domain.usecase.review.GetReviewsUseCase
import com.app.domain.usecase.review.SaveReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val saveReviewUseCase: SaveReviewUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val getReviewsByCodeSongUseCase: GetReviewsByCodeSongUseCase,
    private val getReviewsByCodeUserUseCase: GetReviewsByCodeUserUseCase,
    private val getReviewsByUsernameFollowerUserCase: GetReviewsByUsernameFollowerUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase
):ViewModel() {
    private val _review = MutableStateFlow(Review(codeUser = "", publicReview = PublicReview(codeSong = "")))
    val review: StateFlow<Review> = _review

    private val _publicReview = MutableStateFlow(PublicReview())
    val publicReview: StateFlow<PublicReview> = _publicReview

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    private val _publicReviews  = MutableStateFlow<List<PublicReview>>(emptyList())
    val publicReviews: StateFlow<List<PublicReview>> = _publicReviews.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _deleteComplete = MutableStateFlow(false)
    val deleteComplete = _deleteComplete.asStateFlow()

    fun setCodeUser(codeUser: String) {
        _review.value = _review.value.copy(
            codeUser = codeUser
        )
    }

    fun setPublicReview(publicReview: PublicReview){
        _publicReview.value = publicReview
        _review.value = _review.value.copy(publicReview = publicReview)
    }

    fun save() {
        viewModelScope.launch {
            saveReviewUseCase(review.value)
        }
    }

    fun getReviews(){
        viewModelScope.launch {
            _publicReviews.value = getReviewsUseCase()
        }
    }

    fun getReviewByCodeSong(codeSong: String){
        viewModelScope.launch {
            _isLoading.value = true
            _publicReviews.value = getReviewsByCodeSongUseCase(codeSong)
            _isLoading.value = false
        }
    }

    fun getReviewsByCodeUser(codeUser: String){
        viewModelScope.launch {
            _isLoading.value = true
            _reviews.value = getReviewsByCodeUserUseCase(codeUser)
            _isLoading.value = false
        }
    }

    fun getReviewsByUsernameFollower(username: String){
        viewModelScope.launch {
            _isLoading.value = true
            _publicReviews.value = getReviewsByUsernameFollowerUserCase(username)
            _isLoading.value = false
        }
    }

    fun deleteReview(publicReview: PublicReview){
        viewModelScope.launch {
            deleteReviewUseCase(publicReview)
            _deleteComplete.value = true
        }
    }
}