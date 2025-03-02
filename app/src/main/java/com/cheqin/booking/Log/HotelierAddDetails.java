package com.cheqin.booking.Log;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.cheqin.booking.Adapter.AmenitiesDataAdapter;
import com.cheqin.booking.Adapter.HotelTypeDataAdapter;
import com.cheqin.booking.Bean.Amenitiesbean;
import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.R;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.gcm.SharedPreference1;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

public class HotelierAddDetails extends AppCompatActivity implements AsyncTaskListener, AmenitiesDataAdapter.OnCheckBoxClicked, OnMapReadyCallback {

    private EditText edt_hotelname = null;
    private EditText edt_addr = null;
    private EditText edt_bank_name = null;
    private EditText edt_branch_loc = null;
    private EditText edt_acc_no = null;
    private EditText ifsc = null;
    private EditText edt_website = null;
    private Button btn_save = null;
    private TextView txt_addimage = null;
    private static String imageEncoded = null;
    private Progressloadingdialog pDialog;
    HashMap<String, String> params = null;
    ImageView imageView = null;
    public static int RESULT_LOAD_IMAGE = 1;
    AsyncTaskListener listener = null;
    private String url = " http://api.mypillows.company/users/save_hotel_details/save.json?";
    private String auth_token = null;
    private String pictureformat = null;
    private SharedPreferences shared_hotel = null;
    private String email = null;
    private LinearLayout hoteltype = null;
    private LinearLayout amenitytype = null;
    private ListView mRecyclerView = null;
    private List<Amenitiesbean> amenitiesbeanList;
    private String[] category = null;

    public static MapFragment googleMap;
    CardView cardmap;
    SharedPreferences sharedPreferences;
    public static TextView hotelnotfound;
    public static Double longi = 0.0;
    public static Double lati = 0.0;
    private ListView hotel_listView;
    private HashMap<String, String> htype = null;
    private HashMap<String, String> para2 = null;
    private List<Hoteltypebean> hotelcategories;
    String hotel_id = null;
    String hotel_info = null;
    TextView txt_hoteltype = null;
    TextView txt_amenitytype = null;

    SettingSharedPrefs ssp;
    ArrayList<String> subcheckedIdList;
    ArrayList<String> checkedIdList;
    Dialog dialog = null;
    private SharedPreferences check = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotelier_add_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_hoteldetails);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Hotel Details");

        SharedPreference sharedPreference = new SharedPreference();
        amenitiesbeanList = sharedPreference.getFavorites(HotelierAddDetails.this);
        boolean[] checkBoxState = new boolean[amenitiesbeanList.size()];
        for (int i = 0; i < amenitiesbeanList.size(); i++) {
            if (amenitiesbeanList.get(i).getName().equals("Free WiFi") || amenitiesbeanList.get(i).getName().equals("Comp Breakfast")) {
                checkBoxState[i] = true;
            }
        }

       /* for (int i = 0; i<amenitiesbeanList.size();i++){
            Log.e("amenitiesbeanList" +i, amenitiesbeanList.get(i).getId());
        }
*/
        ssp = SettingSharedPrefs.getInstance(getApplicationContext());
        listener = HotelierAddDetails.this;

        edt_hotelname = (EditText) findViewById(R.id.edt_hotel_name);
        edt_addr = (EditText) findViewById(R.id.edt_hotel_addr);
        edt_bank_name = (EditText) findViewById(R.id.edt_bank_name);
        edt_branch_loc = (EditText) findViewById(R.id.edt_branch_loc);
        edt_acc_no = (EditText) findViewById(R.id.edt_account_no);
        ifsc = (EditText) findViewById(R.id.edt_ifsc);
        edt_website = (EditText) findViewById(R.id.edt_website);
        btn_save = (Button) findViewById(R.id.save_button);
        txt_addimage = (TextView) findViewById(R.id.txt_image);
        hoteltype = (LinearLayout) findViewById(R.id.hoteltype);
        txt_hoteltype = (TextView) findViewById(R.id.hoteltype_text1);
        amenitytype = (LinearLayout) findViewById(R.id.amenittype);
        txt_amenitytype = (TextView) findViewById(R.id.amenitytype_text);
//        auth_token = mpref.getString("auth", null);

        subcheckedIdList = new ArrayList<String>();
        checkedIdList = new ArrayList<String>();

        Intent intent = getIntent();


        email = intent.getStringExtra("emailid");

        if (intent.getStringExtra("auth_token") != null) {
            auth_token = intent.getStringExtra("auth_token");
        } else {
            auth_token = ssp.getPRE_auth_token();
        }


        cardmap = (CardView) findViewById(R.id.card_view);
        hotelnotfound = (TextView) findViewById(R.id.hotelnotfound);
        googleMap = (MapFragment) getFragmentManager().findFragmentById(R.id.hotelmap);
        googleMap.getMapAsync(this);

        final CameraPosition cameragoogle = new CameraPosition.Builder()
                .target(new LatLng(21.0000, 78.0000))      // Sets the center of the map to Mountain View
                .zoom(3)                   // Sets the zoom
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();
        // googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameragoogle));

        imageView = (ImageView) findViewById(R.id.hotel_image);


