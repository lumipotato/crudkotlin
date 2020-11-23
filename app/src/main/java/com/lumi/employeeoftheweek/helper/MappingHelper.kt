package com.lumi.employeeoftheweek.helper

import android.database.Cursor
import com.lumi.employeeoftheweek.db.DatabaseContract
import com.lumi.employeeoftheweek.entity.Member

object MappingHelper {

    fun mapCursorToArrayList(membersCursor: Cursor?): ArrayList<Member> {
        val membersList = ArrayList<Member>()
        membersCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.MemberColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.MemberColumns.NAME))
                val description = getString(getColumnIndexOrThrow(DatabaseContract.MemberColumns.DESCRIPTION))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.MemberColumns.DATE))
                membersList.add(Member(id, title, description, date))
            }
        }
        return membersList
    }
}