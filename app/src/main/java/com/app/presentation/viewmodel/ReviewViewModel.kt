package com.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.app.domain.model.Review
import com.app.domain.usecase.review.SaveReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow

class ReviewViewModel(
    private val saveReviewUseCase: SaveReviewUseCase
):ViewModel() {
    private val _review = MutableStateFlow(Review("","","",,0,""))
}