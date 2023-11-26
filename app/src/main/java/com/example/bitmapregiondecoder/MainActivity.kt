package com.example.bitmapregiondecoder

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var regionLoadImageView: RegionLoadImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        regionLoadImageView = findViewById(R.id.iv_cover)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            regionLoadImageView?.setImageRes(R.raw.super_long)
        }
    }
}