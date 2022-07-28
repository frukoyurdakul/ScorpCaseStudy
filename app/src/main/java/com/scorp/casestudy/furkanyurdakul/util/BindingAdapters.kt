package com.scorp.casestudy.furkanyurdakul.util

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.scorp.casestudy.furkanyurdakul.data.model.Person

@BindingAdapter("personInfo")
fun TextView.setPersonInfo(person: Person)
{
    val personText = "${person.fullName} (${person.id})"
    text = personText
}

@BindingAdapter("android:text")
fun TextView.setTextWithAdapter(resultText: String?)
{
    text = resultText
}

@BindingAdapter("isVisible")
fun View.setVisible(visible: Boolean)
{
    isVisible = visible
}