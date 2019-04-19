package com.example.androidkaraokeapp.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androidkaraokeapp.R
import com.example.androidkaraokeapp.model.RecordModel
import com.example.androidkaraokeapp.presenter.ListRecordContract
import com.example.androidkaraokeapp.presenter.ListRecordPresenter
import com.example.androidkaraokeapp.view.recyclerView.ListRecordRecyclerView.ListRecordRecyclerViewAdapter

class UserFragment: Fragment(),ListRecordContract.View  {



    private lateinit var listRecordRecyclerView: RecyclerView
    private var listRecordPresenter = ListRecordPresenter()
    private var listRecord: MutableList<RecordModel> = mutableListOf()
    private lateinit var listRecordAdapter: ListRecordRecyclerViewAdapter

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: ListSongFragment? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: ListSongFragment().also { instance = it }
        }
    }
//    private lateinit var viewModel: ListSongViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.user_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ListSongViewModel::class.java)
        view?.let{
            setupPresenter()
            configureUI(it)
        }
    }

    override fun showListRecord() {
        this.listRecordRecyclerView.adapter = ListRecordRecyclerViewAdapter(listRecord,listRecordPresenter)
    }

    override fun updateListRecord(removeRecord: RecordModel) {
        listRecord.remove(removeRecord)
        this.listRecordRecyclerView.adapter?.notifyDataSetChanged()
    }
    //region private method
    private fun configureUI (it:View) {

        listRecordRecyclerView = it.findViewById(R.id.list_record_recycler_view)
        listRecordRecyclerView.layoutManager = LinearLayoutManager(activity)
        listRecordRecyclerView.adapter =  ListRecordRecyclerViewAdapter(listRecord, listRecordPresenter)
        listRecordAdapter = listRecordRecyclerView.adapter as ListRecordRecyclerViewAdapter

        //        Add divider to viewholder
        val dividerItemDecoration = DividerItemDecoration(context!!, LinearLayoutManager.VERTICAL)
        dividerItemDecoration.setDrawable(context!!.resources.getDrawable(R.drawable.divider_recycler_view))
        listRecordRecyclerView.addItemDecoration(dividerItemDecoration)

        listRecordPresenter.fetchRecordFromFirestore(listRecord)

    }

    private fun setupPresenter() {
        listRecordPresenter.setView(this)
    }
    //endregion

}