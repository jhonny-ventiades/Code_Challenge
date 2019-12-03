package com.willdom.codechallenge.IdlingResource

import androidx.annotation.Nullable
import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class LoadStoreIdlingResource : IdlingResource {

    var isIdlState: Boolean = false
        set(idlState) {
            field = idlState
            if (idlState && mCallback != null) {
                mCallback!!.onTransitionToIdle()
            }
        }
    @Nullable
    @Volatile
    private var mCallback: IdlingResource.ResourceCallback? = null
    private val mIsIdleNow = AtomicBoolean(true)


    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        mCallback = callback
    }
    override fun getName(): String {
        return this.javaClass.name
    }

    override fun isIdleNow(): Boolean {
        return mIsIdleNow.get()
    }

}