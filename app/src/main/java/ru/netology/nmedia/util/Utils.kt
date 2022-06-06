package ru.netology.nmedia.util

import kotlin.math.roundToInt

object Utils {

    fun formatValue(value: Int): String {
        val input = value / 1000
        val output: String = when {
            input < 1 -> {
                value.toString()
            }
            input in 1..99 -> {
                (((value.toDouble() / 1000) * 10.0).roundToInt() / 10.0).toString() + "K"
            }
            input in 100..1000 -> {
                ((value.toDouble() / 1000).roundToInt()).toString() + "K"
            }
            else -> {
                (((value.toDouble() / 1000000) * 10.0).roundToInt() / 10.0).toString() + "M"
            }
        }
        return output
    }
}