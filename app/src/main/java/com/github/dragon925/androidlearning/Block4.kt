package com.github.dragon925.androidlearning


// Задание 1

interface Publication {

    val price: Double
    val wordCount: Int

    fun getType(): String
}

// Задание 2

class Book(
    val name: String,
    override val price: Double,
    override val wordCount: Int,
) : Publication {

    override fun getType(): String = when {
        wordCount <= 1000 -> "Flash Fiction"
        wordCount <= 7500 -> "Short Story"
        else -> "Novel"
    }

    // Задание 3
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Book

        if (price != other.price) return false
        if (wordCount != other.wordCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = price.hashCode()
        result = 31 * result + wordCount
        return result
    }


}

class Magazine(
    val name: String,
    override val price: Double,
    override val wordCount: Int,
) : Publication {

    override fun getType(): String = "Magazine"
}

// Задание 4

fun buy(publication: Publication) {
    println("The purchase is complete. The purchase amount was ${publication.price}€")
}

// Задание 4

val sum: (Int, Int) -> Unit = { a: Int, b: Int -> println(a + b) }