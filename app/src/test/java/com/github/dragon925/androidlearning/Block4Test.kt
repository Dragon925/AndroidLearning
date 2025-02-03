package com.github.dragon925.androidlearning

import org.junit.Test

class Block4Test {

    @Test
    fun block4_task3() {
        val book1 = Book("Book1", 2.5, 2314)
        val book2 = Book("Book2", 12.2, 10000)
        val magazine = Magazine("Magazine1", 0.5, 1000)

        println("${book1.name}: ${book1.getType()}, ${book1.wordCount} words, ${book1.price}€")
        println("${book2.name}: ${book2.getType()}, ${book2.wordCount} words, ${book2.price}€")
        println("${magazine.name}: ${magazine.getType()}, ${magazine.wordCount} words, ${magazine.price}€")

        val book1Copy = Book("Book1", 2.5, 2314)
        println("Equals by link: ${ book1 === book1Copy }")
        println("Equals by method: ${ book1 == book1Copy }")
    }

    @Test
    fun block4_task4() {
        val book1: Book? = null
        val book2: Book? = Book("Book2", 12.2, 10000)

        book1?.let { buy(it) }
        book2?.let { buy(it) }
    }

    @Test
    fun block4_task5() {
        sum(1, 2)
    }
}