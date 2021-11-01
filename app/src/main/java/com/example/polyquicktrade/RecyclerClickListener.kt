package com.example.polyquicktrade

import android.view.View

interface RecyclerClickListener<T> {
    fun onClick(item: T, view: View?)
}