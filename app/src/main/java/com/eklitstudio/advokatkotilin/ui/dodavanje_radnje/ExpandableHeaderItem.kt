package com.eklitstudio.advokatkotilin.ui.dodavanje_radnje

import android.graphics.drawable.Animatable
import android.view.View
import com.eklitstudio.advokatkotilin.R
import com.eklitstudio.advokatkotilin.data.db.entity.Tarifa
import com.xwray.groupie.ExpandableGroup
import com.xwray.groupie.ExpandableItem
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.expandable_item_naslov_tarife.*

/**
 * Ovaj deo se nalazi bas u biblioteci groupie u jednom od primera
 */
class ExpandableHeaderItem(private val tarifa: Tarifa) : HeaderItem(tarifa.nazivTarife), ExpandableItem {

    var clickListener: ((ExpandableHeaderItem) -> Unit)? = null
    private lateinit var expandableGroup: ExpandableGroup

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        super.bind(viewHolder, position)
        //viewHolder.item_expandable_header_title.text = tarifa.nazivTarife.toString()

        viewHolder.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.colapse else R.drawable.expand)
            setOnClickListener{
                expandableGroup.onToggleExpanded()
                bindIcon(viewHolder)
            }
        }

        viewHolder.itemView.setOnClickListener {
            //expandableGroup.onToggleExpanded()
            clickListener?.invoke(this)
        }
    }

    private fun bindIcon(viewHolder: GroupieViewHolder) {
        viewHolder.icon.apply {
            visibility = View.VISIBLE
            setImageResource(if (expandableGroup.isExpanded) R.drawable.collapse_animated else R.drawable.expand_animated)
            (drawable as Animatable).start()
        }
    }

   // override fun getLayout() = R.layout.expandable_item_naslov_tarife


    override fun setExpandableGroup(onToggleListener: ExpandableGroup) {
        expandableGroup = onToggleListener
    }


}