package com.willdom.codechallenge.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.willdom.codechallenge.R
import com.willdom.codechallenge.adapter.StoresAdapter
import com.willdom.codechallenge.model.entity.Store
import com.willdom.codechallenge.presenter.ListActivity.StoreListContract
import com.willdom.codechallenge.presenter.StoreListPresenter.StoreListPresenter
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MenuItem
import androidx.annotation.Nullable
import com.willdom.codechallenge.IdlingResource.LoadStoreIdlingResource


class MainActivity : AppCompatActivity(), StoreListContract.View {

    private val presenter: StoreListPresenter = StoreListPresenter(this)
    internal var adapter: StoresAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        store_recycler_view.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.divider, null)!!)
        store_recycler_view.addItemDecoration(divider)

        presenter.attempLoadStores()

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            presenter.attempLoadStores()
        }
    }


    override fun displayLoading() {
        loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loading.visibility = View.GONE
        swipeRefreshLayout.isRefreshing = false
    }

    override fun hideNoItemsFound() {
        message_not_found.visibility = View.GONE
    }

    override fun displayMessage(message: String) {
        val snackbarView = Snackbar.make(
            main_container,
            message,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(android.R.string.ok) { }
            /*.view
        (snackbarView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER
        snackbarView.*/
        snackbarView.show()
    }

    override fun displayNoItemsFound() {
        message_not_found.visibility = View.VISIBLE
    }

    override fun displayList(stores: List<Store>) {
        store_recycler_view.visibility = View.VISIBLE

        //adapter =  StoresAdapter(this, stores)
        adapter = StoresAdapter(this, stores) { store: Store, view: View ->
            launchActivity(store,view)
        }
        store_recycler_view.adapter = adapter

    }


    private fun launchActivity(store:Store, view:View){
        val intent = Intent(this, StoreActivity::class.java)
        intent.putExtra(StoreActivity.KEY_STORE, store)
        // Get the transition name from the string
        val transitionName = this.getString(com.willdom.codechallenge.R.string.transition_string)
        val options =

            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                view, // Starting view
                transitionName    // The String
            )
        //Start the Intent
        ActivityCompat.startActivity(this, intent, options.toBundle())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Respond to the action bar's Up/Home button
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
