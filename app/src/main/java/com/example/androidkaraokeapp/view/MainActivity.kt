package com.example.androidkaraokeapp.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.os.Environment.isExternalStorageRemovable
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.ulti.HandleDiskLRUCache
import com.example.androidkaraokeapp.view.fragment.FavoriteSongFragment
import com.example.androidkaraokeapp.view.fragment.ListSongFragment
import com.example.androidkaraokeapp.view.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.graphics.drawable.BitmapDrawable


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment = when (item.itemId) {
            R.id.navigation_song -> ListSongFragment()
            R.id.navigation_favorite -> FavoriteSongFragment()
            R.id.navigation_user -> UserFragment()
            else -> ListSongFragment()
        }
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        ft.replace(R.id.fragment_container, fragment)
        ft.commit()
        return@OnNavigationItemSelectedListener true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ListSongFragment()).commit()



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 0)
        }
        configureDiskLRUCache()
    }

    private fun configureDiskLRUCache() {
        HandleDiskLRUCache.initDiskLRUCache(this)
    }

}