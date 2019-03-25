package com.example.androidkaraokeapp.presenter

import java.lang.ref.WeakReference


abstract class BasePresenter<V> {
    private var view: WeakReference<V>? = null

    fun setView(v: V) {
        view = WeakReference(v)
    }

    protected fun getView(): V? = view?.get()
}