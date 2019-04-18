package com.example.androidkaraokeapp.presenter

import android.content.Context
import android.util.Log
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.model.SongModel
import com.example.androidkaraokeapp.ulti.FirestoreUlti
import com.example.androidkaraokeapp.ulti.Handle_UI
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class ListRecordPresenter : BasePresenter<ListRecordContract.View>(), ListRecordContract.Presenter {


    val db = FirebaseFirestore.getInstance()
    private val recordCollection = FirestoreUlti.Collection_Record

    override fun fetchRecordFromFirestore(listRecord: MutableList<RecordModel>) {
        db.collection(recordCollection).get().addOnSuccessListener { result ->
                                                    for (document in result) {
                                                        val record = document.toObject(RecordModel::class.java )
                                                        listRecord.add(record)
                                                    }

                                                    listRecord.sortByDescending {  it.create_time }

                                                    getView()?.showListRecord()
                                                }
                                                .addOnFailureListener { exception ->
                                                    Log.w("Firestore ex", "Error getting documents.", exception)
                                                }
    }

    override fun findRecordBySong(listRecord: MutableList<RecordModel>, song: SongModel) {
        listRecord.clear()
        db.collection(recordCollection).whereEqualTo("id", song.id)
                                       .get().addOnSuccessListener { result ->
                                            for (document in result) {
                                                val record = document.toObject(RecordModel::class.java)
                                                listRecord.add(record)

                                            }

                                            listRecord.sortByDescending { it.create_time }

                                            getView()?.showListRecord()
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.w("Firestore ex", "Error getting documents.", exception)
                                        }
    }



    override fun deleteRecordFirestore(record: RecordModel, context: Context) {
        db.collection(recordCollection).document(record.create_time.toString()).delete()
        .addOnSuccessListener {
            val recordFolder = context.filesDir.path + "/record"
            val pathRecord =  recordFolder+ "/" +record.record_url
            val file = File(pathRecord)
            if ( file.exists() ) {
                file.delete()
                Handle_UI().toastWithDuration("Đã xóa",1, context.applicationContext)
                getView()?.updateListRecord(record)
            }
        }
        .addOnFailureListener {
            Handle_UI().toastWithDuration("Xóa không thành công!",1, context.applicationContext)
        }
    }


}
