package io.github.joaogouveia89.helper;

import android.databinding.BindingAdapter;
import android.view.View;

public class DataBinder {
    @BindingAdapter("android:visibility")
    public static void setVisibility(View view, boolean isVisible){
        if(isVisible)
            view.setVisibility(View.VISIBLE);
        else
            view.setVisibility(View.GONE);
    }
}
