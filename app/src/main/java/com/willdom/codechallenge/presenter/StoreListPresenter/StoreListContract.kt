@file:JvmName("StoreListPresenterKt")

package com.willdom.codechallenge.presenter.ListActivity

import com.willdom.codechallenge.model.entity.Store


class StoreListContract{

    interface View {
        fun displayLoading()
        fun hideLoading()
        fun hideNoItemsFound()
        fun displayMessage(message:String)
        fun displayNoItemsFound()
        fun displayList(stores: List<Store>)

    }

    interface Presenter {
        fun attempLoadStores()
    }

    //repository
    interface Interactor {
        fun loadStores():List<Store>
        fun numberItemsOnDataBase():Int

    }

    interface dataConnectionListener {
        fun onWebService()
        fun onDataBase()
        fun onErrorGettingData()
    }
}