//        txt_addimage.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View arg0) {
//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                imageView.setVisibility(View.VISIBLE);
//
//                startActivityForResult(i, RESULT_LOAD_IMAGE);

//            }
//        });

//        sharedPreferences = getSharedPreferences("hotel_lat", MODE_PRIVATE);
//
//        lati = Double.longBitsToDouble(sharedPreferences.getLong("latitude", 0));

//        longi = Double.longBitsToDouble(sharedPreferences.getLong("longitude", 0));

        shared_hotel = getSharedPreferences("hotel_name", MODE_PRIVATE);
        SharedPreferences.Editor shd_hotelname = shared_hotel.edit();
        shd_hotelname.putString("hotelname", edt_hotelname.getText().toString());

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (lati != 0.0) {

                    if (hotel_info != null) {

                        if (edt_hotelname.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the hotel name", Toast.LENGTH_SHORT).show();
                        } else if (edt_addr.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the hotel address", Toast.LENGTH_SHORT).show();
                        }
//                        else if (edt_bank_name.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(getApplicationContext(), "Please enter the bank name", Toast.LENGTH_SHORT).show();
//                        } else if (edt_branch_loc.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(getApplicationContext(), "Please enter the branch name", Toast.LENGTH_SHORT).show();
//                        } else if (edt_acc_no.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(getApplicationContext(), "Please enter the account number", Toast.LENGTH_SHORT).show();
//                        }
                        else if (edt_website.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(getApplicationContext(), "Please enter the hotel's website", Toast.LENGTH_SHORT).show();
                        }
//                        else if (ifsc.getText().toString().equalsIgnoreCase("")) {
//                            Toast.makeText(getApplicationContext(), "Please enter ifsc code", Toast.LENGTH_SHORT).show();
//                        }
                        else {

                            checkConnection();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select the Hotel type", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select the Hotel Location", Toast.LENGTH_LONG).show();
                }
            }
        });


        cardmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(HotelierAddDetails.this, HotelierSelectHotel.class));
            }
        });

        hoteltype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hotelcategories = new ArrayList<>();
                SharedPreference1 sharedPreference1 = new SharedPreference1();
                hotelcategories = sharedPreference1.getFavorites(HotelierAddDetails.this);
                final Dialog dialog1 = new Dialog(HotelierAddDetails.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.prompt_hoteltype);
                dialog1.setCancelable(true);
                dialog1.show();
                hotel_listView = (ListView) dialog1.findViewById(R.id.hotel_list);
                hotel_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                hotel_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        check = getSharedPreferences("check", MODE_PRIVATE);
                        SharedPreferences.Editor edt = check.edit();
                        edt.putString("id", hotelcategories.get(position).getId().toString());
                        edt.putString("name", hotelcategories.get(position).getName().toString());
                        edt.commit();
                        hotel_id = hotelcategories.get(position).getId().toString();
                        hotel_info = hotelcategories.get(position).getName().toString();
                        txt_hoteltype.setVisibility(View.VISIBLE);
                        txt_hoteltype.setText(hotel_info);
                        dialog1.dismiss();
                    }
                });
                HotelTypeDataAdapter hotelTypeDataAdapter = new HotelTypeDataAdapter(HotelierAddDetails.this, hotelcategories);
                hotel_listView.setAdapter(hotelTypeDataAdapter);


            }
        });

        amenitytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Button amenities_ok;
                dialog = new Dialog(HotelierAddDetails.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.prompt_amenities);
                dialog.setCancelable(true);
                dialog.show();
                amenities_ok = (Button) dialog.findViewById(R.id.amenities_ok);
                mRecyclerView = (ListView) dialog.findViewById(R.id.recyclerView);

                amenities_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        amenities_text.setVisibility(View.VISIBLE);
                        checkedIdList = null;
                        checkedIdList = new ArrayList<String>(subcheckedIdList);
                        // AmenitiesDataAdapter.checkedIdList2 = null;
                        String coun = checkedIdList.size() + "";
                        //Log.e("count", coun);
//                        amenities_text.setText(coun + " Selected");
                        dialog.dismiss();
                    }
                });

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        subcheckedIdList = null;
                        subcheckedIdList = new ArrayList<String>(checkedIdList);

                        /*for (int i = 0; i<checkedIdList.size();i++){
                            Log.e("checkedIdList" +i, checkedIdList.get(i));
                        }
*/
                        // AmenitiesDataAdapter.checkedIdList2 = null;
                        dialog.dismiss();
                    }
                });

                AmenitiesDataAdapter mAdapter = new AmenitiesDataAdapter(amenitiesbeanList, HotelierAddDetails.this, checkedIdList);
//                checkedIdList = null;
//                checkedIdList = new ArrayList<String>();
                mRecyclerView.setAdapter(mAdapter);
