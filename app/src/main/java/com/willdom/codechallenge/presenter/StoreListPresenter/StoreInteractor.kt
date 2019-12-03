package com.willdom.codechallenge.presenter.StoreListPresenter

import android.util.Log
import com.miasesor.myasesortributario.model.data.AppDataBase
import com.miasesor.myasesortributario.model.data.DStore
import com.willdom.codechallenge.MyApplication
import com.willdom.codechallenge.isConnected
import com.willdom.codechallenge.model.entity.Store
import com.willdom.codechallenge.presenter.ListActivity.StoreListContract
import com.willdom.codechallenge.tag
import com.willdom.codechallenge.webservice.WebService


class StoreInteractor(private var listener: StoreListContract.dataConnectionListener) : StoreListContract.Interactor {


    private val dStore =  AppDataBase.getAppDataBase(MyApplication.getAppContext()).dStore()

    override fun loadStores():List<Store>{
        var list: List<Store>
        if(isConnected(MyApplication.getAppContext())){
            try {
                val wrapperObject = WebService(MyApplication.getAppContext()).stores()
                list = wrapperObject.stores
                dStore.clean()
                dStore.insert(list)
                listener.onWebService()
            } catch (e:Exception){
                Log.e(tag, e.message, e)
                list = dStore.list()
                if(list.isEmpty()) {
                    listener.onErrorGettingData()
                } else {
                    listener.onDataBase()
                }
            }
        } else{
            list = dStore.list()
            listener.onDataBase()
        }

       return  list
    }

    override fun numberItemsOnDataBase(): Int {
        return dStore.count()
    }

}