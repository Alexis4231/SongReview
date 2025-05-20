package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.domain.model.Review
import com.app.domain.usecase.review.GetReviewsByCodeSongUseCase
import com.app.domain.usecase.review.GetReviewsByCodeUserUseCase
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
    private val getReviewsByCodeUserUseCase: GetReviewsByCodeUserUseCase
):ViewModel() {
    private val _review = MutableStateFlow(Review())
    val review: StateFlow<Review> = _review

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()

    fun setCodeUser(codeUser: String) {
        _review.value = _review.value.copy(
            codeUser = codeUser
        )
    }

    fun setCodeSong(codeSong: String) {
        _review.value = _review.value.copy(
            codeSong = codeSong
        )
    }

    fun setRating(rating: Int) {
        _review.value = _review.value.copy(
            rating = rating
        )
    }

    fun setComment(comment: String) {
        _review.value = _review.value.copy(
            comment = comment
        )
    }

    fun save() {
        viewModelScope.launch {
            saveReviewUseCase(review.value)
        }
    }

    fun getReviews(){
        viewModelScope.launch {
            _reviews.value = getReviewsUseCase()
        }
    }

    fun getReviewByCodeSong(codeSong: String){
        viewModelScope.launch {
            _reviews.value = getReviewsByCodeSongUseCase(codeSong)
        }
    }

    fun getReviewsByCodeUser(codeUser: String){
        viewModelScope.launch {
            _reviews.value = getReviewsByCodeUserUseCase(codeUser)
        }
    }
}