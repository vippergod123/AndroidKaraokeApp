package com.example.androidkaraokeapp.ulti

import android.os.AsyncTask
import java.lang.Exception
import android.annotation.SuppressLint
import android.content.Context
import com.example.androidkaraokeapp.model.SongModel
import java.io.*
import java.net.URL


@SuppressLint("StaticFieldLeak")
object HandleDiskLRUCache {

    private const val DISK_CACHE_SIZE = 1024 * 1024 * 30.toLong() // 10MB

    private var context: Context ?= null


    class DownloadSongAudioAsynctask(var song: SongModel, var callBack: () -> Unit) : AsyncTask<File, Void, Void>() {
        override fun doInBackground(vararg params: File?): Void? {
            try {
                val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")
                if ( !audioCacheFolder.exists())
                    audioCacheFolder.mkdir()

                var nameAudio = songPathInCacheFolder(song.id)
                if ( nameAudio != null) {
                    val exist = File(nameAudio)
                    val updateFileName = "${song.id}-${System.currentTimeMillis()}.mp3"
                    val updateFile = File(audioCacheFolder.path + "/" + updateFileName)
                    exist.renameTo(updateFile)
                    return null
                }

                val u = URL(song.mp3_url)
                val conn = u.openConnection()
                val contentLength = conn.contentLength

                val stream = DataInputStream(u.openStream())

                val buffer = ByteArray(contentLength)
                stream.readFully(buffer)
                stream.close()

                nameAudio = "${song.id}-${System.currentTimeMillis()}.mp3"
                val outputFile = File(audioCacheFolder.path + "/" + nameAudio)
                val fos = DataOutputStream(FileOutputStream(outputFile))
                fos.write(buffer)
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                return  null
            } catch (e: IOException) {
                return  null
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            optimizedCacheFolder()
            callBack()
        }
    }

    fun initDiskLRUCache(mContext:Context) {
        context = mContext
        val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")
        if ( !audioCacheFolder.exists())
            audioCacheFolder.mkdir()
    }

    fun getSizeAudioCacheFolder():Long{
        var size:Long = -1
        val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")
        if (audioCacheFolder.exists()) {
            audioCacheFolder.listFiles().forEach{
                size += it.length()
            }
            return size
        }
        return size
    }

    fun deleteAudioFileInCacheFolderByID(songID: Int) {
        val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")

        if ( audioCacheFolder.exists()) {
            val file = audioCacheFolder.listFiles().find {
                it.name.contains(songID.toString())
            }
            try {
                file?.delete()
            }
            catch(ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun optimizedCacheFolder(): Void?{
        val listFile = mutableMapOf<Int, Long>()
        val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")
        if ( getSizeAudioCacheFolder() < DISK_CACHE_SIZE)
            return null

        if ( audioCacheFolder.exists()) {

            audioCacheFolder.listFiles().forEach{
                val split = it.name.split("-")
                val idSong = split[0].toInt()
                val cacheTime = split[1].substringBefore(".mp3").toLong()
                listFile[idSong] = cacheTime
            }


            while ( getSizeAudioCacheFolder() > DISK_CACHE_SIZE ) {
                val audio = listFile.minBy {
                    it.value
                }
                try {
//                    val filePath = "${audio?.key}-${audio?.value}.mp3"
                    deleteAudioFileInCacheFolderByID(audio?.key!!)
                    listFile.remove(audio.key)
                }
                catch( ex:Exception) {
                    ex.printStackTrace()
                }
            }
        }
        else {
            audioCacheFolder.mkdir()
        }
        return null
    }

    fun songPathInCacheFolder(songID:Int):String? {
        val audioCacheFolder = File(context!!.cacheDir.path + "/audio-cache")
        if (audioCacheFolder.exists()) {
            val song = audioCacheFolder.listFiles().find {
                val id = it.name.split('-')[0].toInt()
                id == songID
            }
            return song?.path
        }
        return null
    }

}