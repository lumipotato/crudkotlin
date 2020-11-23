package com.lumi.employeeoftheweek.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Member(
    var id: Int = 0,
    var name: String? = null,
    var description: String? = null,
    var date: String? = null

    ):Parcelable