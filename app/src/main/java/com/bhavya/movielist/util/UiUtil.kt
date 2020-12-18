package com.bhavya.movielist.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar

/**
 *  show toast
 */
fun Activity.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    makeText(applicationContext, message, duration).show()
}

/**
 * Provides [ViewModel] of type [VM] from [factory].
 */
inline fun <reified VM : ViewModel> AppCompatActivity.viewModelOf(factory: ViewModelProvider.Factory) =
    ViewModelProvider(this, factory).get(VM::class.java)
fun Context.dismissKeyboard(view: View?) {
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}
/**
 *  show snackbar
 */
fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackbar ->
        snackbar.setAction("Ok") {
            snackbar.dismiss()
        }
    }.show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

