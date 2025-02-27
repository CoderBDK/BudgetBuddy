package com.coderbdk.budgetbuddy.utils

object TextUtils {
    fun String.capitalizeFirstLetter(): String {
        return if (this.isNotEmpty()) {
            this[0].uppercase() + this.substring(1)
        } else {
            this
        }
    }
}