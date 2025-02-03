package com.github.dragon925.androidlearning

// Задание 2

enum class Type {
    DEMO, FULL
}

// Задание 3

data class User(
    val id: Int,
    val name: String,
    val age: Int,
    val type: Type
) {

    val startTime: Long by lazy { System.currentTimeMillis() }
}

// Задание 5

val userList = mutableListOf(User(1, "User1", 23, Type.DEMO)).apply {
    add(User(2, "User2", 12,  Type.FULL))
    add(User(3, "User3", 25, Type.DEMO))
    add(User(4, "User4", 26,  Type.FULL))
    add(User(5, "User5",13,  Type.DEMO))
    add(User(6, "User6",45,  Type.FULL))
    add(User(7, "User7",16, Type.DEMO))
    add(User(8, "User8",21, Type.FULL))
    add(User(9, "User9", 29, Type.DEMO))
    add(User(10, "User10",10, Type.FULL))
}

// Задание 6

val usersWithFullAccess = userList.filter { it.type == Type.FULL }

// Задание 7

val usersName = userList.map(User::name)

// Задание 8

fun User.isAdult() {
    if (age > 18) {
        println(this)
    } else {
        throw IllegalStateException("User is not adult")
    }
}

// Задание 9

interface AuthCallback {

    fun authSuccess()
    fun authFailed()
}

val authCallback: AuthCallback = object : AuthCallback {
    override fun authSuccess() {
        println("Auth success")
    }

    override fun authFailed() {
        println("Auth failed")
    }
}

// Задание 10, 11

inline fun auth(user: User, updateCache: () -> Unit) {
    try {
        user.isAdult()
        updateCache()
        authCallback.authSuccess()
    } catch (e: IllegalStateException) {
        authCallback.authFailed()
    }
}

// Задание 12

sealed class Action {

    class Registration() : Action()

    data class Login(val user: User) : Action()

    class Logout() : Action()
}

// Задание 13

fun doAction(action: Action) {
    when (action) {
        is Action.Registration -> println("Registration")
        is Action.Login -> {
            println("Login")
            auth(action.user) { println("Update cache") }
        }

        is Action.Logout -> println("Logout")
    }
}
