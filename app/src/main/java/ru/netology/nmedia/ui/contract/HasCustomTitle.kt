package ru.netology.nmedia.ui.contract

import androidx.annotation.StringRes

interface HasCustomTitle {
    @StringRes
    fun getTitleRes(): Int
}