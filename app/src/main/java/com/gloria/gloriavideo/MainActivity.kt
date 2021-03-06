package com.gloria.gloriavideo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import com.gloria.common.card.CardLayout
import com.gloria.common.network.VideoApi
import com.gloria.common.network.VideoBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var linearLayout: LinearLayout? = null
    private var btnSortDefault: Button? = null
    private var btnSortLike: Button? = null

    private var scope: CoroutineScope? = null
    private var videoList: List<VideoBean>? = null
    private var sortByLike = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();// 隐藏ActionBar
        setContentView(R.layout.activity_main)

        scope = MainScope()
        initView()
        initAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope?.cancel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val url = data?.getStringExtra("url")
        val likecount = data?.getStringExtra("likecount")
        for (item in videoList!!) {
            if (item.feedurl == url) {
                if (likecount != null) {
                    item.addLikeCount(likecount)
                }
                break
            }
        }
        setVideoData()
    }

    private fun initView() {
        linearLayout = findViewById(R.id.ll_show_cards)
        btnSortDefault = findViewById(R.id.btn_sort_default)
        btnSortLike = findViewById(R.id.btn_sort_like)
    }

    private fun initAction() {
        getVideoData()
        btnSortDefault?.setOnClickListener {
            sortByLike = false
            scope?.launch { setVideoData() }
        }
        btnSortLike?.setOnClickListener {
            sortByLike = true
            scope?.launch { setVideoData() }
        }
    }

    private fun getVideoData() {
        scope?.launch {
            try {
                videoList = VideoApi.createVideoApi().requestData()
                Log.d(TAG, videoList.toString())
                sortByLike = false
                setVideoData()
            }catch (e: Exception){
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun setVideoData() {
        var showVideoList = ArrayList<VideoBean>()
        if (sortByLike){
            for (item in videoList!!){
                showVideoList.add(item)
            }
            Collections.sort(showVideoList, object : Comparator<VideoBean> {
                override fun compare(p0: VideoBean?, p1: VideoBean?): Int {
                    if (p0?.getLikeInt()!! > p1?.getLikeInt()!!){
                        return -1
                    }
                    if (p0?.getLikeInt()!! < p1?.getLikeInt()!!){
                        return 1
                    }
                    return 0
                }
            })
        }else{
            showVideoList = videoList as ArrayList<VideoBean>
        }
        linearLayout?.removeAllViews()
        for(item in showVideoList!!) {
            val card = CardLayout(this, null)
            linearLayout?.addView(card)
            card.setVideoBean(item)

            card.setOnClickListener {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("url", item.feedurl)
                startActivityForResult(intent, 1)
            }
        }
    }
}