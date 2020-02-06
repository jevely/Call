package com.flash.light.free.good.fashioncallflash.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.flash.light.free.good.fashioncallflash.R
import com.flash.light.free.good.fashioncallflash.adapter.MainAdapter
import com.flash.light.free.good.fashioncallflash.adapter.ThemeAdapter
import com.flash.light.free.good.fashioncallflash.tool.DataTool
import com.flash.light.free.good.fashioncallflash.view.NewGridManager

class ThemeFragment : Fragment() {

    private lateinit var fragment_recyclerview: RecyclerView
    private lateinit var adapter: ThemeAdapter

    companion object {
        fun newInstance(position: Int): ThemeFragment {
            val args = Bundle()
            args.putInt("position", position)
            val fragment = ThemeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments!!.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(R.layout.themefragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_recyclerview = view.findViewById(R.id.fragment_recyclerview)
        fragment_recyclerview.layoutManager = NewGridManager(context, 2)
        adapter = ThemeAdapter(DataTool.getInstance().allTheme[position])
        fragment_recyclerview.adapter = adapter
        adapter.classifyPosition(position)
    }


}