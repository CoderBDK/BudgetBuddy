package com.coderbdk.budgetbuddy.domain.usecase.init

import android.content.Context
import com.coderbdk.budgetbuddy.data.repository.InitRepository
import javax.inject.Inject

class InitCategoryUseCase @Inject constructor(private val initRepository: InitRepository) {
    operator fun invoke(context: Context) {
        initRepository.initCategory(context)
    }
}