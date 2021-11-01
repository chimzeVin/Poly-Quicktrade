package com.example.polyquicktrade;

import android.view.View;

public interface RecyclerClickListener<T> {

    void onClick(T item, View view);

}
