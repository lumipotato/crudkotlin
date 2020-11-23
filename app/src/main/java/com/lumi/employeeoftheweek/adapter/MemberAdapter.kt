package com.lumi.employeeoftheweek.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lumi.employeeoftheweek.R
import com.lumi.employeeoftheweek.entity.Member
import com.lumi.employeeoftheweek.ui.MemberAddUpdateActivity
import kotlinx.android.synthetic.main.item_member.view.*

class MemberAdapter(private val activity: Activity): RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    var listMembers = ArrayList<Member>()
        set(listMembers) {
            if (listMembers.size > 0) {
                this.listMembers.clear()
            }
            this.listMembers.addAll(listMembers)
            notifyDataSetChanged()
        }

    fun addItem(member: Member) {
        this.listMembers.add(member)
        notifyItemInserted(this.listMembers.size - 1)
    }
    fun updateItem(position: Int, member: Member) {
        this.listMembers[position] = member
        notifyItemChanged(position, member)
    }
    fun removeItem(position: Int) {
        this.listMembers.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMembers.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        return MemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(listMembers[position])
    }

    override fun getItemCount(): Int = this.listMembers.size

    inner class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(member: Member) {
            with(itemView){
                tv_item_name.text = member.name
                tv_item_date.text = member.date
                tv_item_description.text = member.description
                cv_item_member.setOnClickListener(CustomOnItemClickListener(adapterPosition, object : CustomOnItemClickListener.OnItemClickCallback {
                    override fun onItemClicked(view: View, position: Int) {
                        val intent = Intent(activity, MemberAddUpdateActivity::class.java)
                        intent.putExtra(MemberAddUpdateActivity.EXTRA_POSITION, position)
                        intent.putExtra(MemberAddUpdateActivity.EXTRA_MEMBER, member)
                        activity.startActivityForResult(intent, MemberAddUpdateActivity.REQUEST_UPDATE)
                    }
                }))
            }
        }
    }
}