package com.github.dragon925.androidlearning.common.contract

fun interface Mapper<I, O> {

    operator fun invoke(input: I): O
}