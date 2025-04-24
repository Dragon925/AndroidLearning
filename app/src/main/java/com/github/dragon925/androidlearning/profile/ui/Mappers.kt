package com.github.dragon925.androidlearning.profile.ui

import android.content.Context
import com.github.dragon925.androidlearning.R
import com.github.dragon925.androidlearning.profile.domain.models.Profile
import com.github.dragon925.androidlearning.profile.ui.models.FriendItem
import com.github.dragon925.androidlearning.profile.ui.models.ProfileUIState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun Profile.toFriendItem() = FriendItem(
    id = id,
    name = name,
    avatar = avatar
)

fun Profile.toProfileState(context: Context) = ProfileUIState(
    id = id,
    name = name,
    avatar = avatar,
    birthday = getDateString(context, birthday),
    fieldOfActivity = fieldOfActivity,
    friends = friends.map(Profile::toFriendItem)
)

private fun getDateString(context: Context, date: LocalDate): String {
    val dateFormat = LocalDate.Format {
        dayOfMonth()
        char(' ')
        monthName(
            MonthNames(context.resources
                .getStringArray(R.array.months_full_names)
                .toList())
        )
        char(' ')
        year()
    }
    return dateFormat.format(date)
}