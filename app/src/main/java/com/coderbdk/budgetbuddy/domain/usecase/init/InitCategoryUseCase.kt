package com.coderbdk.budgetbuddy.domain.usecase.init

import android.content.Context
import com.coderbdk.budgetbuddy.data.repository.InitRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InitCategoryUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val initRepository: InitRepository
) {
    suspend operator fun invoke() {
        initRepository.initCategory(context)
    }
}