//                mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    }
//                });
            }
        });


    }

    private void checkConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {
            doAddDetails();
        } else {
            //Toast.makeText(getApplicationContext(),"Internet is not Avilable",Toast.LENGTH_SHORT).show();
            ShowSnackBar();
        }
    }

    private void doAddDetails() {

        pDialog = new Progressloadingdialog(HotelierAddDetails.this, "Saving Hotel details..");
        pDialog.setCancelable(false);
        pDialog.show();
        params = new HashMap<>();
        params.put("buysell_category_id", hotel_id);
        params.put("merchant[business_name]", edt_hotelname.getText().toString());
        params.put("merchant[contact_detail]", edt_addr.getText().toString());
        params.put("merchant[website_url]", edt_website.getText().toString());
        params.put("merchant[bank_name]", edt_bank_name.getText().toString());
        params.put("merchant[branch]", edt_branch_loc.getText().toString());
        params.put("merchant[checking]", edt_acc_no.getText().toString());
        params.put("merchant[swift_code]", "");
        params.put("merchant[routing]", ifsc.getText().toString());
        params.put("merchant_premise[latitude]", lati.toString());
        params.put("merchant_premise[longitude]", longi.toString());
        params.put("merchant_premise[premise_name]", edt_addr.getText().toString());
        params.put("auth_token", auth_token);
        params.put("hotel_filename", "");
        params.put("hotel_img_contentType", "");
        params.put("hotel_img_data", "");
//                Log.e("image string", imageEncoded);
        //Log.e("paramn",params.toString());
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), listener, url, params, 2, "adddetails");
        httpAsync.execute();

    }

    private void ShowSnackBar() {
        String styledText = "<font color='black'>No Internet Connection</font>.";
        // msnackBar;
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkConnection();
                    }
                })
                .setActionTextColor(Color.RED);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ffffff")); // snackbar background color
        // snackbar.setActionTextColor(Color.parseColor("#000000")); // snackbar action text color
        snackbar.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            pictureformat = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);

            if (bm != null) {
                encodeTobase64(bm);
                imageView.setImageBitmap(bm);
            }

        } else {
            Toast.makeText(HotelierAddDetails.this, "pick image not performed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (checkedIdList != null && checkedIdList.size() > 0) {
            for (int i = 0; i < checkedIdList.size(); i++) {
//                    showToast(checkedIdList.get(i));
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        //Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {


        if (tag.equalsIgnoreCase("hotel_categories")) {
//            Log.e("result", result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONArray jsonArray1 = jsonObject.getJSONArray("hotel_types");
//                if (jsonArray1.length() > 0) {
//                    for (int j = 0; j < jsonArray1.length(); j++) {
//                        JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
//                        Hoteltypebean hoteltypebean = new Hoteltypebean();
//                        hoteltypebean.setId(jsonObject1.getString("id"));
//                        hoteltypebean.setName(jsonObject1.getString("name"));
//                        hotelcategories.add(hoteltypebean);
//                    }
//
//                }
//                HotelTypeDataAdapter hotelTypeDataAdapter = new HotelTypeDataAdapter(HotelierAddDetails.this, hotelcategories);
//                hotel_listView.setAdapter(hotelTypeDataAdapter);
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        } else if (tag.equalsIgnoreCase("amenities")) {
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                JSONArray jsonArray = jsonObject.getJSONArray("amenities");
//                if (jsonArray.length() > 0) {
//                    category = new String[jsonArray.length()];
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jo = jsonArray.getJSONObject(i);
//                        Amenitiesbean amenity = new Amenitiesbean();
//                        amenity.setName(jo.getString("name"));
//                        amenity.setId(jo.getString("id"));
//                        amenity.setImage(jo.getString("img_url"));
//                        amenitiesbeanList.add(amenity);
//                    }
//                    ArrayList<String> checkedIdList= new ArrayList<>();
//                    AmenitiesDataAdapter mAdapter = new AmenitiesDataAdapter(amenitiesbeanList, HotelierAddDetails.this,checkedIdList);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        } else if (tag.equalsIgnoreCase("adddetails")) {
            pDialog.dismiss();
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {
                        //  Log.e("success", job.getBoolean("success") + " ");
                        //Log.e("msg", job.getString("msg"));
                        ssp.setPRE_usertype("buysell");
                        ssp.setPRE_auth_token(auth_token);
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                        // Intent intent = new Intent(HotelierAddDetails.this, HotelierShowRequests.class);
                        //startActivity(intent);
                        //finish();
                    } else {
                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    public void oncbClicked(int position, boolean checked) {
        if (checked) {
            subcheckedIdList.add(amenitiesbeanList.get(position).getId());
            Log.e("checked", amenitiesbeanList.get(position).getId().toString());
        } else {
            subcheckedIdList.remove(amenitiesbeanList.get(position).getId());
            Log.e("checked", amenitiesbeanList.get(position).getId().toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == 100 || requestCode == 101){
//            amenities_text.setText( "0 Selected");
//        }
//    }
}
