package com.flash.light.free.good.fashioncallflash.adapter

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.flash.light.free.good.fashioncallflash.CallApplication
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.activity.ShowActivity
import com.flash.light.free.good.fashioncallflash.db.ThemeContent
import com.flash.light.free.good.fashioncallflash.util.Logger
import com.flash.light.free.good.fashioncallflash.util.getScreen

class ThemeAdapter(val list: MutableList<ThemeContent>) :
    RecyclerView.Adapter<ThemeAdapter.MainViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(CallApplication.getContext())
                .inflate(R.layout.main_recycler_layout, parent, false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.setData(list[position], position)
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val main_background = itemView.findViewById<ImageView>(R.id.main_background)
        val main_re = itemView.findViewById<RelativeLayout>(R.id.main_re)

        fun setData(content: ThemeContent, position: Int) {

            Glide
                .with(CallApplication.getContext())
                .load(content.image_url)
                .placeholder(R.mipmap.defult_img)
                .override((getScreen().x / 2.0F).toInt(), (getScreen().y / 2.0F).toInt())
                .centerCrop()
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Logger.d("load img fail : ${content.image_url}")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .into(main_background)

            main_re.setOnClickListener {
                val intent = Intent(CallApplication.getContext(), ShowActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("classify", classifyPosition)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                CallApplication.getContext().startActivity(intent)
            }
        }
    }

    private var classifyPosition = 0
    fun classifyPosition(position: Int) {
        classifyPosition = position
    }

}