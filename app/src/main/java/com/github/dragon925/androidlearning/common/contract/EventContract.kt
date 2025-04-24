package com.github.dragon925.androidlearning.common.contract

interface EventContract : ModelContract {
    val id: String
    val name: String
    val startDate: Long
    val endDate: Long
    val description: String
    val status: Long
    val photos: List<String>
    val categories: List<String>
    val createAt: Long
    val phone: String
    val address: String
    val organization: String
}