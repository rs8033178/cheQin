package com.cheqin.booking.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheqin.booking.R;

import com.koushikdutta.ion.Ion;


public class Progressloadingdialog extends Dialog {

    private TextView header=null;
    private String status= null;
    private ImageView loadgif=null;
    public Progressloadingdialog(Context context,String string) {
        super(context);

        this.status=string;
        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progressloadingdialog);

        loadgif=(ImageView)findViewById(R.id.loadGif);
        Ion.with(loadgif).load("file:///android_asset/loading.gif");

        header=(TextView)findViewById(R.id.txtHeader);
        header.setText(status);
    }

}
