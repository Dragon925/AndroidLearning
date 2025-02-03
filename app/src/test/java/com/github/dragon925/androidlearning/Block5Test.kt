package com.github.dragon925.androidlearning

import org.junit.Test

class Block5Test {

    @Test
    fun block5_task4() {
        val user = User(1, "Alex", 25,  Type.DEMO)
        println(user.startTime)
        Thread.sleep(1000)
        println(user.startTime)
    }

    @Test
    fun block5_task7() {
        println(usersName.first())
        println(usersName.last())
    }

    @Test
    fun block5_authWithAdultUser() {
        val user = User(1, "Alex", 25,  Type.DEMO)
        doAction(Action.Login(user))
    }

    @Test
    fun block5_authWithChildUser() {
        val user = User(1, "Alex", 15,  Type.DEMO)
        doAction(Action.Login(user))
    }
}