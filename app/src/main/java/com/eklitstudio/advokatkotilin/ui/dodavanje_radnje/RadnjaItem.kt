package com.eklitstudio.advokatkotilin.ui.dodavanje_radnje

import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.Radnja
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.radnja_item.*

class RadnjaItem(val radnja : Radnja) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.apply { radnja_item_naziv_radnje.text = radnja.naziv_radnje.toString()}
    }

    override fun getLayout() = R.layout.radnja_item

    override fun getSpanSize(spanCount: Int, position: Int) = spanCount / 1
}