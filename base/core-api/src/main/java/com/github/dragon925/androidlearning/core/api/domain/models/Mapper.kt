package com.github.dragon925.androidlearning.core.api.domain.models

fun interface Mapper<I, O> {

    operator fun invoke(input: I): O
}