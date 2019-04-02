package com.example.androidkaraokeapp.view

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.ulti.HandleMemoryStorage
import com.example.androidkaraokeapp.view.fragment.FavoriteSongFragment
import com.example.androidkaraokeapp.view.fragment.ListSongFragment
import com.example.androidkaraokeapp.view.fragment.UserFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val fragment: Fragment =  when (item.itemId) {
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

        configureSaveMemory()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun configureSaveMemory() {
        val folderName = HandleMemoryStorage.getInstance().faveSongFolderName
//        HandleMemoryStorage.getInstance().createFolder(folderName,this.applicationContext)
    }
}
