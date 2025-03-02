package com.cheqin.booking.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.cheqin.booking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class PayU_Gateway extends AppCompatActivity implements AsyncTaskListener {

    private AsyncTaskListener listener =null;
    private String fname="";
    private  String about= "";
    private Double amount=1.0;
    private  String email = "";
    private String phone="";
    private  String surl="";
    private  String furl="";
    private  String url="";
    private String curl="";
    private String key="";
    private WebView wv=null;
    private String securityid="",purchaseid="";
    private Bundle bundle;
    private ProgressDialog progressBar=null;
    private HashMap<String, String> parameter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        Bundle bundle=getIntent().getExtras();
        fname = bundle.getString("name");
        about = bundle.getString("about");
        amount=Double.parseDouble(bundle.getString("price"));
        email=bundle.getString("email");
        phone=bundle.getString("email");
        purchaseid=bundle.getString("purchase_id");
        securityid=bundle.getString("reference_no");

        Log.e("bundle",bundle.toString());

//        purchaseid="58142";
//        securityid="91";
//        amount=1.0;
//        about="hjbhj";
//        fname="nn";
//        email="a@a.c";

        key="T5rFrX";
        wv=(WebView)findViewById(R.id.webView1);
        email.hashCode();

        surl=" http://api.mypillows.company/user_requests/payU_success/"+purchaseid;
        furl=" http://api.mypillows.company/user_requests/payU_failure/"+purchaseid;
        url="https://secure.payu.in/_payment";
        curl=" http://api.mypillows.company/user_requests/payU_cancel/"+purchaseid;
        listener = PayU_Gateway.this;
        progressBar = new ProgressDialog(PayU_Gateway.this);
        String msg = "T5rFrX"+ "|" + securityid.toString() + "|"+ String.valueOf(amount) + "|" +
                about + "|"+ fname + "|"+ email + "|||||||||||" + "TVq2chFR";
        Log.e("msg_hash",msg);

        parameter = new HashMap<>();

        parameter.put("hash", msg);
        progressBar.setMessage("Registering...");
        progressBar.setCancelable(true);
        progressBar.show();
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL+"user_requests/get_hash/get.json?", parameter, 2, null);
        httpAsync.execute();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar user_notification_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        progressBar.dismiss();
        String data1 = null;
        if(result.equalsIgnoreCase(Constants.FAIL)){

        }else{
            Log.e("response_pay",result);
            //			Log.e("data", data);
            try {
                JSONObject jobj = new JSONObject(result);
                data1 = jobj.getString("hashed_string");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String hash=data1.replace("\"", "");
            String totalurl="firstname=".toString()+fname+"&productinfo=".toString()+
                    about.toString()+"&amount="+String.valueOf(amount)+"&email="+email+
                    "&phone="+phone+"&surl="+surl+"&furl="+furl+"&hash="+hash+
                    "&key="+key+"&txnid="+securityid.toString();
            System.out.println(totalurl);
            Log.e("total url",totalurl);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());

            progressBar.dismiss();


            wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            final AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();

//            progressBar = ProgressDialog.show(getApplicationContext(), null, Constants.REDIRECTING);

            wv.setWebViewClient(new WebViewClient() {
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





                @SuppressWarnings("deprecation")
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    //   Log.e(TAG, "Error: " + description);
                    Toast.makeText(getApplicationContext(), "Please check your internet connection.!" + description, Toast.LENGTH_SHORT).show();
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
            wv.postUrl(url, totalurl.getBytes());
        }

    }
}
