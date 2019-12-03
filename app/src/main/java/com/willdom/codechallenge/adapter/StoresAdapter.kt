package com.willdom.codechallenge.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.willdom.codechallenge.R
import com.willdom.codechallenge.model.entity.Store
import kotlinx.android.synthetic.main.store_item.view.*
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import android.content.Intent
import com.willdom.codechallenge.activity.StoreActivity


class StoresAdapter(private  var context: Context, private  var data: List<Store>, var onSelected: (m: Store, view:View) -> Unit) : RecyclerView.Adapter<StoresAdapter.ViewHolder>() {

    private  var mInflater: LayoutInflater = LayoutInflater.from(context)

    // inflates the cell layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.store_item, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the textview in each cell
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    // total number of cells
    override fun getItemCount(): Int {
        return data.size
    }


    // stores and recycles views as they are scrolled off screen
    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {


        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            onSelected(itemView.tag as Store, itemView.image)
        }


        fun bind(store: Store) {

            val myOptions = RequestOptions()
                .placeholder(R.drawable.ic_photo)
                /*.transforms(
                    CenterCrop(),
                    //RoundedCorners(context.resources.getDimension(R.dimen.round_borders).toInt())
                    RoundedCorners(50)
                )*/

            Glide.with(context)
                .load(store.storeLogoURL)
                .apply(myOptions)
                .into(itemView.image)

            itemView.phone_number.text = store.phone
            itemView.address.text = store.address
            itemView.tag = store
        }
    }


    // convenience method for getting data at click position
    internal fun getItem(id: Int): String {
        return data[id].name

    }
}