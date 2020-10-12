package com.example.kotlinyoutube

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        recyclerView_main.layoutManager = LinearLayoutManager(this)
        recyclerView_main.adapter = VideoAdapter()
    }

    private class VideoAdapter: RecyclerView.Adapter<VideoDetailViewHolder>() {

        override fun getItemCount(): Int {
            return 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoDetailViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val customView = layoutInflater.inflate(R.layout.video_detail_row, parent, false)
            return VideoDetailViewHolder(customView)
        }

        override fun onBindViewHolder(holder: VideoDetailViewHolder, position: Int) {

        }
    }

    private class VideoDetailViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    }
}