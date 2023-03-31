package uz.univer.argis.models

import androidx.annotation.ColorRes
import uz.univer.argis.R

data class Type(
    val name: String,
    @ColorRes
    val color: Int
)

val yerTurlari = listOf<Type>(
    Type("Shudgor", R.color.light_yellow),
    Type("Paxta", R.color.green),
    Type("G'alla", R.color.dark_yellow),
    Type("Bog' zor, Uzim zor", R.color.light_green),
    Type("Dala, tomorqa", R.color.brown),
    Type("Dukkaklik o'simliklar", R.color.light_blue),
    Type("Moyli o'simliklar", R.color.violet),
    Type("Poliz ekinlari", R.color.orange),
)