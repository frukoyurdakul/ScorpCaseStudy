package com.scorp.casestudy.furkanyurdakul.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.scorp.casestudy.furkanyurdakul.data.model.Person

@BindingAdapter("personInfo")
fun TextView.setPersonInfo(person: Person)
{
    val personText = "${person.fullName} (${person.id})"
    text = personText
}