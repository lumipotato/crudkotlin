package com.lumi.employeeoftheweek.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lumi.employeeoftheweek.R
import com.lumi.employeeoftheweek.adapter.MemberAdapter
import com.lumi.employeeoftheweek.db.MemberHelper
import com.lumi.employeeoftheweek.entity.Member
import com.lumi.employeeoftheweek.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

        private lateinit var adapter: MemberAdapter
        private lateinit var memberHelper: MemberHelper

        companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

            supportActionBar?.title = "Our Employee"
            rv_members.layoutManager = LinearLayoutManager(this)
            rv_members.setHasFixedSize(true)
            adapter = MemberAdapter(this)
            rv_members.adapter = adapter

            memberHelper = MemberHelper.getInstance(applicationContext)
            memberHelper.open()

            fab_add.setOnClickListener {
                val intent = Intent(this@MainActivity, MemberAddUpdateActivity::class.java)
                startActivityForResult(intent, MemberAddUpdateActivity.REQUEST_ADD)
            }

            if (savedInstanceState == null) {
                loadMembersAsync()

            } else {
                val list = savedInstanceState.getParcelableArrayList<Member>(EXTRA_STATE)
                if (list != null) {
                    adapter.listMembers = list
                }
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMembers)
    }

    private fun loadMembersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredMembers = async(Dispatchers.IO) {
                val cursor = memberHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            progressbar.visibility = View.INVISIBLE

            val members = deferredMembers.await()

            if (members.size > 0) {
                adapter.listMembers = members

            } else {
                adapter.listMembers = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                MemberAddUpdateActivity.REQUEST_ADD -> if (resultCode == MemberAddUpdateActivity.RESULT_ADD) {
                    val member = data.getParcelableExtra<Member>(MemberAddUpdateActivity.EXTRA_MEMBER)

                    adapter.addItem(member!!)
                    rv_members.smoothScrollToPosition(adapter.itemCount - 1)
                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }

                MemberAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        MemberAddUpdateActivity.RESULT_UPDATE -> {
                            val member = data.getParcelableExtra<Member>(MemberAddUpdateActivity.EXTRA_MEMBER)
                            val position = data.getIntExtra(MemberAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.updateItem(position, member!!)
                            rv_members.smoothScrollToPosition(position)
                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        MemberAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(MemberAddUpdateActivity.EXTRA_POSITION, 0)
                            adapter.removeItem(position)
                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_members, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        memberHelper.close()
    }

}