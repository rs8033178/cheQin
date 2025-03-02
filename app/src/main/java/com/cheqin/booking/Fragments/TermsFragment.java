package com.cheqin.booking.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cheqin.booking.R;

import androidx.fragment.app.Fragment;

public class TermsFragment extends Fragment {

    //    private TextView terms = null;
//    private TextView call = null;
//    private TextView email = null;
    private SharedPreferences contact = null;
    private WebView webView = null;
    private ProgressDialog progressBar=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.terms, container, false);
//        terms = (TextView) view.findViewById(R.id.textView4);
//        call = (TextView) view.findViewById(R.id.textView5);
//        email = (TextView) view.findViewById(R.id.textView6);
        webView = (WebView) view.findViewById(R.id.webView);
        progressBar = new ProgressDialog(this.getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait...");
        WebSettings settings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");

                view.loadUrl(url);

                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.show();
            }

            public void onPageFinished(WebView view, String url) {
                //   Log.i(TAG, "Finished loading URL: " +url);

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //   Log.e(TAG, "Error: " + description);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                Toast.makeText(getContext(), "Please check your internet connection.!" + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return; // $codepro.audit.disable unnecessaryReturn
                    }
                });
                alertDialog.show();
            }
        });
        webView.loadUrl("file:///android_asset/terms.html");
        return view;
    }

//        contact=this.getActivity().getSharedPreferences("contact",Context.MODE_PRIVATE);
//        terms.setText(contact.getString("terms",null));
//        call.setText(contact.getString("call_now_phone",null));
//        email.setText(contact.getString("support_email",null));


}


