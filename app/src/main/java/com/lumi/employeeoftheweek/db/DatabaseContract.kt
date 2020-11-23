package com.lumi.employeeoftheweek.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class MemberColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "member"
            const val _ID = "_id"
            const val NAME = "name"
            const val DESCRIPTION = "description"
            const val DATE = "date"
        }
    }
}