package com.example.androidkaraokeapp.ulti

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import android.content.Context.MODE_PRIVATE
import com.example.androidkaraokeapp.model.SongModel
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream


class HandleMemoryStorage {
    val db = FirebaseFirestore.getInstance()
    val faveSongFolderName = "fav_song"
    val faveSongSaveFile = "song"

    private var saveFolder: MutableMap<String,File> = hashMapOf()


    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile

        private var instance: HandleMemoryStorage? = null

//        const val INTENT_LIST_SONG = "list_song"
//        const val LIST_DETAIL_REQUEST_CODE = 123

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: HandleMemoryStorage().also { instance = it }
        }
    }

    fun saveFaveSongIntoFolder_InternalStorage (song:SongModel, folderName:String, context:Context) {
        val toastDuration = 1.toLong()

//        val fileName = song.name
        val file = File(saveFolder[folderName], faveSongSaveFile)
        val fileText = readFile(file)


        if (fileText!!.contains(song.name) && fileText.contains(song.singer))  {
            Handle_UI().toastWithDuration("Bài hát này đã được lưu",toastDuration,context.applicationContext)
        }
        else {
            FileOutputStream(file.path,true).use {
                                it.write(HandleGSON().SongModelToGSon(song)!!.toByteArray())
                                it.write(",".toByteArray())
            }
            Handle_UI().toastWithDuration("Lưu thành công!",toastDuration,context.applicationContext)
        }
//        if (file.exists()) {
//
//        }
//        else {
//
//        }
        println(fileText)
    }

    fun saveFaveSongIntoCache() {

    }

    fun createFolder(folderName:String, context: Context) {
        val newFolder = context.getDir(folderName, MODE_PRIVATE)  //Don't do
        if (!newFolder.exists())
            newFolder.mkdir()

        saveFolder[folderName] = newFolder


    }

    fun getDirSaveFolder(folderName:String): File? {
        return saveFolder[folderName]
    }

//    fun CacheConfigure( type: T): LruCache<String, T> {
//        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
//        val imageCache: LruCache<String, type>
//        val cacheSize = maxMemory / 1
//
//        imageCache = object : LruCache<String, type>(cacheSize) {
//            override fun sizeOf(key: String, bitmap: type): Int {
//                return bitmap.byteCount / 1024
//            }
//        }
//
//        return imageCache
//    }

    fun readFile(file:File): String? {
//        val file = File(saveFolder[folderName], faveSongSaveFile)
        var fileText: String? = ""
//        file.listFiles().forEach { file ->
//            val bufferedReader: BufferedReader = file.bufferedReader()
//            inputString = bufferedReader.use {
//                it.readText()
//            }
//        }
        if ( file.exists() ) {
            val bufferedReader: BufferedReader = file.bufferedReader()
            fileText = bufferedReader.use {
                it.readText()
            }
        }
        else {
            file.createNewFile()
        }

        return fileText
    }
}