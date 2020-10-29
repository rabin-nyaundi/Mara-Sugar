package com.rabitech.util

import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

@BindingAdapter("day")
fun bindDay(textView: AppCompatTextView, date :String){
    val mothDay = date.split(",")[0]
    textView.text = mothDay.split(" ")[1]

}

@BindingAdapter("month")
fun bindMonth(textView: AppCompatTextView, date :String){
    val mothDay = date.split(",")[0]
    textView.text = mothDay.split(" ")[0]

}