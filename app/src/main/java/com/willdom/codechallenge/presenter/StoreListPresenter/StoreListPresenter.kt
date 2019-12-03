package com.willdom.codechallenge.presenter.StoreListPresenter

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import com.willdom.codechallenge.IdlingResource.LoadStoreIdlingResource
import com.willdom.codechallenge.MyApplication
import com.willdom.codechallenge.R
import com.willdom.codechallenge.presenter.ListActivity.StoreListContract
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class StoreListPresenter(private var view: StoreListContract.View?): StoreListContract.Presenter, StoreListContract.dataConnectionListener{



    @Nullable
    private var loadStoreIdlingResource: LoadStoreIdlingResource? = null
    private var interactor: StoreListContract.Interactor = StoreInteractor(this)

    override fun attempLoadStores() {
        if (loadStoreIdlingResource != null) {
            loadStoreIdlingResource!!.isIdlState = false
        }
        view?.displayLoading()
        view?.hideNoItemsFound()
        doAsync {
            val stores = interactor.loadStores()
            uiThread {
                view?.hideLoading()
                view?.displayList(stores)
                if(stores.isEmpty()){
                    view?.displayNoItemsFound()
                } else {
                    view?.hideNoItemsFound()
                }
                if (loadStoreIdlingResource != null) {
                    loadStoreIdlingResource!!.isIdlState = true
                }
            }
        }
    }


    override fun onWebService() {
        view?.displayMessage(MyApplication.getAppContext().getString(R.string.message_data_updated))
    }

    override fun onDataBase() {
        view?.displayMessage(MyApplication.getAppContext().getString(R.string.message_offline_data))
    }

    override fun onErrorGettingData() {
        view?.displayMessage(MyApplication.getAppContext().getString(R.string.message_error_getting_data))
    }

    @VisibleForTesting
    @NonNull
    fun getIdlingResource(): IdlingResource {
        if (loadStoreIdlingResource == null) {
            loadStoreIdlingResource = LoadStoreIdlingResource()
        }
        return loadStoreIdlingResource as LoadStoreIdlingResource
    }
}