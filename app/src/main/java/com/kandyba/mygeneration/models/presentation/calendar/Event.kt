package com.kandyba.mygeneration.models.presentation.calendar

import android.os.Parcel
import android.os.Parcelable

data class Event(
    val id: String?,
    val name: String?,
    val description: String?,
    val startDay: String?,
    val startTime: String?,
    val finishTime: String?
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    constructor(): this(null, null, null, null, null, null)

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
        dest?.writeString(description)
        dest?.writeString(startDay)
        dest?.writeString(startTime)
        dest?.writeString(finishTime)
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}