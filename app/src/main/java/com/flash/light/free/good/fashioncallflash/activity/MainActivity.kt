package com.flash.light.free.good.fashioncallflash.activity

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.view.NewGridManager

class MainActivity : BaseActivity() {

    private lateinit var main_recyclerview: RecyclerView
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        Log.d("LJW","main activity")
    }

    private fun init() {
        main_recyclerview = findViewById(R.id.main_recyclerview)
        main_recyclerview.layoutManager = NewGridManager(this, 2)

        adapter = MainAdapter(this)

        main_recyclerview.adapter = adapter

    }

}
