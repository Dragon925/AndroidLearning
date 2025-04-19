package com.github.dragon925.androidlearning.profile.data.repositories

import com.github.dragon925.androidlearning.profile.domain.repositories.ProfileRepository
import com.github.dragon925.androidlearning.profile.domain.models.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.LocalDate

internal object ProfileRepositoryImpl : ProfileRepository {

    override fun loadProfile(id: String): Flow<Profile> = flowOf(getSampleData(id))

    private fun getSampleData(id: String) = Profile(
        id = id,
        name = "Константинов Денис",
        birthday = LocalDate(1980, 2, 1),
        fieldOfActivity = "Хирургия, травматология",
        avatar = "file:///android_asset/images/image_man.png",
        friends = listOf(
            Profile(
                id = "1",
                name = "Дмитрий Валерьевич",
                birthday = LocalDate(1990, 1, 1),
                avatar = "file:///android_asset/images/avatar_1.png"
            ),
            Profile(
                id = "2",
                name = "Евгений Александров",
                birthday = LocalDate(1990, 1, 1),
                avatar = "file:///android_asset/images/avatar_2.png"
            ),
            Profile(
                id = "3",
                name = "Виктор Кузнецов",
                birthday = LocalDate(1990, 1, 1),
                avatar = "file:///android_asset/images/avatar_3.png"
            ),
        )
    )
}