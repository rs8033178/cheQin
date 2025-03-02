package com.cheqin.booking.User;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.cheqin.booking.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by user on 06-11-2015.
 */
public class UserContactUs extends AppCompatActivity {

    private LinearLayout callus = null;
    private LinearLayout emailus = null;
    private String email = "ask@utel.bargains";
    private String body = "Hi,\nPlease provide the registered details at cheQin App as below.\n\nEmail:\nMobile:\nDetails of the issue:\n\n" +
            "Thanks\n{Your name}";
    private String subject = "Appropriate reason here!";
    private String chooserTitle = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        callus = (LinearLayout) findViewById(R.id.call_us);
        emailus = (LinearLayout) findViewById(R.id.email_us);

        callus.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+918908789789"));
            startActivity(intent);
        });

        emailus.setOnClickListener(v -> {
            Uri uri = Uri.parse("mailto:")
                    .buildUpon()
                    .appendQueryParameter("subject", subject)
                    .appendQueryParameter("body", body)
                    .build();
            Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
            intent1.putExtra(Intent.EXTRA_EMAIL,new String[] { email });
            startActivity(Intent.createChooser(intent1, chooserTitle));
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


}

