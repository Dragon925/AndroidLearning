package com.github.dragon925.androidlearning.common.data

import com.github.dragon925.androidlearning.common.domain.Event
import com.github.dragon925.androidlearning.common.domain.Member
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.datetime.LocalDate
import java.lang.reflect.Type

class EventDeserializer : JsonDeserializer<Event> {

    private val dateFormat = LocalDate.Format {
        year(); chars("-"); monthNumber(); chars("-"); dayOfMonth()
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Event = json?.asJsonObject?.let { event ->
        Event(
            id = event[Event.ID].asInt,
            name = event[Event.NAME].asString,
            description = event[Event.DESCRIPTION].asString,
            organizer = event[Event.ORGANIZER].asString,
            categoryIds = event[Event.CATEGORY_IDS].asJsonArray.map { it.asInt },
            address = event[Event.ADDRESS].asString,
            phoneNumbers = event[Event.PHONE_NUMBERS].asJsonArray.map { it.asString },
            email = event[Event.EMAIL].asString,
            website = event[Event.WEBSITE].asString,
            startDate = dateFormat.parse(event[Event.START_DATE].asString),
            endDate = dateFormat.parse(event[Event.END_DATE].asString),
            photos = event[Event.PHOTOS].asJsonArray.map { it.asString },
            members = event[Event.MEMBERS].asJsonArray.takeIf { context != null }
                ?.map { context!!.deserialize(it, Member::class.java) }
                ?: emptyList()
        )
    } ?: throw IllegalArgumentException("Event is null")
}