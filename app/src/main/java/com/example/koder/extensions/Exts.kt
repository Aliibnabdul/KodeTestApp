package com.example.koder.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.koder.R
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private val localDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
val glideCircleTransformOption = RequestOptions().apply(RequestOptions.circleCropTransform())
private const val CROSSFADE_DURATION = 500
private const val SNACKBAR_DURATION = 4000

fun String.toLocalDate(): LocalDate = localDateFormatter.parse(this, LocalDate::from)

fun String.toUiFormat(): String {
    val date = this.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("d MMM", Locale("ru"))
    return date.format(formatter).dropLast(1)
}

fun LocalDate.toDetailsUiFormat(): String {
    val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))
    return this.format(formatter)
}

fun Int.toAgeString(): String {
    return when (this % 10) {
        0, 5, 6, 7, 8, 9 -> "$this лет"
        1 -> "$this год"
        2, 3, 4 -> "$this года"
        else -> ""
    }
}

fun String.toNextLocalDate(): LocalDate {
    val now = LocalDate.now()
    val birthday = this.toLocalDate()
    val year = if (now.dayOfYear <= birthday.dayOfYear) now.year
    else now.year + 1
    return LocalDate.of(year, birthday.monthValue, birthday.dayOfMonth)
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun ImageView.setCircleImage(uri: String, @DrawableRes placeholderRes: Int) {
    Glide.with(this)
        .load(uri)
        .apply(glideCircleTransformOption)
        .placeholder(placeholderRes)
        .transition(DrawableTransitionOptions.withCrossFade(CROSSFADE_DURATION))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

fun Context.callPhone(number: String) {
    val callUri = Uri.parse("tel:$number")
    try {
        startActivity(Intent(Intent.ACTION_DIAL, callUri))
    } catch (e: Exception) {
        Log.e("_TAG", "No Activity found to handle Intent")
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.getSnackBar(
    view: View,
    messageString: String,
    anchorView: View? = null,
): Snackbar? {
    view.hideKeyboard()
    return if (view.isAttachedToWindow) {
        val snackBar = Snackbar.make(view, messageString, Snackbar.LENGTH_INDEFINITE)
            .setAnchorView(anchorView)
            .addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                    sb?.anchorView = null
                }
            })
        snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.bg_loading_snackbar)
        val textView = snackBar.view.rootView.findViewById<TextView>(R.id.snackbar_text)
        textView.textSize = 14F
        snackBar.show()
        return snackBar
    } else null
}

fun Context.showSnackBar(
    view: View,
    messageString: String,
    anchorView: View? = null,
) {
    view.hideKeyboard()
    if (view.isAttachedToWindow) {
        val snackBar = Snackbar.make(view, messageString, Snackbar.LENGTH_LONG)
            .setAnchorView(anchorView)
            .addCallback(object : Snackbar.Callback() {
                override fun onShown(sb: Snackbar?) {
                    super.onShown(sb)
                    sb?.anchorView = null
                }
            })
        snackBar.view.background = ContextCompat.getDrawable(this, R.drawable.bg_error_snackbar)
        val textView = snackBar.view.rootView.findViewById<TextView>(R.id.snackbar_text)
        textView.textSize = 14F
        snackBar.show()
    }
}