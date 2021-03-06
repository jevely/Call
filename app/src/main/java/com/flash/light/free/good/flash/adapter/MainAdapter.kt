package com.flash.light.free.good.flash.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.alite.qeuaed.manager.NliManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.ads.AdOptionsView
import com.facebook.ads.MediaView
import com.facebook.ads.NativeAdLayout
import com.flash.light.free.good.flash.CallApplication
import com.flash.light.free.good.flash.R
import com.flash.light.free.good.flash.activity.ContentActivity
import com.flash.light.free.good.flash.db.ThemeContent
import com.flash.light.free.good.flash.tool.DataTool
import com.flash.light.free.good.flash.util.DeviceUtils
import com.flash.light.free.good.flash.util.Logger

class MainAdapter(val context: Activity, val addClickCallBack: AddClickCallBack) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var requestTime = 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(CallApplication.getContext())
                    .inflate(R.layout.main_recycler_layout, parent, false)
            return MainViewHolder(view)
        } else if (viewType == 2) {
            val view = LayoutInflater.from(CallApplication.getContext())
                .inflate(R.layout.facebook_native_layout, parent, false)
            return AdViewHolder(view)
        } else {
            val view = LayoutInflater.from(CallApplication.getContext())
                .inflate(R.layout.adapter_add_layout, parent, false)
            return AddViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == DataTool.getInstance().themeList.size - 1) {
            return 3
        } else if (position % 5 == 0) {
            val content = DataTool.getInstance().themeList[position]
            if (content.fbNativeAd == null) {
                if (System.currentTimeMillis() - requestTime > 10000) {
                    requestTime = System.currentTimeMillis()
                    val newFbNativeAd = NliManager.getFbAppAd()
                    if (newFbNativeAd != null) {
                        content.fbNativeAd = newFbNativeAd
                        Logger.d("我是列表中的广告 getItemViewType")
                        return 2
                    }
                }
            } else {
                Logger.d("我是列表中的广告 getItemViewType 直接是现成的")
                return 2
            }
        }
        return 1
    }


    override fun getItemCount(): Int {
        return DataTool.getInstance().themeList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            holder.setData(DataTool.getInstance().themeList[position], position)
        } else if (holder is AdViewHolder) {
            Logger.d("我是列表中的广告 onBindViewHolder")
            holder.initAd(DataTool.getInstance().themeList[position])
        } else if (holder is AddViewHolder) {
            holder.setData()
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun initAd(content: ThemeContent) {
            try {
                val nativeAd = content.fbNativeAd ?: return

                nativeAd.unregisterView()

                val parentView = itemView

                val adView =
                    parentView.findViewById<NativeAdLayout>(R.id.iuw_moni_native_ad_container)

                // Add the AdOptionsView
                val adChoicesContainer =
                    adView.findViewById<LinearLayout>(R.id.iuw_moni_ad_choices_container)

                val adOptionsView = AdOptionsView(CallApplication.getContext(), nativeAd, adView)
                adChoicesContainer.removeAllViews()
                adChoicesContainer.addView(adOptionsView, 0)

                // Create native UI using the ad metadata.
                val nativeAdIcon = adView.findViewById<MediaView>(R.id.iuw_moni_native_ad_icon)
                val nativeAdTitle = adView.findViewById<TextView>(R.id.iuw_moni_native_ad_title)
                val nativeAdMedia = adView.findViewById<MediaView>(R.id.iuw_moni_native_ad_media)
                val nativeAdCallToAction =
                    adView.findViewById<Button>(R.id.iuw_moni_native_ad_call_to_action)
                val adsocial = adView.findViewById<TextView>(R.id.iuw_moni_adsocial)
                val nl_fb_content1 = adView.findViewById<TextView>(R.id.iuw_moni_nl_fb_content1)
                val nl_fb_content2 = adView.findViewById<TextView>(R.id.iuw_moni_nl_fb_content2)
                val nl_bf_parent = adView.findViewById<RelativeLayout>(R.id.iuw_moni_nl_bf_parent)
                val nl_fb_translation =
                    adView.findViewById<TextView>(R.id.iuw_moni_native_ad_translation)

                val title = nativeAd.advertiserName
                if (!TextUtils.isEmpty(title))
                    nativeAdTitle.text = title

                val translation = nativeAd.sponsoredTranslation
                if (!TextUtils.isEmpty(translation)) {
                    nl_fb_translation.text = translation
                }

                val social = nativeAd.adSocialContext
                if (!TextUtils.isEmpty(social)) {
                    adsocial.text = social
                }

                val head = nativeAd.adHeadline
                if (head != null && !TextUtils.isEmpty(head) && head.length > 20) {
                    nl_fb_content1.text = head
                } else {
                    nl_fb_content1.visibility = View.GONE

                    val layoutparams: RelativeLayout.LayoutParams =
                        nl_fb_content2.layoutParams as RelativeLayout.LayoutParams
                    layoutparams.topMargin = DeviceUtils.dp2px(CallApplication.getContext(), 16f)
                    nl_fb_content2.layoutParams = layoutparams
                }

                val body = nativeAd.adBodyText
                if (!TextUtils.isEmpty(body)) {
                    nl_fb_content2.text = body
                }

                if (nativeAd.hasCallToAction()) {
                    nativeAdCallToAction.visibility = View.VISIBLE
                    val action = nativeAd.adCallToAction
                    if (!TextUtils.isEmpty(action))
                        nativeAdCallToAction.text = action
                } else {
                    nativeAdCallToAction.visibility = View.INVISIBLE
                }

                // Create a list of clickable views
                val clickableViews = mutableListOf<View>()

                clickableViews.add(nativeAdIcon)

                clickableViews.add(nativeAdTitle)

                clickableViews.add(nl_fb_content1)
                clickableViews.add(nl_fb_content2)

                clickableViews.add(nl_bf_parent)


                clickableViews.add(nativeAdCallToAction)

                // Register the Title and CTA button to listen for clicks.
                //必须在主线程调用
                nativeAd.registerViewForInteraction(
                    adView,
                    nativeAdMedia,
                    nativeAdIcon,
                    clickableViews
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val main_background = itemView.findViewById<ImageView>(R.id.main_background)
        val main_re = itemView.findViewById<RelativeLayout>(R.id.main_re)

        fun setData(content: ThemeContent, position: Int) {

            Glide
                .with(CallApplication.getContext())
                .load(content.image_url)
                .placeholder(R.mipmap.defult_img)
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
                val intent = Intent(CallApplication.getContext(), ContentActivity::class.java)
                intent.putExtra("position", position)
                intent.putExtra("classify", -1)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                CallApplication.getContext().startActivity(intent)
            }
        }
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val adapter_add = itemView.findViewById<ImageView>(R.id.adapter_add)
        fun setData() {
            adapter_add.setOnClickListener {
                addClickCallBack.addClickCallBack()
            }
        }
    }

    interface AddClickCallBack {
        fun addClickCallBack()
    }

}