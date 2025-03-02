package com.cheqin.booking.User;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cheqin.booking.Adapter.UserNotificationAdapter;
import com.cheqin.booking.Bean.UserNotificationBean;
import com.cheqin.booking.R;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by Suhas on 06-Jan-16.
 */
public class UserNotifications extends AppCompatActivity implements AsyncTaskListener {

    ListView lv = null;
    private List<UserNotificationBean> rowItems;
    private HashMap<String, String> parameters = null;
    private AsyncTaskListener listener = null;
    private String auth_token = null;
    private Context context = null;
    private Progressloadingdialog pd = null;
    private HashMap<String, String> paraHashMap = null;
    private int positionx = -1;
    private UserNotificationAdapter adpt;

    SettingSharedPrefs ssp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_notification);

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Messages");
        lv = (ListView) findViewById(R.id.listView6);
        context = getApplicationContext();

        auth_token = ssp.getPRE_auth_token();

        rowItems = new ArrayList<>();
        listener = UserNotifications.this;
        pd = new Progressloadingdialog(UserNotifications.this, "Loading Messages...");
        pd.setCancelable(false);
        pd.show();
        parameters = new HashMap<>();
        parameters.put("auth_token", auth_token);
        new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "my_deals/user_notifications/get.json?", parameters, 1, "messages").execute();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                positionx = position;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserNotifications.this);
                alertDialogBuilder.setTitle("Delete")
                        .setMessage("Are you sure you want to Delete the Message?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                pd = new Progressloadingdialog(UserNotifications.this, "Deleting...");
                                pd.setCancelable(false);
                                pd.show();

                                paraHashMap = new HashMap<>();
                                paraHashMap.put("msg_id", rowItems.get(position).getMsgid());
                                paraHashMap.put("auth_token", auth_token);
                                HttpAsync pass = new HttpAsync(getApplicationContext(), listener, Constants.BASE_URL + "/my_deals/user_message_read/read.json?", paraHashMap, 2, "read");
                                pass.execute();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialogBuilder.show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {
        Log.e("result is", result);

        if (result.equalsIgnoreCase("fail")) {
            Toast.makeText(getApplicationContext(), "Please Check Your Internet Connection!!!", Toast.LENGTH_LONG).show();
        } else if (tag.equalsIgnoreCase("messages")) {
            try {
                JSONObject jobj = new JSONObject(result);

                if (jobj != null) {

                    JSONArray jarry = jobj.getJSONArray("messages");
                    if (jarry.length() > 0) {
                        Log.e("length", jarry.length() + " ");

                        for (int i = 0; i < jarry.length(); i++) {
                            JSONObject jo = jarry.getJSONObject(i);

                            String msg = Html.fromHtml(jo.getString("msg_body")).toString();
                            UserNotificationBean b = new UserNotificationBean();
                            b.setMsgid(jo.getString("id"));
//                        b.setmsge(jo.getString("msg_body"));
                            b.setmsge(msg);
                            Log.e("dfsdsf", msg);
                            rowItems.add(b);
                        }
                        adpt = new UserNotificationAdapter(context, rowItems);
                        lv.setAdapter(adpt);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }
        } else if (tag.equalsIgnoreCase("read")) {
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        if (positionx != -1) {
                            rowItems.remove(positionx);
                            if (adpt != null) {
                                adpt.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }
        }
        pd.dismiss();

    }
}
