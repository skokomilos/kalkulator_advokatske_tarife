package com.eklitstudio.advokatkotilin.ui.dodavanje_radnje

import android.view.View
import androidx.annotation.DrawableRes
import com.eklitstudio.advokatkotilin.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.expandable_item_naslov_tarife.*

open class HeaderItem(private val title: String,
                 @DrawableRes private val iconResId: Int? = null,
                 private val onIconClickListener: View.OnClickListener? = null) : Item() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.item_expandable_header_title.setText(title)

        viewHolder.icon.apply {
            visibility = View.GONE
            iconResId?.let {
                visibility = View.VISIBLE
                setImageResource(it)
                setOnClickListener(onIconClickListener)
            }
        }
    }

    override fun getLayout() = R.layout.expandable_item_naslov_tarife
}