package com.github.dragon925.androidlearning.datasources.contract

interface CategoryContract : ModelContract {
    val id: String
    val name: String
    val nameEn: String
    val image: String
}