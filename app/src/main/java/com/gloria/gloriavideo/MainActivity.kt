package com.gloria.gloriavideo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.gloria.common.card.CardLayout
import com.gloria.common.network.VideoApi
import com.gloria.common.network.VideoBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private var linearLayout: LinearLayout? = null
    private var scope: CoroutineScope? = null
    private var videoList: List<VideoBean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();// 隐藏ActionBar
        setContentView(R.layout.activity_main)

        initView()
        initAction()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope?.cancel()
    }

    private fun initView() {
        linearLayout = findViewById(R.id.ll_show_cards)
    }

    private fun initAction() {
        getVideoData()
    }

    private fun getVideoData() {
        scope = MainScope()
        scope?.launch {
            try {
                videoList = VideoApi.createVideoApi().requestData()
                Log.d(TAG, videoList.toString())
                setVideoData()
            }catch (e: Exception){
                e.printStackTrace()
                Log.e(TAG, e.toString())
            }
        }
    }

    private fun setVideoData() {
        for(item in videoList!!) {
            val card = CardLayout(this, null)
            linearLayout?.addView(card)
            card.setVideoBean(item)

            card.setOnClickListener {
                val intent = Intent(applicationContext, DetailActivity::class.java)
                intent.putExtra("url", item.feedurl)
                startActivity(intent)
            }
        }
    }
}