package com.lumi.employeeoftheweek.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lumi.employeeoftheweek.R
import com.lumi.employeeoftheweek.db.DatabaseContract
import com.lumi.employeeoftheweek.db.DatabaseContract.MemberColumns.Companion.DATE
import com.lumi.employeeoftheweek.db.MemberHelper
import com.lumi.employeeoftheweek.entity.Member
import kotlinx.android.synthetic.main.activity_member_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class MemberAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var isEdit = false
    private var member: Member? = null
    private var position: Int = 0
    private lateinit var memberHelper: MemberHelper

    companion object {
        const val EXTRA_MEMBER = "extra_member"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_add_update)

        memberHelper = MemberHelper.getInstance(applicationContext)
        memberHelper.open()

        member = intent.getParcelableExtra(EXTRA_MEMBER)
        if (member != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true
        } else {
            member = Member()
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah Data"
            btnTitle = "Update"

            member?.let {
                edt_name.setText(it.name)
                edt_description.setText(it.description)
            }

        } else {
            actionBarTitle = "Tambah Karyawan"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_submit) {
            val name = edt_name.text.toString().trim()
            val description = edt_description.text.toString().trim()
            if (name.isEmpty()) {
                edt_name.error = "Field can not be blank"
                return
            }

            member?.name = name
            member?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_MEMBER, member)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.MemberColumns.NAME, name)
            values.put(DatabaseContract.MemberColumns.DESCRIPTION, description)

            if (isEdit) {
                val result = memberHelper.update(member?.id.toString(), values).toLong()
                if (result > 0) {
                    setResult(RESULT_UPDATE, intent)
                    finish()
                } else {
                    Toast.makeText(this@MemberAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
                }
            } else {
                member?.date = getCurrentDate()
                values.put(DATE, getCurrentDate())
                val result = memberHelper.insert(values)
                if (result > 0) {
                    member?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                } else {
                    Toast.makeText(this@MemberAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus karyawan ini?"
            dialogTitle = "Hapus Karyawan"
        }
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val result = memberHelper.deleteById(member?.id.toString()).toLong()
                    if (result > 0) {
                        val intent = Intent()
                        intent.putExtra(EXTRA_POSITION, position)
                        setResult(RESULT_DELETE, intent)
                        finish()
                    } else {
                        Toast.makeText(this@MemberAddUpdateActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }
}