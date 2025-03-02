package com.cheqin.booking.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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

public class FaqFragment extends Fragment {

    private Context context;
//    private SharedPreferences contact = null;
    private WebView webView = null;
    private ProgressDialog progressBar = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_tc_main, container, false);
        context = getActivity();
        webView = (WebView) view.findViewById(R.id.webView1);
        progressBar = new ProgressDialog(this.getActivity());
        progressBar.setCancelable(false);
        progressBar.setMessage("Please wait...");
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Log.i(TAG, "Processing webview url click...");

                view.loadUrl(url);

                return true;
            }



            public void onPageFinished(WebView view, String url) {
                //   Log.i(TAG, "Finished loading URL: " +url);

                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                Toast.makeText(getContext(), "Please check your internet connection.!" + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", (dialog, which) -> {
                    return; // $codepro.audit.disable unnecessaryReturn
                });
                alertDialog.show();
            }
        });
        webView.loadUrl("file:///android_asset/faq.html");

        return view;
    }

}
