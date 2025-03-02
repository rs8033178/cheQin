package com.cheqin.booking.User;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cheqin.booking.Adapter.AmenitiesDataAdapter;
import com.cheqin.booking.Adapter.HotelTypeDataAdapter;
import com.cheqin.booking.Adapter.RoomTypeDataAdapter;
import com.cheqin.booking.Adapter.WeddingServicesAdapter;
import com.cheqin.booking.Adapter.WeddingVenueAdapter;
import com.cheqin.booking.Bean.Amenitiesbean;
import com.cheqin.booking.Bean.ConfirmationBean;
import com.cheqin.booking.Bean.Hoteltypebean;
import com.cheqin.booking.Bean.RoomTypeBean;
import com.cheqin.booking.Bean.WeddingService;
import com.cheqin.booking.Bean.WeddingVenue;
import com.cheqin.booking.DateSelectionActivity;
import com.cheqin.booking.MainActivity;
import com.cheqin.booking.MultiDateSelectionActivity;
import com.cheqin.booking.R;
import com.cheqin.booking.activities.login.LoginSignupActivity;
import com.cheqin.booking.dialogs.PostResquestConfirmationDialog;
import com.cheqin.booking.gcm.NotificationListActivity;
import com.cheqin.booking.gcm.QuickstartPreferences;
import com.cheqin.booking.gcm.RegistrationIntentService;
import com.cheqin.booking.gcm.SharedPreference;
import com.cheqin.booking.gcm.SharedPreference1;
import com.cheqin.booking.gcm.SharedPreferenceRoom;
import com.cheqin.booking.models.City;
import com.cheqin.booking.utils.AsyncTaskListener;
import com.cheqin.booking.utils.Common;
import com.cheqin.booking.utils.Constants;
import com.cheqin.booking.utils.GPSTracker;
import com.cheqin.booking.utils.HttpAsync;
import com.cheqin.booking.utils.KeyboardUtil;
import com.cheqin.booking.utils.Progressloadingdialog;
import com.cheqin.booking.utils.SettingSharedPrefs;
import com.cheqin.booking.viewmodel.UserBookingViewModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.cheqin.booking.utils.Common.calculateDateDiff;
import static com.cheqin.booking.utils.Common.getFormattedAmount;
import static com.cheqin.booking.utils.Common.logException;
import static com.cheqin.booking.utils.Constants.DATE_FORMAT_PATTERN;

;

public class UserBooking extends AppCompatActivity implements AmenitiesDataAdapter.OnCheckBoxClicked,
        WeddingServicesAdapter.WeddingServiceClickListener,
        AsyncTaskListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final String TAG = UserBooking.class.getSimpleName();
    private static final int MY_REQUEST_CODE = 201;
    private static final int REFRESH_NOTIFICATION_COUNT = 202;
    private static final int REQUEST_CODE_SELECT_DATE = 203;
    private static final int REQUEST_CODE_SELECT_SPECIFIC_WEDDING_DATE = 204;

    private RadioGroup rg_booking_type;
    private LinearLayout llHotelBooking;
    private LinearLayout llWeddingBooking;
    private TextView tv_venue, tv_additional_services, tv_selected_wedding_dates, tv_wedding_adults;
    private RadioButton rbSpecificDates, rbFlexibleDates;
    private EditText edtBudgetVenue, edtBudgetServices, edtGuestCount;
    private TextView tvSelectedVenue, tvSelectedServices;
    private LinearLayout ll_date_selection;


    private TextView chkin_txt_day = null;
    private TextView chkin_txt_month = null;
    private TextView chkin_txt_year = null;
    private TextView chkin_txt_day1 = null;
    private TextView chkin_txt_month1 = null;
    private TextView chkin_txt_year1 = null;
    private TextView chkout_txt_day = null;
    private TextView chkout_txt_month = null;
    private TextView chkout_txt_year = null;
    private LinearLayout nightCheckInDate = null;
    private LinearLayout nightCheckOutDate = null;
    private LinearLayout hourlyCheckinDate = null;
    private TextView user_rooms = null;
    private TextView user_adults = null;
    private TextView user_childrens = null;
    private ImageButton room_plus_button = null;
    private ImageButton room_minus_button = null;
    private TextView rooms = null;
    private ImageButton adults_plus_button = null;
    private ImageButton adults_minus_button = null;
    private TextView adults = null;
    private ImageButton children_plus_button = null;
    private ImageButton children_minus_button = null;
    private TextView childrens = null;
    private LinearLayout amenity = null;
    private LinearLayout hoteltype = null, roomType = null;
    //    private EditText price = null;
    private TextView budgettext = null;
    private Button bargainNowButton = null;
    private TextView hoteltype_text = null, roomType_text = null;
    private TextView amenities_text = null;
    private int count;
    private int count1;
    private int count2;
    private String url = "";
    private HashMap<String, String> para = null;
    public static HashMap<String, String> para1 = null;
    private HashMap<String, String> parameter = null;
    private HashMap<String, String> para2 = null;
    private HashMap<String, String> parameters = null;
    private HashMap<String, String> prof_parameter = null;
    //    private HashMap<String, String> para_lat = null;
    private UserBooking asyncTaxxskListener = null;
    private AutoCompleteTextView autoselectcity = null;

    private String dayDifference = null;
    private int lowest_offers;
    private int messages;
    private int confirmed_bookings;
    private int cancelled_counts;
    private ArrayList<String> names = null;
    private static final String TAG_RESULT = "predictions";
    private String browserKey;
    private Double total_price_long;
    private String startdate = null;
    private String enddate = null;
    ArrayList<String> checked;

    final Context mContext = this;

    private Progressloadingdialog progressDialog = null;
    private Progressloadingdialog progressDialog1 = null;
    private SharedPreferences check = null, roomPrefCheck;


    private Calendar nightlyStartCalendar = null, nightlyEndCalendar = null, hourlyStartCalendar, hourlyEndCalendar;
    private Calendar weddingSpecificCalendar1, weddingSpecificCalendar2, weddingSpecificCalendar0;
    private TimePickerDialog timePickerDialogCheckin = null, timePickerDialogCheckOut = null;

    private CardView grpBookingCard;
    private CheckedTextView grpBookingCb;
    private ViewGroup grpBookingDetails;
    private EditText grpBookingBudget;
    private TextView grpBookingTotalDesc, getBookingTotalLabel, grpBookingTotalPrice;
    private TextView grpBookingSpecialRequests;

    //LinearLayout dateSelectorView;
    private String current_time = null;
    private List<String> cities = null;
    private List<String> placeids = null;
    private Toolbar toolBar = null;
    private DrawerLayout drawer = null;
    private Toolbar mToolbar;

    DecimalFormat commaformatter;

    private ListView mRecyclerView = null;

    private List<Amenitiesbean> amenitiesbeanList;
    private List<Hoteltypebean> hotelcategories;
    private List<RoomTypeBean> roomTypeBeanList;
    ListView hotel_listView, roomTypeListview;
    private String hotel_id = null;
    private String hotel_info = null;
    private String display_name = null;

    private String room_id = null, room_info = null, room_rank = null;
    private TextView txt_start_time = null;
    private TextView txt_end_time = null;
    public static final String TIMEPICKER_TAG = "timepicker";
    public Progressloadingdialog progressLoadingDialog;
    private HashMap<String, String> paraHashMap = null;
    //    private SharedPreferences mpref = null;
    private String auth_token = null;

    private AutoCompleteTextView auto_city = null;
    private AutoCompleteTextView auto_area = null;
    //    private TextView auto_location = null;
    private RadioGroup radioGroup = null;

    private TextView navUserName = null;
    private NavigationView nv = null;
    private String user_type = null;

    private ImageView clear_butt_city = null;
    private ImageView clear_butt_area = null;

    private String city = "";
    private String urcity = null;
    private String urarea = null;

    private boolean isNightlyBasis = true;
    private View nightBasisSelectionLayout = null;
    private View hourlyBasisSelectionLayout = null;

    private Button btn_noconn = null;
    private LinearLayout layout_noconn = null;
    private LinearLayout main_layout = null;
    private ImageView my_loc = null;
    private LinearLayout select_all = null;
    SettingSharedPrefs ssp;
    private String email = null;
    private String uid = null;
    private String amenities = "";
    private String amenities_long = "";
    private CardView auto_visible = null;
    boolean changing = false;
    Dialog dialog = null;
    private TextView name_prefix = null;
    private TextView legalMenuOption;
    //Location
    LinearLayout base_layout;
    //    private GoogleApiClient googleApiClient = null;
    private String str_latitude = "";
    private String str_longitude = "";
    private SharedPreferences locationPref = null;

    private GPSTracker gps;
    private String URL = "http://maps.googleapis.com/maps/api/geocode/json?";
    //    public Double latitude = 20.5937;
//    public Double longitude = 78.9629;
    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 200;
    //  private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;
    // private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 102;
    private String loc = null;
    private String state = null;
    private String country = null;
    private String auto_loc = null;
    private String city_place_id = null;

    private static final int REQUEST_CHECK_SETTINGS = 876;

    ImageView nav_butt;


    //GCM

    private static final int MY_PERMISSIONS_RECEIVE_SMS = 101;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    ArrayList<String> checkedIdList;
    ArrayList<String> subcheckedIdList;

    private WeddingVenue selectedWeddingVenue;
    private List<WeddingVenue> weddingVenueDataList;
    private List<WeddingService> weddingServiceDataList;
    private ArrayList<Long> weddingVenueIdList = new ArrayList<>();
    private ArrayList<Long> selectedWeddingVenueIdList = new ArrayList<>();
    private ArrayList<Long> weddingServicesIdList = new ArrayList<>();
    private ArrayList<Long> selectedWeddingServiceIdList = new ArrayList<>();

    private GoogleApiClient mGoogleApiClient;
    //    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(20.5937, 78.9629), new LatLng(20.5937, 78.9629));
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private HashMap<String, String> para_lat = null;
    // private Double city_latitude = 20.5937;
    //private Double city_longitude = 78.9629;
    private String curr = null;
    private HashMap<String, String> para_curr = null;
    private String curr_url = " http://api.mypillows.company/my_deals/get_currency/get.json?";
    public static String curr_code = null;
    public static String Country = "";
//    private CardView card_curr = null;


    Geocoder geoCoder;
    List<Address> addresses;
    public static final int ACCESS_FINE_LOCATION = 1;


    private Location mLocation;
    private LocationManager mLocationManager;

    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private LocationManager locationManager;
    Location location;
    private ArrayList<City> cityList;
    private String currencyCode = "INR";
    private String currencySymbol;
    private UserBookingViewModel model;
    private Double latitude;
    private Double longitude;
    private Double req_latitude;
    private Double req_longitude;
    private AsyncTaskListener asyncTaskListener;


    // Create a listener to track request state updates.
    private InstallStateUpdatedListener installStateUpdatedListener = state -> {
        // Show module progress, log state, or install the update.
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }
    };
    private AppUpdateManager appUpdateManager;
    private SimpleDropDownAdapter citySuggestionAdapter;
    private SimpleDropDownAdapter areaSuggestionAdapter;
    private int unread_push_counts;
    private TextView tvNotificationCount;
    private String totalGroupBookingPrice;

    private String selectedArea;
    private String selectedCityName;

    private LinearLayout ll_wedding_guest;
    private Integer weddingAdultCount;
    private ArrayList<Calendar> selectedCalendar = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(UserBookingViewModel.class);
        setContentView(R.layout.user_content_main);

        subcheckedIdList = new ArrayList<String>();
        checkedIdList = new ArrayList<String>();
        SharedPreference sharedPreference = new SharedPreference();
        amenitiesbeanList = sharedPreference.getFavorites(UserBooking.this);

        buildWeddingDataList();

        browserKey = sharedPreference.getApiKey(getApplicationContext());
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        rg_booking_type = findViewById(R.id.rg_booking_type);
        llHotelBooking = findViewById(R.id.container_hotel_booking);
        llWeddingBooking = findViewById(R.id.container_wedding_booking);
        rbSpecificDates = findViewById(R.id.rb_specific_date);
        rbFlexibleDates = findViewById(R.id.rb_flexible_date);

        grpBookingCard = findViewById(R.id.group_booking_card);
        grpBookingCb = findViewById(R.id.cb_group_booking);
        grpBookingDetails = findViewById(R.id.ll_grp_booking_details);
        grpBookingBudget = findViewById(R.id.edt_grp_price);
        grpBookingSpecialRequests = findViewById(R.id.edt_grp_special_req);
        grpBookingTotalDesc = findViewById(R.id.tv_grp_budget_desc);
        grpBookingTotalPrice = findViewById(R.id.tv_grp_total_price);
        getBookingTotalLabel = findViewById(R.id.tv_grp_total_label);

        progressDialog = new Progressloadingdialog(UserBooking.this, "Posting Request...");
        progressDialog.setCancelable(false);

        commaformatter = new DecimalFormat("#,###,###");
        mGoogleApiClient = new GoogleApiClient.Builder(UserBooking.this)
                // .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mGoogleApiClient.connect();
        //check whether location service is enable or not in your  phone

        ssp = SettingSharedPrefs.getInstance(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent type = getIntent();
        user_type = type.getStringExtra("type");
        base_layout = (LinearLayout) findViewById(R.id.base_layout);
        main_layout = findViewById(R.id.main);
        nav_butt = (ImageView) findViewById(R.id.nav_butt);
        chkin_txt_day = (TextView) findViewById(R.id.chkin_txt_day);
        chkin_txt_month = (TextView) findViewById(R.id.chkin_txt_month);
        chkin_txt_year = (TextView) findViewById(R.id.chkin_txt_year);
        chkin_txt_day1 = (TextView) findViewById(R.id.chkin_txt_day1);
        chkin_txt_month1 = (TextView) findViewById(R.id.chkin_txt_month1);
        chkin_txt_year1 = (TextView) findViewById(R.id.chkin_txt_year1);
        chkout_txt_day = (TextView) findViewById(R.id.chkout_txt_day);
        chkout_txt_month = (TextView) findViewById(R.id.chkout_txt_month);
        chkout_txt_year = (TextView) findViewById(R.id.chkout_txt_year);
        nightCheckInDate = (LinearLayout) findViewById(R.id.nightCheckInDate);
        nightCheckOutDate = (LinearLayout) findViewById(R.id.nightCheckOutDate);
        hourlyCheckinDate = (LinearLayout) findViewById(R.id.hourlyCheckinDate);
        user_rooms = (TextView) findViewById(R.id.user_rooms);
        user_adults = (TextView) findViewById(R.id.user_adults);
        user_childrens = (TextView) findViewById(R.id.user_childrens);
//        price = (EditText) findViewById(R.id.budget);
        txt_start_time = (TextView) findViewById(R.id.txt_start_time);
        txt_end_time = (TextView) findViewById(R.id.txt_end_time);
        amenity = (LinearLayout) findViewById(R.id.linear5);
        hoteltype = (LinearLayout) findViewById(R.id.linear6);
        hoteltype_text = (TextView) findViewById(R.id.hoteltype_text);

        roomType = (LinearLayout) findViewById(R.id.linearRoomType);
        roomType_text = (TextView) findViewById(R.id.roomType_text);

        amenities_text = (TextView) findViewById(R.id.amenities_text);
        auto_city = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_city);

        auto_area = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView_area);

        clear_butt_city = (ImageView) findViewById(R.id.clear_butt_city);


        clear_butt_area = (ImageView) findViewById(R.id.clear_butt_area);
        nightBasisSelectionLayout = (LinearLayout) findViewById(R.id.line1);
        hourlyBasisSelectionLayout = (LinearLayout) findViewById(R.id.line2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        layout_noconn = (LinearLayout) findViewById(R.id.lnlayout_noconnection);
        btn_noconn = (Button) findViewById(R.id.btn_noconnection);
        select_all = (LinearLayout) findViewById(R.id.select_all);
        auto_visible = (CardView) findViewById(R.id.auto_visible);

        tv_venue = findViewById(R.id.textview_venue);
        tv_additional_services = findViewById(R.id.textview_additonal_services);

        tv_selected_wedding_dates = findViewById(R.id.textview_selected_date);
        edtBudgetVenue = findViewById(R.id.edt_budget_venue);
        edtBudgetServices = findViewById(R.id.edt_budget_services);
        edtGuestCount = findViewById(R.id.edt_guest_count);
        ll_wedding_guest = findViewById(R.id.ll_wedding_guest);
        tvSelectedVenue = findViewById(R.id.textview_selected_venue);
        tvSelectedServices = findViewById(R.id.textview_selected_services);
        rbSpecificDates = findViewById(R.id.rb_specific_date);
        rbFlexibleDates = findViewById(R.id.rb_flexible_date);

        setCurrencySymbol();

        rg_booking_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_hotels) {
                    llHotelBooking.setVisibility(View.VISIBLE);
                    llWeddingBooking.setVisibility(View.GONE);
                } else {
                    llHotelBooking.setVisibility(View.GONE);
                    llWeddingBooking.setVisibility(View.VISIBLE);
                }
            }
        });

        rbSpecificDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearWeddingDates();
            }
        });

        rbFlexibleDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearWeddingDates();
            }
        });

        findViewById(R.id.ll_venue_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVenueSelectionDialog();
            }
        });

        findViewById(R.id.ll_service_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdditionalServicesDialog();
            }
        });


        grpBookingCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grpBookingCb.setChecked(!grpBookingCb.isChecked());

                if (grpBookingCb.isChecked()) {
                    grpBookingDetails.setVisibility(View.VISIBLE);
                } else {
                    grpBookingDetails.setVisibility(View.GONE);
                }

            }
        });

        /*grpBookingCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    grpBookingDetails.setVisibility(View.VISIBLE);
                } else {
                    grpBookingDetails.setVisibility(View.GONE);
                }

            }
        });*/

        grpBookingBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalGroupBookingPrice(s.toString());
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navUserName = (TextView) headerView.findViewById(R.id.navUserName);
        name_prefix = (TextView) headerView.findViewById(R.id.name_prefix);
        legalMenuOption = navigationView.findViewById(R.id.nav_legal);
        // dateSelectorView=(LinearLayout)findViewById(R.id.dateSelectorView);
        defaultCheckAmenities();

        legalMenuOption.setOnClickListener(v -> {
            Intent prof4 = new Intent(UserBooking.this, UserTermsandConditions.class);
            prof4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(prof4);
        });


        asyncTaskListener = UserBooking.this;


        auth_token = ssp.getPRE_auth_token();

        locationPref = getSharedPreferences("latlong", MODE_PRIVATE);
//        SharedPreferences.Editor edt = locationPref.edit();
//        edt.putString("lat", "12.9356");
//        edt.putString("long", "77.7023");
//        edt.commit();

//        checkStartConnection();

//  **********************    Getting Counts to Display    ***********************

//        parameters = new HashMap<>();
//        parameters.put("auth_token", auth_token);
//        new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + "user_requests/buyer_dashboard_count/get.json?", parameters, 1, "counts").execute();

//  **********************    End of Getting Counts to Display    ***********************


        count = Integer.valueOf((String) user_rooms.getText());
        count1 = Integer.valueOf((String) user_adults.getText());
        count2 = Integer.valueOf((String) user_childrens.getText());

        select_all.setOnClickListener(v -> {
            final TextView select_all;
            final TextView cancel;
            final Dialog dialog2 = new Dialog(UserBooking.this);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setContentView(R.layout.prompt_select);
            dialog2.setCancelable(true);
            dialog2.show();

            room_minus_button = (ImageButton) dialog2.findViewById(R.id.rooms_minus_button1);
            room_plus_button = (ImageButton) dialog2.findViewById(R.id.rooms_plus_button);
            rooms = (TextView) dialog2.findViewById(R.id.rooms);
            adults_plus_button = (ImageButton) dialog2.findViewById(R.id.adult_plus_button);
            adults_minus_button = (ImageButton) dialog2.findViewById(R.id.adult_minus_button1);
            adults = (TextView) dialog2.findViewById(R.id.adults);
            children_plus_button = (ImageButton) dialog2.findViewById(R.id.children_plus_button);
            children_minus_button = (ImageButton) dialog2.findViewById(R.id.children_minus_button1);
            childrens = (TextView) dialog2.findViewById(R.id.childrens);

            count = Integer.valueOf((String) user_rooms.getText());
            count1 = Integer.valueOf((String) user_adults.getText());
            count2 = Integer.valueOf((String) user_childrens.getText());
            rooms.setText(user_rooms.getText());
            adults.setText(user_adults.getText());
            childrens.setText(user_childrens.getText());

            room_plus_button.setOnClickListener(v18 -> {
                count++;
                if (count > 99) {
                    count = 99;
                    room_plus_button.setEnabled(false);
                    rooms.setText(Integer.toString(99));
//                            user_rooms.setText(Integer.toString(99));
                } else {
                    rooms.setText(Integer.toString(count));
//                            user_rooms.setText(Integer.toString(count));
                }
            });
            room_minus_button.setOnClickListener(v17 -> {
                if (count > 1) {
                    count--;
                    room_plus_button.setEnabled(true);
                    rooms.setText(Integer.toString(count));

                } else {
                    rooms.setText(Integer.toString(1));
//                            user_rooms.setText(Integer.toString(1));
                }
            });
            adults_plus_button.setOnClickListener(v16 -> {
                count1++;
                if (count1 > 999) {
                    count1 = 999;
                    adults_plus_button.setEnabled(false);
                    adults.setText(Integer.toString(999));
//                            user_adults.setText(Integer.toString(99));
                } else {
                    adults.setText(Integer.toString(count1));
//                            user_adults.setText(Integer.toString(count1));
                }
            });
            adults_minus_button.setOnClickListener(v15 -> {
                if (count1 > 1) {
                    count1--;
                    adults_plus_button.setEnabled(true);
                    adults.setText(Integer.toString(count1));

                } else {
                    adults.setText(Integer.toString(1));
//                            user_adults.setText(Integer.toString(1));
                }
            });
            children_plus_button.setOnClickListener(v14 -> {
                count2++;
                if (count2 > 99) {
                    count2 = 99;
                    children_plus_button.setEnabled(false);
                    childrens.setText(Integer.toString(99));
//                            user_childrens.setText(Integer.toString(99));
                } else {
                    childrens.setText(Integer.toString(count2));

                }
            });
            children_minus_button.setOnClickListener(v1 -> {
                if (count2 > 0) {
                    count2--;
                    children_plus_button.setEnabled(true);
                    childrens.setText(Integer.toString(count2));
//                            user_childrens.setText(Integer.toString(count2));
                } else {
                    childrens.setText(Integer.toString(0));
//                            user_childrens.setText(Integer.toString(0));
                }
            });

            select_all = (TextView) dialog2.findViewById(R.id.select_all_ok);
            select_all.setOnClickListener(v12 -> {
                user_rooms.setText(Integer.toString(count));
                user_adults.setText(Integer.toString(count1));
                user_childrens.setText(Integer.toString(count2));

                updateGroupBookingView();
                dialog2.dismiss();
            });

            cancel = (TextView) dialog2.findViewById(R.id.cancel_all);
            cancel.setOnClickListener(v13 -> dialog2.dismiss());
        });

        nav_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkLocation()) {
                    auto_area.dismissDropDown();
                    clear_butt_area.setVisibility(View.GONE);
                    updateCurrentLocation();

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to find location! Try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //autocomplete text for city

        configureCitySuggestionView(sharedPreference);

        //autocomplete text for area

        configureAreaSuggestionView(sharedPreference);


        clear_butt_city.setOnClickListener(v -> {
            if (auto_city.getText().toString().length() > 0) {
                auto_city.setText("");
                nav_butt.setVisibility(View.VISIBLE);
                clear_butt_city.setVisibility(View.GONE);
                auto_visible.setVisibility(View.GONE);
                auto_area.setEnabled(true);
                auto_city.setEnabled(true);
                if (!auto_area.getText().toString().equalsIgnoreCase("")) {
                    auto_area.setText("");
                }
            }
        });


        clear_butt_area.setOnClickListener(v -> {
            auto_area.dismissDropDown();
            auto_area.setText("");
            clear_butt_area.setVisibility(View.GONE);
        });

        tv_selected_wedding_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeddingDateSelectionScreen();
            }
        });

        rbSpecificDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeddingDateSelectionScreen();
            }
        });

        rbFlexibleDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeddingDateSelectionScreen();
            }
        });

        amenity.setOnClickListener(v -> {

            final Button amenities_ok;
            dialog = new Dialog(UserBooking.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.prompt_amenities);
            dialog.setCancelable(true);
            dialog.show();
            amenities_ok = (Button) dialog.findViewById(R.id.amenities_ok);
            mRecyclerView = (ListView) dialog.findViewById(R.id.recyclerView);
            amenities_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAmentities();
                    dialog.dismiss();
                }
            });

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    subcheckedIdList = null;
                    subcheckedIdList = new ArrayList<String>(checkedIdList);

                    for (int i = 0; i < checkedIdList.size(); i++) {
                        Log.e("checkedIdList" + i, checkedIdList.get(i));
                    }
                    dialog.dismiss();
                }
            });

            AmenitiesDataAdapter mAdapter = new AmenitiesDataAdapter(amenitiesbeanList, UserBooking.this, checkedIdList);
            mRecyclerView.setAdapter(mAdapter);
        });

        hoteltype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hotelcategories = new ArrayList<>();
                SharedPreference1 sharedPreference1 = new SharedPreference1();
                hotelcategories = sharedPreference1.getFavorites(UserBooking.this);
                final Dialog dialog1 = new Dialog(UserBooking.this);
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
                        hoteltype_text.setVisibility(View.VISIBLE);
                        hoteltype_text.setText(hotel_info);
                        dialog1.dismiss();
                    }
                });
                HotelTypeDataAdapter hotelTypeDataAdapter = new HotelTypeDataAdapter(UserBooking.this, hotelcategories);
                hotel_listView.setAdapter(hotelTypeDataAdapter);
            }
        });


        roomType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                roomTypeBeanList = new ArrayList<>();
                SharedPreferenceRoom sharedPreferenceRoom = new SharedPreferenceRoom();
                roomTypeBeanList = sharedPreferenceRoom.getFavorites(UserBooking.this);
                final Dialog dialog1 = new Dialog(UserBooking.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.prompt_roomtype);
                dialog1.setCancelable(true);
                dialog1.show();
                roomTypeListview = (ListView) dialog1.findViewById(R.id.room_list);
                roomTypeListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                roomTypeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        roomTypeSelection(roomTypeBeanList.get(position).getId().toString(), roomTypeBeanList.get(position).getName().toString()
                                , roomTypeBeanList.get(position).getName().toString());
                        dialog1.dismiss();
                    }
                });
                RoomTypeDataAdapter roomTypeDataAdapter = new RoomTypeDataAdapter(UserBooking.this, roomTypeBeanList);
                roomTypeListview.setAdapter(roomTypeDataAdapter);
            }
        });


        nightCheckInDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckinDialog();
            }
        });

        nightCheckOutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckoutDialog();
            }
        });

        hourlyCheckinDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHourDatePicker();

            }
        });
        txt_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogCheckin.show();
            }
        });

        txt_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogCheckOut.show();
            }
        });


        bargainNowButton = (Button) findViewById(R.id.post_request);

//  **************************     Navigation Bar    ***************************

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        SpannableString title = new SpannableString(getString(R.string.app_name));
        title.setSpan(new com.cheqin.booking.animation.TypefaceSpan(getApplicationContext(), "gotham.ttf"), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(title);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(R.string.utel_app);

        nv = (NavigationView) findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(UserBooking.this, drawer, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        nv.setItemTextColor(new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{}
                },
                new int[]{
                        Color.RED,
                        Color.RED,
                        Color.GRAY
                }
        ));

// FOR NAVIGATION VIEW ITEM ICON COLOR
        nv.setItemIconTintList(new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, // disabled
                        new int[]{android.R.attr.state_enabled}, // enabled
                        new int[]{-android.R.attr.state_checked}, // unchecked
                        new int[]{android.R.attr.state_pressed}  // pressed

                },

                new int[]{
                        Color.BLACK,
                        Color.BLACK,
                        Color.BLACK,
                        Color.RED
                }
        ));

        toggle.syncState();

        if (!ssp.getPRE_user_login()) {
            navUserName.setText("Guest");
            nv.getMenu().findItem(R.id.log_in).setVisible(true);
            //nv.getMenu().findItem(R.id.terms_and_conditions).setVisible(true);
            nv.getMenu().findItem(R.id.contact_us).setVisible(true);
            nv.getMenu().findItem(R.id.requested_offer).setVisible(false);
            nv.getMenu().findItem(R.id.my_posted_offers).setVisible(false);
            nv.getMenu().findItem(R.id.request_to_offer).setVisible(false);
            nv.getMenu().findItem(R.id.my_offers).setVisible(false);
            nv.getMenu().findItem(R.id.messages).setVisible(false);
            nv.getMenu().findItem(R.id.hotel_gallery).setVisible(false);
            nv.getMenu().findItem(R.id.confirm_booking).setVisible(false);
            nv.getMenu().findItem(R.id.cancel_request).setVisible(false);
            nv.getMenu().findItem(R.id.profile).setVisible(false);
            nv.getMenu().findItem(R.id.hotel_profile).setVisible(false);
            nv.getMenu().findItem(R.id.rate_us).setVisible(false);
            nv.getMenu().findItem(R.id.send_feedback).setVisible(false);
            nv.getMenu().findItem(R.id.log_out).setVisible(false);
        } else {
            ssp.setPRE_GCM_MSG_d_status(false);
            nv.getMenu().findItem(R.id.log_in).setVisible(false);
            //    nv.getMenu().findItem(R.id.terms_and_conditions).setVisible(true);
            nv.getMenu().findItem(R.id.contact_us).setVisible(true);
            nv.getMenu().findItem(R.id.requested_offer).setVisible(false);
            nv.getMenu().findItem(R.id.my_posted_offers).setVisible(false);
            nv.getMenu().findItem(R.id.request_to_offer).setVisible(true);
            checkStartConnection();
            nv.getMenu().findItem(R.id.hotel_gallery).setVisible(false);
            nv.getMenu().findItem(R.id.hotel_profile).setVisible(false);
            nv.getMenu().findItem(R.id.profile).setVisible(true);
            nv.getMenu().findItem(R.id.rate_us).setVisible(false);
            nv.getMenu().findItem(R.id.send_feedback).setVisible(true);
            nv.getMenu().findItem(R.id.log_out).setVisible(true);
            checkStartConnection();
        }
        nv.setNavigationItemSelectedListener(this);

//  ****************************      End of navigation drawer     ************************

//  ****************************     Start Date picker      ****************************

        // SimpleDateFormat sdf = new SimpleDateFormat("MM dd yyyy");
        //String currentDateandTime = sdf.format(new Date());

        nightlyStartCalendar = Calendar.getInstance();
        nightlyEndCalendar = Calendar.getInstance();
        hourlyStartCalendar = Calendar.getInstance();
        hourlyEndCalendar = Calendar.getInstance();

        nightlyEndCalendar.add(Calendar.DAY_OF_MONTH, 1);

        setDate(nightlyStartCalendar.get(Calendar.DAY_OF_MONTH), nightlyStartCalendar.get(Calendar.MONTH), nightlyStartCalendar.get(Calendar.YEAR), chkin_txt_day, chkin_txt_month, chkin_txt_year);
        setDate(nightlyEndCalendar.get(Calendar.DAY_OF_MONTH), nightlyEndCalendar.get(Calendar.MONTH), nightlyEndCalendar.get(Calendar.YEAR), chkout_txt_day, chkout_txt_month, chkout_txt_year);

        //setSpecificWeddingDate(tv_selected_wedding_dates0, weddingSpecificCalendar0.get(Calendar.YEAR), weddingSpecificCalendar0.get(Calendar.MONTH), weddingSpecificCalendar0.get(Calendar.DAY_OF_MONTH));

        timePickerDialogCheckin = new TimePickerDialog(UserBooking.this, timeListnerCheckIn, hourlyStartCalendar.get(Calendar.HOUR_OF_DAY), 0, false);
        timePickerDialogCheckOut = new TimePickerDialog(UserBooking.this, timeListnerCheckOut, hourlyEndCalendar.get(Calendar.HOUR_OF_DAY), 0, false);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rad_long_stay) {
                    nightlyEndCalendar = Calendar.getInstance();
                    nightlyEndCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    setDate(nightlyStartCalendar.get(Calendar.DAY_OF_MONTH), nightlyStartCalendar.get(Calendar.MONTH), nightlyStartCalendar.get(Calendar.YEAR), chkin_txt_day, chkin_txt_month, chkin_txt_year);
                    setDate(nightlyEndCalendar.get(Calendar.DAY_OF_MONTH), nightlyEndCalendar.get(Calendar.MONTH), nightlyEndCalendar.get(Calendar.YEAR), chkout_txt_day, chkout_txt_month, chkout_txt_year);
                    nightBasisSelectionLayout.setVisibility(View.VISIBLE);
                    hourlyBasisSelectionLayout.setVisibility(View.GONE);

                    isNightlyBasis = true;

//                    price.setHint("Budget per Night only");

                    //  nightlyCheckInDPDialog = new DatePickerDialog(UserBooking.this, nightlyDatePickerCheckin, nightlyStartCalendar.get(Calendar.DAY_OF_MONTH), nightlyStartCalendar.get(Calendar.MONTH), nightlyStartCalendar.get(Calendar.YEAR));

                } else if (checkedId == R.id.rad_short_stay) {
                    hourlyStartCalendar.add(Calendar.HOUR_OF_DAY, 1);

                  //  hourlyEndCalendar.setTime(hourlyStartCalendar.getTime());
                    hourlyEndCalendar.set(Calendar.HOUR_OF_DAY, 23);
                  //  hourlyEndCalendar.set(Calendar.MINUTE, 59);

                    isNightlyBasis = false;
                    setDate(hourlyStartCalendar.get(Calendar.DAY_OF_MONTH), hourlyStartCalendar.get(Calendar.MONTH), hourlyStartCalendar.get(Calendar.YEAR), chkin_txt_day1, chkin_txt_month1, chkin_txt_year1);
                    nightBasisSelectionLayout.setVisibility(View.GONE);
                    hourlyBasisSelectionLayout.setVisibility(View.VISIBLE);

                    DateFormat date = new SimpleDateFormat("hh:mm a");
                    // date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                    setTime(date.format(hourlyStartCalendar.getTime()), txt_start_time);
                    setTime(date.format(hourlyEndCalendar.getTime()), txt_end_time);
                }
                updateGroupBookingView();
            }
        });
        radioGroup.check(R.id.rad_long_stay);


//   ********************     End of date picker    ***********************

//   ********************     Getting current Location    **********************
        getLocation();

        bargainNowButton.setOnClickListener(v -> checkConnection());
        SharedPreferenceRoom sharedPreferenceRoom = new SharedPreferenceRoom();
        roomTypeBeanList = sharedPreferenceRoom.getFavorites(UserBooking.this);
        if (roomTypeBeanList != null && roomTypeBeanList.size() > 0) {
            roomTypeSelection(roomTypeBeanList.get(0).getId().toString(), roomTypeBeanList.get(0).getName().toString()
                    , roomTypeBeanList.get(0).getName().toString());
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("hashMap") && bundle.containsKey("bargainNowButton")) {
            Log.i("HAS_intent", " -> " + bundle.containsKey("hashMap") + bundle.containsKey("bargainNowButton") + "okay");
            progressDialog.show();
            HashMap<String, String> params = (HashMap<String, String>) bundle.getSerializable("hashMap");
            params.put("auth_token", auth_token);
            HttpAsync user_values = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + Constants.POST, params, 2, "user_values");
            user_values.execute();

        }


    } // on create ends

/*
    private void showWeddingGuestSelectionPopup() {
        final TextView select_all;
        final TextView cancel;
        final Dialog dialog2 = new Dialog(UserBooking.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.prompt_select);
        dialog2.setCancelable(true);
        dialog2.show();

        ImageButton adults_plus_button = (ImageButton) dialog2.findViewById(R.id.adult_plus_button);
        ImageButton adults_minus_button = (ImageButton) dialog2.findViewById(R.id.adult_minus_button1);
        TextView adults = (TextView) dialog2.findViewById(R.id.adults);

        dialog2.findViewById(R.id.ll_children_container).setVisibility(View.GONE);
        dialog2.findViewById(R.id.ll_room_container).setVisibility(View.GONE);
        dialog2.findViewById(R.id.divider_rooms).setVisibility(View.GONE);
        dialog2.findViewById(R.id.divider_children).setVisibility(View.GONE);

        cancel = (TextView) dialog2.findViewById(R.id.cancel_all);
        cancel.setOnClickListener(v13 -> dialog2.dismiss());

        weddingAdultCount = Integer.valueOf((String) tv_wedding_adults.getText());
        adults.setText(tv_wedding_adults.getText());

        adults_plus_button.setOnClickListener(v16 -> {
            weddingAdultCount++;
            if (weddingAdultCount > 999) {
                weddingAdultCount = 999;
                adults_plus_button.setEnabled(false);
                adults.setText(Integer.toString(999));
            } else {
                adults.setText(Integer.toString(weddingAdultCount));
            }
        });
        adults_minus_button.setOnClickListener(v15 -> {
            if (weddingAdultCount > 1) {
                weddingAdultCount--;
                adults_plus_button.setEnabled(true);
                adults.setText(Integer.toString(weddingAdultCount));

            } else {
                adults.setText(Integer.toString(1));
            }
        });

        select_all = (TextView) dialog2.findViewById(R.id.select_all_ok);
        select_all.setOnClickListener(v12 -> {
            tv_wedding_adults.setText(Integer.toString(weddingAdultCount));
            dialog2.dismiss();
        });
    }
*/

    private void buildWeddingDataList() {
        SharedPreferences weddingSharePrefs = getSharedPreferences("wedding", MODE_PRIVATE);

        String weddingVenue = weddingSharePrefs.getString("wedding_venues", null);
        String weddingServices = weddingSharePrefs.getString("wedding_vendors", null);

        Gson gson = new Gson();
        weddingVenueDataList = gson.fromJson(weddingVenue, new TypeToken<List<WeddingVenue>>() {
        }.getType());
        weddingServiceDataList = gson.fromJson(weddingServices, new TypeToken<List<WeddingService>>() {
        }.getType());

        selectedWeddingServiceIdList = new ArrayList<>();
        selectedWeddingVenueIdList = new ArrayList<>();
    }


    private void showVenueSelectionDialog() {
        Dialog dialog = new Dialog(UserBooking.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.wedding_venue_dialog_view);
        dialog.setCancelable(true);
        dialog.show();
        TextView title = dialog.findViewById(R.id.title);
        ListView listView = (ListView) dialog.findViewById(R.id.recyclerView);

        title.setText("Select Venue");

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        WeddingVenueAdapter mAdapter = new WeddingVenueAdapter(weddingVenueDataList, UserBooking.this, weddingVenueIdList);
        mAdapter.setListener(new WeddingVenueAdapter.WeddingVenueClickListener() {
            @Override
            public void onWeddingVenueClicked(int position, boolean checked) {
                selectedWeddingVenue = weddingVenueDataList.get(position);
                tvSelectedVenue.setText(selectedWeddingVenue.getName());
                dialog.dismiss();
            }
        });
        listView.setAdapter(mAdapter);
    }

    private void showAdditionalServicesDialog() {
        final Button btnOk;
        Dialog dialog = new Dialog(UserBooking.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.prompt_amenities);
        dialog.setCancelable(true);
        dialog.show();
        TextView title = dialog.findViewById(R.id.title);

        btnOk = (Button) dialog.findViewById(R.id.amenities_ok);
        ListView listView = (ListView) dialog.findViewById(R.id.recyclerView);

        title.setText("Select Services");

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelectedServices();
                dialog.dismiss();
            }
        });

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                dialog.dismiss();
            }
        });

        WeddingServicesAdapter mAdapter = new WeddingServicesAdapter(weddingServiceDataList, UserBooking.this, weddingServicesIdList);
        listView.setAdapter(mAdapter);
    }

    private void saveSelectedVenues() {
        weddingVenueIdList.clear();
        weddingVenueIdList.addAll(selectedWeddingVenueIdList);
    }

    private void saveSelectedServices() {
        weddingServicesIdList.clear();
        weddingServicesIdList.addAll(selectedWeddingServiceIdList);
        tvSelectedServices.setText(weddingServicesIdList.size() + " service(s) selected");
    }

    private void configureAreaSuggestionView(SharedPreference sharedPreference) {
        auto_area.setThreshold(2);
        auto_area.setAdapter(new PlacesAutoCompleteAdapter(this, PlacesRemoteRepo.getInstance(), sharedPreference, true));
        auto_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                selectedArea = "";
                if (auto_area.getText().toString().equalsIgnoreCase("")) {
                    clear_butt_area.setVisibility(View.GONE);
                } else {
                    if (auto_area.isEnabled()) {
                        clear_butt_area.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        auto_area.setOnItemClickListener((parent, view, position, id) -> {

            PlacesAutoCompleteAdapter placesAutoCompleteAdapter = (PlacesAutoCompleteAdapter) auto_area.getAdapter();
            KeyboardUtil.toggle(UserBooking.this);
            selectedArea = placesAutoCompleteAdapter.getItem(position);
            String laturl = "https://maps.googleapis.com/maps/api/place/details/json?";
            para_lat = new HashMap<>();
            para_lat.put("placeid", placesAutoCompleteAdapter.getPlaceId(position));
            para_lat.put("key", browserKey);
            HttpAsync lat = new HttpAsync(getApplicationContext(), asyncTaskListener, laturl, para_lat, 1, "latitude");
            lat.execute();

        });
    }

    private void configureCitySuggestionView(SharedPreference sharedPreference) {
        auto_city.setThreshold(2);
        auto_city.setOnItemClickListener(mAutocompleteClickListener);
        auto_city.setAdapter(new PlacesAutoCompleteAdapter(this, PlacesRemoteRepo.getInstance(), sharedPreference, false));
        auto_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (auto_city.getText().toString().equalsIgnoreCase("")) {
                    clear_butt_city.setVisibility(View.GONE);
                    nav_butt.setVisibility(View.VISIBLE);
                } else {

                    if (auto_city.getText().toString().contains(",")) {
                        selectedCityName = auto_city.getText().toString().substring(0, auto_city.getText().toString().indexOf(","));
                    } else {
                        selectedCityName = auto_city.getText().toString();
                    }
                    clear_butt_city.setVisibility(View.VISIBLE);
                    nav_butt.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateTotalGroupBookingPrice(String perNightPrice) {

        try {
            if (TextUtils.isEmpty(perNightPrice)) {
                totalGroupBookingPrice = "";
            } else {
                double perNightAmount = Double.parseDouble(perNightPrice);
                int roomCount = Integer.parseInt(user_rooms.getText().toString());

                int daysDiff = calculateDateDiff(nightlyStartCalendar.getTimeInMillis(), nightlyEndCalendar.getTimeInMillis());

                double totalPrice = perNightAmount * roomCount * daysDiff;

                updateGroupDuration(daysDiff, roomCount);

                totalGroupBookingPrice = getFormattedAmount(totalPrice);
            }

            if (!TextUtils.isEmpty(totalGroupBookingPrice)) {
                grpBookingTotalPrice.setText(String.format(getString(R.string.rupee_amount), totalGroupBookingPrice));
            } else {
                grpBookingTotalPrice.setText("");
            }
        } catch (Exception e) {
            logException(e);
        }

    }

    private void updateGroupDuration(int daysDiff, int roomCount) {
        grpBookingTotalDesc.setVisibility(View.VISIBLE);
        getBookingTotalLabel.setVisibility(View.VISIBLE);

        String nightString = "";
        String roomString = "";
        if (daysDiff > 1) {
            nightString = String.format(getString(R.string.for_x_nights), daysDiff);
        } else {
            nightString = getString(R.string.for_1_night);
        }

        if (roomCount > 1) {
            roomString = String.format(getString(R.string.for_x_rooms), roomCount);
        } else {
            roomString = getString(R.string.for_1_room);
        }

        grpBookingTotalDesc.setText("(" + nightString + " + " + roomString + ")");

    }

    // shows currency in budget field
    private void setCurrencySymbol() {
        currencySymbol = Common.getCurrencySymbol(currencyCode);
    }

    private void showHourDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog hourlyCheckInDPDialog = DatePickerDialog.newInstance(hourlyDatePickerCheckIn,
                hourlyStartCalendar.get(Calendar.YEAR),
                hourlyStartCalendar.get(Calendar.MONTH),
                hourlyStartCalendar.get(Calendar.DAY_OF_MONTH));
        hourlyCheckInDPDialog.setMinDate(now);
        hourlyCheckInDPDialog.setTitle("Select check-in date");
        hourlyCheckInDPDialog.show(getSupportFragmentManager(), "DatepickerdialogHour");
    }

    private void showCheckoutDialog() {
        // showCheckinDialog();


        startDateSelectionScreen();

        /*DatePickerDialog nightlyCheckOutDPDialog = DatePickerDialog.newInstance(
                nightlyDatePickerCheckOut,
                nightlyEndCalendar.get(Calendar.YEAR),
                nightlyEndCalendar.get(Calendar.MONTH),
                nightlyEndCalendar.get(Calendar.DAY_OF_MONTH));

        Calendar tempDateIncrement = Calendar.getInstance();
        tempDateIncrement.setTimeInMillis(nightlyStartCalendar.getTimeInMillis());
        tempDateIncrement.add(Calendar.DAY_OF_YEAR, 1);
        nightlyCheckOutDPDialog.setMinDate(tempDateIncrement);
        nightlyCheckOutDPDialog.setTitle("Select check-out date");


        nightlyCheckOutDPDialog.setHighlightedDays(new Calendar[]{Calendar.getInstance()});

        nightlyCheckOutDPDialog.show(getSupportFragmentManager(), "DatepickerdialogOut");*/

    }

    private void showCheckinDialog() {

        startDateSelectionScreen();

       /* Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                nightlyDatePickerCheckin,
                nightlyStartCalendar.get(Calendar.YEAR), // initial year selection
                nightlyStartCalendar.get(Calendar.MONTH), // initial month selection
                nightlyStartCalendar.get(Calendar.DAY_OF_MONTH) // inital day selection
        );
        dpd.setMinDate(now);
        dpd.setTitle("Select check in date");
        dpd.show(getSupportFragmentManager(), "DatepickerdialogIn");*/
    }

    private void startDateSelectionScreen() {
        Date startDate = nightlyStartCalendar.getTime();
        Date endDate = nightlyEndCalendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String startDateString = simpleDateFormat.format(startDate);
        String endDateString = simpleDateFormat.format(endDate);

        DateSelectionActivity.startActivityForResult(UserBooking.this, startDateString, endDateString, REQUEST_CODE_SELECT_DATE);
    }

    private void startWeddingDateSelectionScreen() {
        boolean isMultidateSelection = rbFlexibleDates.isChecked();
        MultiDateSelectionActivity.startActivityForResult(this, isMultidateSelection, null, REQUEST_CODE_SELECT_SPECIFIC_WEDDING_DATE);
        /*DatePickerDialog datePickerDialog = new DatePickerDialog();
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

                Calendar c = Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                Calendar currentCal = Calendar.getInstance();
                currentCal.set(Calendar.HOUR_OF_DAY, 0);
                currentCal.set(Calendar.MINUTE, 0);
                currentCal.set(Calendar.SECOND, 0);
                currentCal.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");

                if (c.getTimeInMillis() >= currentCal.getTimeInMillis()) {
                    weddingSpecificCalendar.set(Calendar.HOUR_OF_DAY, 0);
                    weddingSpecificCalendar.set(Calendar.MINUTE, 0);
                    weddingSpecificCalendar.set(Calendar.SECOND, 0);
                    weddingSpecificCalendar.set(Calendar.MILLISECOND, 0);

                    weddingSpecificCalendar.set(year, monthOfYear, dayOfMonth);
                    setSpecificWeddingDate(year, monthOfYear, dayOfMonth);
                    //setDate(day, month, year, chkin_txt_day, chkin_txt_month, chkin_txt_year);
                } else {
                    Toast.makeText(getApplicationContext(), "Date Already Expired!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        datePickerDialog.show(getSupportFragmentManager(), "date_picker");*/
    }

    private void setSpecificWeddingDate(int year, int month, int day) {

        Date d = new Date();
        d.setMonth(month);
        d.setDate(day);

        String dayString = new SimpleDateFormat("dd").format(d);
        String monthString = new SimpleDateFormat("MMM").format(d);
        String yearString = String.valueOf(year);

        String currentText = tv_selected_wedding_dates.getText().toString();
        String newDate = dayString + " " + monthString + " " + yearString;

        if (!TextUtils.isEmpty(currentText)) {
            tv_selected_wedding_dates.setText(currentText + ", " + newDate);
        } else {
            tv_selected_wedding_dates.setText(newDate);
        }

    }

    private void roomTypeSelection(String id, String name, String rank) {
        roomPrefCheck = getSharedPreferences("roomPrefCheck", MODE_PRIVATE);
        SharedPreferences.Editor edt = roomPrefCheck.edit();
        edt.putString("id", id);
        edt.putString("name", name);
        edt.putString("rank", rank);
        edt.commit();
        room_id = id;
        room_info = name;
        room_rank = rank;
        roomType_text.setVisibility(View.VISIBLE);
        roomType_text.setText(room_info);
    }

    private void setAmentities() {
        amenities_text.setVisibility(View.VISIBLE);
        checkedIdList = null;
        checkedIdList = new ArrayList<String>(subcheckedIdList);
        // AmenitiesDataAdapter.checkedIdList2 = null;
        String coun = checkedIdList.size() + "";
//                        Log.e("count", coun);
        amenities_text.setText(coun + " Selected");
    }

    private void defaultCheckAmenities() {
        //To auto select amenities
        if (amenitiesbeanList != null) {
            boolean[] checkBoxState = new boolean[amenitiesbeanList.size()];
            for (int i = 0; i < amenitiesbeanList.size(); i++) {
                if (amenitiesbeanList.get(i).getName().equals("Free WiFi")) {
                    subcheckedIdList.add(amenitiesbeanList.get(i).getId());
                }
            }
            checkedIdList = subcheckedIdList;
            amenities_text.setText(checkedIdList.size() + " Selected");
        }

    }


    private void address(String trim, boolean isCity) {
        if (trim.length() > 0 && !isCity) {
            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
            parameter = new HashMap<>();
            parameter.put("input", trim);
            parameter.put("radius", "100");
            parameter.put("sensor", "true");
            parameter.put("location", latitude + "," + longitude);
            parameter.put("key", browserKey);
            new HttpAsync(getApplicationContext(), asyncTaskListener, url, parameter, 1, "area_search").execute();
        } else if (trim.length() > 0 && isCity) {
            url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";
            parameter = new HashMap<>();
            parameter.put("input", trim);
            parameter.put("key", browserKey);
            //  parameter.put("language ","hi");
            parameter.put("types", "(cities)");
            parameter.put("components", "country:in");
            new HttpAsync(getApplicationContext(), asyncTaskListener, url, parameter, 1, "autocomplete").execute();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        //Log.d("reque", "--->>>>");
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
        //  mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        //mLongitudeTextView.setText(String.valueOf(location.getLongitude() ));
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }


    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Please Enable Location to " +
                        "use current location")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        //  dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                PlacesAutoCompleteAdapter placesAutoCompleteAdapter = (PlacesAutoCompleteAdapter) auto_city.getAdapter();
                auto_visible.setVisibility(View.VISIBLE);
                String laturl = "https://maps.googleapis.com/maps/api/place/details/json?";
                para_lat = new HashMap<>();
                para_lat.put("placeid", placesAutoCompleteAdapter.getPlaceId(position));
                para_lat.put("key", browserKey);
                HttpAsync lat = new HttpAsync(getApplicationContext(), asyncTaskListener, laturl, para_lat, 1, "city_latitude");
                lat.execute();
            } catch (Exception e) {
                e.printStackTrace();
                Common.logException(e);
                Toast.makeText(getApplicationContext(), getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 || requestCode == 101) {
            amenities_text.setText("0 Selected");
        }

        switch (requestCode) {
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK: {

                        gps = new GPSTracker(UserBooking.this);
                        if (gps.canGetLocation()) {

                            try {
                                str_latitude = gps.getLatitude() + "";
                                str_longitude = gps.getLongitude() + "";
                                SharedPreferences.Editor edt = locationPref.edit();
                                edt.putString("lat", String.valueOf(str_latitude));
                                edt.putString("long", String.valueOf(str_longitude));
                                edt.commit();

                                SharedPreferences sp = getSharedPreferences("currentlocation", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("current_longitude", String.valueOf(str_longitude));
                                editor.putString("current_latitude", String.valueOf(str_latitude));
                                editor.apply();
                                getamenities();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Common.logException(e);
                                Toast.makeText(getApplicationContext(), "Unable to fetch location! Try Again!", Toast.LENGTH_SHORT).show();
                            }


                        }

                        break;
                    }

                    case Activity.RESULT_CANCELED: {
                        // The navUserName was asked to change settings, but chose not to
                        finish();


                        break;
                    }
                    default: {
                        break;
                    }
                }

                break;
            case REFRESH_NOTIFICATION_COUNT:
                getCount();

                break;

            case REQUEST_CODE_SELECT_DATE:

                if (data != null && resultCode == Activity.RESULT_OK) {
                    String startDate = data.getStringExtra(Constants.CHECK_IN_DATE);
                    String endDate = data.getStringExtra(Constants.CHECK_OUT_DATE);

                    updateDatePickerViews(startDate, endDate);
                }

                break;

            case REQUEST_CODE_SELECT_SPECIFIC_WEDDING_DATE:

                if (data != null && resultCode == Activity.RESULT_OK) {
                    List<String> selectedDates = data.getStringArrayListExtra(Constants.CHECK_IN_DATE);
                    if (selectedDates != null && selectedDates.size() > 0) {
                        updateWeddingDatePickerView(selectedDates);
                    }
                }

                break;

        }

    }

    private void clearWeddingDates() {
        weddingSpecificCalendar0 = null;
        weddingSpecificCalendar1 = null;
        weddingSpecificCalendar2 = null;
        selectedCalendar.clear();
        if (rbSpecificDates.isChecked()) {
            tv_selected_wedding_dates.setText("Select Event Date");
        } else {
            tv_selected_wedding_dates.setText("Select Flexible Event Date(s)");
        }
    }

    private void updateWeddingDatePickerView(List<String> selectedDatesString) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);

        selectedCalendar.clear();
        try {
            for (String date : selectedDatesString) {
                Date checkinDate = sdf.parse(date);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(checkinDate);
                selectedCalendar.add(calendar);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        weddingSpecificCalendar0 = Calendar.getInstance();
        weddingSpecificCalendar0.set(Calendar.HOUR_OF_DAY, 0);
        weddingSpecificCalendar0.set(Calendar.MINUTE, 0);
        weddingSpecificCalendar0.set(Calendar.SECOND, 0);
        weddingSpecificCalendar0.set(Calendar.MILLISECOND, 0);
        Calendar calendar0 = selectedCalendar.get(0);
        weddingSpecificCalendar0.set(calendar0.get(Calendar.YEAR), calendar0.get(Calendar.MONTH), calendar0.get(Calendar.DAY_OF_MONTH));
        tv_selected_wedding_dates.setText("");
        setSpecificWeddingDate(calendar0.get(Calendar.YEAR), calendar0.get(Calendar.MONTH), calendar0.get(Calendar.DAY_OF_MONTH));

        if (selectedCalendar.size() > 1) {
            if (selectedCalendar.get(1) != null) {
                weddingSpecificCalendar1 = Calendar.getInstance();
                weddingSpecificCalendar1.set(Calendar.HOUR_OF_DAY, 0);
                weddingSpecificCalendar1.set(Calendar.MINUTE, 0);
                weddingSpecificCalendar1.set(Calendar.SECOND, 0);
                weddingSpecificCalendar1.set(Calendar.MILLISECOND, 0);
                Calendar calendar1 = selectedCalendar.get(1);
                weddingSpecificCalendar1.set(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
                setSpecificWeddingDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));

            } else {
                weddingSpecificCalendar1 = null;
            }
        } else {
            weddingSpecificCalendar1 = null;
        }

        if (selectedCalendar.size() > 2) {
            if (selectedCalendar.get(2) != null) {
                weddingSpecificCalendar2 = Calendar.getInstance();
                weddingSpecificCalendar2.set(Calendar.HOUR_OF_DAY, 0);
                weddingSpecificCalendar2.set(Calendar.MINUTE, 0);
                weddingSpecificCalendar2.set(Calendar.SECOND, 0);
                weddingSpecificCalendar2.set(Calendar.MILLISECOND, 0);
                Calendar calendar2 = selectedCalendar.get(2);
                weddingSpecificCalendar2.set(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));
                setSpecificWeddingDate(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH));

            } else {
                weddingSpecificCalendar2 = null;
            }
        } else {
            weddingSpecificCalendar2 = null;
        }

    }

    private void updateDatePickerViews(String checkInDateString, String checkOutDateString) {
        Calendar checkInDate = Calendar.getInstance();
        Calendar checkOutDate = Calendar.getInstance();


        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        try {
            Date startDate = sdf.parse(checkInDateString);
            checkInDate.setTime(startDate);

            Date endDate = sdf.parse(checkOutDateString);
            checkOutDate.setTime(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error selecting dates", Toast.LENGTH_LONG).show();
            return;
        }

        //create calendar for current time
        Calendar currentCal = Calendar.getInstance();
        currentCal.set(Calendar.HOUR_OF_DAY, 0);
        currentCal.set(Calendar.MINUTE, 0);
        currentCal.set(Calendar.SECOND, 0);
        currentCal.set(Calendar.MILLISECOND, 0);

        //setCheckInDate
        checkInDate.set(Calendar.HOUR_OF_DAY, 0);
        checkInDate.set(Calendar.MINUTE, 0);
        checkInDate.set(Calendar.SECOND, 0);
        checkInDate.set(Calendar.MILLISECOND, 0);

        if (checkInDate.getTimeInMillis() >= currentCal.getTimeInMillis()) { //checkInDate should be after current time
            nightlyStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
            nightlyStartCalendar.set(Calendar.MINUTE, 0);
            nightlyStartCalendar.set(Calendar.SECOND, 0);
            nightlyStartCalendar.set(Calendar.MILLISECOND, 0);

            int checkinYear = checkInDate.get(Calendar.YEAR);
            int checkInMonth = checkInDate.get(Calendar.MONTH);
            int checkInDay = checkInDate.get(Calendar.DAY_OF_MONTH);

            nightlyStartCalendar.set(checkinYear, checkInMonth, checkInDay);
            setDate(checkInDay, checkInMonth, checkinYear, chkin_txt_day, chkin_txt_month, chkin_txt_year);
        } else {
            Toast.makeText(getApplicationContext(), "Please select valid date", Toast.LENGTH_SHORT).show();
            return;
        }

        //setCheckoutDate
        checkOutDate.set(Calendar.HOUR_OF_DAY, 0);
        checkOutDate.set(Calendar.MINUTE, 0);
        checkOutDate.set(Calendar.SECOND, 0);
        checkOutDate.set(Calendar.MILLISECOND, 0);

        if (checkOutDate.getTimeInMillis() >= currentCal.getTimeInMillis()) { //checkOutDate should be after current time
            if (checkOutDate.getTimeInMillis() > nightlyStartCalendar.getTimeInMillis()) {
                int checkOutYear = checkOutDate.get(Calendar.YEAR);
                int checkOutMonth = checkOutDate.get(Calendar.MONTH);
                int checkOutDay = checkOutDate.get(Calendar.DAY_OF_MONTH);
                nightlyEndCalendar.set(checkOutYear, checkOutMonth, checkOutDay);
                setDate(checkOutDay, checkOutMonth, checkOutYear, chkout_txt_day, chkout_txt_month, chkout_txt_year);
            } else {
                Toast.makeText(getApplicationContext(), "CheckOut date should be greater than CheckIn Date!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Date Already Expired!", Toast.LENGTH_SHORT).show();
        }

    }

    private void getamenities() {

        latitude = Double.valueOf(str_latitude);
        longitude = Double.valueOf(str_longitude);
        para = new HashMap<String, String>();
        para.put("latlng", latitude + "," + longitude);
        para.put("sensor", String.valueOf(false));
        Log.e("locPara", para.toString());
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), asyncTaskListener, URL, para, 1, "location");
        httpAsync.execute();
    }

    private void ShowSnackBar() {
        if (progressDialog1 != null) {
            progressDialog1.dismiss();
        } else {

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


            snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
    }

   /* public int getDays() throws ParseException {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        Date dateout = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(chkout_txt_day.getText().toString());
        Date datein = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(chkin_txt_day.getText().toString());

        Calendar calout = Calendar.getInstance();
        calout.setTime(dateout);
        Calendar calin = Calendar.getInstance();
        calin.setTime(datein);

        cal2.set(Integer.parseInt(chkin_txt_year.getText().toString()), calin.get(Calendar.MONTH), Integer.parseInt(chkin_txt_day.getText().toString()));
        cal1.set(Integer.parseInt(chkout_txt_year.getText().toString()), calout.get(Calendar.MONTH), Integer.parseInt(chkout_txt_day.getText().toString()));
       // Log.v("YEAR", "YEAR in--" + checkinyear.getText().toString() + calin.get(Calendar.MONTH) + checkinday.getText().toString());
        //Log.v("YEAR", "YEAR out--" + checkoutyear.getText().toString() + calout.get(Calendar.MONTH) + checkoutday.getText().toString());

        return (int) TimeUnit.MILLISECONDS.toDays(Math.abs(nightlyStartCalendar.getTimeInMillis() - nightlyEndCalendar.getTimeInMillis()));

    }*/

    private void checkConnection() {

        if (rg_booking_type.getCheckedRadioButtonId() == R.id.rb_wedding) {
            doWeddingPost();
            return;
        }


        int hours = (int) TimeUnit.MILLISECONDS.toHours(Math.abs(hourlyEndCalendar.getTimeInMillis() - hourlyStartCalendar.getTimeInMillis()));
        //Toast.makeText(getApplicationContext(), "hous"+hours, Toast.LENGTH_SHORT).show();
        /*Log.v("date","STAFG---"+hourlyStartCalendar.get(Calendar.DAY_OF_MONTH)+"--"+hourlyStartCalendar.get(Calendar.MONTH)+"--"+hourlyStartCalendar.get(Calendar.YEAR)
                +"--"+hourlyStartCalendar.get(Calendar.HOUR_OF_DAY)+"--"+hourlyStartCalendar.get(Calendar.MINUTE));
        Log.v("date","ENDT---"+hourlyEndCalendar.get(Calendar.DAY_OF_MONTH)+"--"+hourlyEndCalendar.get(Calendar.MONTH)+"--"+hourlyEndCalendar.get(Calendar.YEAR)
                +"--"+hourlyEndCalendar.get(Calendar.HOUR_OF_DAY)+"--"+hourlyEndCalendar.get(Calendar.MINUTE));
        Log.v("date","ENDT---"+(hourlyEndCalendar.getTimeInMillis()<hourlyStartCalendar.getTimeInMillis())+"--"+hourlyEndCalendar.getTimeInMillis()+" ---"+hourlyStartCalendar.getTimeInMillis());
*/


        if (isNightlyBasis) {
            nightlyStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
            nightlyStartCalendar.set(Calendar.MINUTE, 0);
            nightlyStartCalendar.set(Calendar.SECOND, 0);
            nightlyStartCalendar.set(Calendar.MILLISECOND, 0);
            nightlyEndCalendar.set(Calendar.HOUR_OF_DAY, 0);
            nightlyEndCalendar.set(Calendar.MINUTE, 0);
            nightlyEndCalendar.set(Calendar.SECOND, 0);
            nightlyEndCalendar.set(Calendar.MILLISECOND, 0);

            if ((auto_city.getText().toString().equalsIgnoreCase(""))) {
                Toast.makeText(getApplicationContext(), "Please choose the city", Toast.LENGTH_SHORT).show();
                return;
            } else if (auto_area.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Please choose the area", Toast.LENGTH_SHORT).show();
                return;
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.rad_long_stay && nightlyEndCalendar.getTimeInMillis() <= nightlyStartCalendar.getTimeInMillis()) {
                Toast.makeText(getApplicationContext(), "Please select a different checkout date", Toast.LENGTH_SHORT).show();
                return;
            } else if (hotel_info == null) {
                Toast.makeText(getApplicationContext(), "Please select hotel class", Toast.LENGTH_SHORT).show();
                return;
            } else if (room_info == null) {
                Toast.makeText(getApplicationContext(), "Please select room", Toast.LENGTH_SHORT).show();
                return;
            } else if (grpBookingCb.isChecked()) {

                if (TextUtils.isEmpty(grpBookingBudget.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please enter Group Booking budget", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidText(grpBookingSpecialRequests.getText().toString())) {
                    grpBookingSpecialRequests.setError("Please remove emails or phone numbers");
                    return;
                }
            }
            int totalNights = 1;
            try {
                //  Log.v("date","START-"+nightlyStartCalendar.get(Calendar.DAY_OF_MONTH)+"--"+nightlyStartCalendar.get(Calendar.MONTH)+"--"+nightlyStartCalendar.get(Calendar.YEAR));
                //Log.v("date","END-"+nightlyEndCalendar.get(Calendar.DAY_OF_MONTH)+"--"+nightlyEndCalendar.get(Calendar.MONTH)+"--"+nightlyEndCalendar.get(Calendar.YEAR));
                totalNights = (int) TimeUnit.MILLISECONDS.toDays(Math.abs(nightlyEndCalendar.getTimeInMillis() - nightlyStartCalendar.getTimeInMillis()));
            } catch (Exception e) {
                e.printStackTrace();
                Common.logException(e);
            }

           /* try {
                total_price_long = Double.parseDouble(price.getText().toString()) * Double.parseDouble(user_rooms.getText().toString()) * totalNights;
            } catch (Exception e) {
                Toast.makeText(UserBooking.this, "Check all the fields", Toast.LENGTH_LONG).show();
                return;
            }*/
            model.confirmationBean = new ConfirmationBean(user_adults.getText().toString(),
                    user_rooms.getText().toString(),
                    String.valueOf(totalNights),
                    room_info,
                    hoteltype_text.getText().toString());


            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                fragmentTransaction.remove(prev);
            }
            fragmentTransaction.addToBackStack(null);
            PostResquestConfirmationDialog dialogFragment = new PostResquestConfirmationDialog();
            dialogFragment.show(fragmentTransaction, "dialog");
            dialogFragment.setListener(new PostResquestConfirmationDialog.ActivityCallback() {
                @Override
                public void proceedClicked() {
                    if (Common.isNetworkAvailable(getApplicationContext())) {
                        doPost();
                    } else {
                        ShowSnackBar();
                    }
                }
            });


        } else {
            if (Common.isNetworkAvailable(getApplicationContext())) {

                if ((radioGroup.getCheckedRadioButtonId() == R.id.rad_short_stay) && ((int) TimeUnit.MILLISECONDS.toHours(Math.abs(hourlyEndCalendar.getTimeInMillis() - hourlyStartCalendar.getTimeInMillis()))) >= 1
                        && (hourlyEndCalendar.getTimeInMillis() > hourlyStartCalendar.getTimeInMillis())) {
                    doPost();

                } else {
                    Toast.makeText(getApplicationContext(), "Time period should be atleast more than 1 hour", Toast.LENGTH_SHORT).show();

                }
            } else {
                ShowSnackBar();
            }
        }


    }

    private void checkStartConnection() {
        if (Common.isNetworkAvailable(getApplicationContext())) {

            layout_noconn.setVisibility(View.GONE);
            main_layout.setVisibility(View.VISIBLE);
            getCount();
            getUsername();
        } else {

            main_layout.setVisibility(View.GONE);

            layout_noconn.setVisibility(View.VISIBLE);

            btn_noconn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkStartConnection();
                }
            });

        }
    }

    private void getUsername() {
        prof_parameter = new HashMap<>();
        prof_parameter.put("auth_token", auth_token);
        Log.e("para_scode", prof_parameter.toString());
        HttpAsync httpAsync = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + Constants.GET_PROFILE, prof_parameter, 2, "profile_data");
        httpAsync.execute();
    }

    private void getCount() {
        parameters = new HashMap<>();
        parameters.put("auth_token", auth_token);
        parameters.put("device_type", "ANDROID");
        new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + "user_requests/buyer_dashboard_count/get.json?", parameters, 1, "counts").execute();
    }


    private void doPost() {

        if (radioGroup.getCheckedRadioButtonId() == R.id.rad_short_stay) {

            if (hotel_info != null) {
                if (room_info != null) {

                    if (auto_city.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please choose the city", Toast.LENGTH_SHORT).show();
                    } else if (auto_area.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please choose the area", Toast.LENGTH_SHORT).show();
                    } else {
                        para1 = new HashMap<>();
                        para1.put("user_request[is_short_term]", "1");
                        para1.put("user_request[no_hrs]", "" + ((int) TimeUnit.MILLISECONDS.toHours(Math.abs(hourlyEndCalendar.getTimeInMillis() - hourlyStartCalendar.getTimeInMillis()))));
                        para1.put("categ_id", "95");
                        para1.put("user_request[buysell_category_id]", hotel_id);
                        para1.put("user_request[city]", auto_city.getText().toString());
                        para1.put("user_request[locality]", selectedArea);
                        para1.put("user_request[item_info]", (hotel_info + " hotel near by " + selectedArea + "," + selectedCityName));
                        para1.put("user_request[room_type_id]", room_id);

                        //group booking
                        if (grpBookingCb.isChecked()) {
                            String specialRequestText = grpBookingSpecialRequests.getText().toString();
                            para1.put("is_group_booking", "GRPBOOKING");
                            para1.put("user_request[booking_note]", specialRequestText);
                            para1.put("user_request[price]", grpBookingBudget.getText().toString());
                            para1.put("user_request[grand_total]", totalGroupBookingPrice);
                        }

                        Log.e("Date", "hourlyCheckin-" + hourlyStartCalendar.get(Calendar.MONTH) + "-" + chkin_txt_day1.getText().toString() + "-" + chkin_txt_year1.getText().toString() + " " + txt_start_time.getText().toString() + ":00");
                        Log.e("Date", "hourlyCheckOUT-" + hourlyEndCalendar.get(Calendar.MONTH) + "-" + hourlyEndCalendar.get(Calendar.DAY_OF_MONTH) + "-" + hourlyEndCalendar.get(Calendar.YEAR) + " " + txt_end_time.getText().toString() + ":00");
                        SimpleDateFormat serverTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

                        para1.put("checkin_date_hourly", serverTimeFormat.format(hourlyStartCalendar.getTime()));
                        para1.put("checkout_date_hourly", serverTimeFormat.format(hourlyEndCalendar.getTime()));

                        para1.put("user_request[no_of_rooms]", user_rooms.getText().toString());
                        para1.put("user_request[adult]", user_adults.getText().toString());
                        para1.put("user_request[children]", user_childrens.getText().toString());

                        para1.put("user_request[descision_factor]", "lowest price");
                        para1.put("user_request[city_latilong]", latitude + "," + longitude);
                        para1.put("NBlatitude", latitude.toString());
                        para1.put("NBlongitude", longitude.toString());
                        para1.put("user_request[currency_code]", currencyCode);
                        para1.put("amenity_ids[]", checkedIdList.toString().trim().replace("[", "").replace("]", "").replace(" ", ""));
                        para1.put("term_agreement_decision", "on");
                        para1.put("auth_token", auth_token);

                        Log.e("parameters:", para1.toString());
                        progressDialog.show();
                        if (ssp.getPRE_user_login()) {
                            HttpAsync user_values = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + Constants.POST, para1, 2, "user_values");
                            user_values.execute();

                        } else {
                            progressDialog.dismiss();
                            Intent myint = new Intent(UserBooking.this, LoginSignupActivity.class);
                            myint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("hashMap", para1);
                            bundle.putBoolean("bargainNowButton", true);
                            bundle.putString("type", user_type);
                            myint.putExtras(bundle);

                            checkedIdList = null;
                            subcheckedIdList = null;

                            checkedIdList = new ArrayList<String>();
                            subcheckedIdList = new ArrayList<String>();
                            startActivity(myint);
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Room", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please select hotel class", Toast.LENGTH_LONG).show();
            }

        } else if (radioGroup.getCheckedRadioButtonId() == R.id.rad_long_stay) {

            if (hotel_info != null) {
                if (room_info != null) {

                    if ((auto_city.getText().toString().equalsIgnoreCase(""))) {
                        Toast.makeText(getApplicationContext(), "Please choose the city", Toast.LENGTH_SHORT).show();
                    } else if (auto_area.getText().toString().equalsIgnoreCase("")) {
                        Toast.makeText(getApplicationContext(), "Please choose the area", Toast.LENGTH_SHORT).show();
                    } else {

                        para1 = new HashMap<>();
                        para1.put("user_request[is_short_term]", "0");
                        para1.put("categ_id", "95");
                        para1.put("user_request[buysell_category_id]", hotel_id);
                        para1.put("user_request[city]", auto_city.getText().toString());
                        para1.put("user_request[locality]", selectedArea);
                        para1.put("user_request[item_info]", (hotel_info + " hotel near by " + selectedArea + "," + selectedCityName));
                        para1.put("user_request[room_type_id]", room_id);

                        SimpleDateFormat serverTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
                        para1.put("checkin_date", serverTimeFormat.format(nightlyStartCalendar.getTime()));
                        para1.put("checkout_date", serverTimeFormat.format(nightlyEndCalendar.getTime()));
                        para1.put("user_request[no_of_rooms]", user_rooms.getText().toString());
                        para1.put("user_request[adult]", user_adults.getText().toString());
                        para1.put("user_request[children]", user_childrens.getText().toString());
                        para1.put("user_request[descision_factor]", "lowest price");

                        //group booking
                        if (grpBookingCb.isChecked()) {
                            String specialRequestText = grpBookingSpecialRequests.getText().toString();
                            para1.put("is_group_booking", "GRPBOOKING");
                            para1.put("user_request[booking_note]", specialRequestText);
                            para1.put("user_request[price]", grpBookingBudget.getText().toString());
                            para1.put("user_request[grand_total]", totalGroupBookingPrice);
                        }

                        if (req_latitude == null) {
                            req_latitude = latitude;
                            req_longitude = longitude;
                        }

                        para1.put("user_request[city_latilong]", req_latitude + "," + req_longitude);
                        para1.put("NBlatitude", req_latitude.toString());
                        para1.put("NBlongitude", req_longitude.toString());
                        para1.put("user_request[currency_code]", currencyCode);
                        para1.put("amenity_ids[]", checkedIdList.toString().trim().replace("[", "").replace("]", "").replace(" ", ""));
                        para1.put("term_agreement_decision", "on");
                        para1.put("auth_token", auth_token);
                        Log.e("parameters:", para1.toString());
                        progressDialog.show();
                        if (ssp.getPRE_user_login()) {
                            HttpAsync user_values = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + Constants.POST, para1, 2, "user_values");
                            user_values.execute();

                        } else {
                            progressDialog.dismiss();
                            Intent myint = new Intent(UserBooking.this,
                                    LoginSignupActivity.class);
                            myint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("hashMap", para1);
                            bundle.putBoolean("bargainNowButton", true);
                            bundle.putString("type", user_type);
                            myint.putExtras(bundle);
                            checkedIdList = null;
                            subcheckedIdList = null;
                            checkedIdList = new ArrayList<String>();
                            subcheckedIdList = new ArrayList<String>();
                            startActivity(myint);
                        }

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Room", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please select hotel class", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void doWeddingPost() {

        int guestCount = 0;
        double venueBudget = 0.0, serviceBudget = 0.0;
        String guests = edtGuestCount.getText().toString();
        try {
            guestCount = Integer.parseInt(guests);
            venueBudget = Double.parseDouble(edtBudgetVenue.getText().toString());
            serviceBudget = Double.parseDouble(edtBudgetServices.getText().toString());
        } catch (Exception e) {

        }


        if ((auto_city.getText().toString().equalsIgnoreCase(""))) {
            Toast.makeText(this, "Please choose the city", Toast.LENGTH_SHORT).show();
        } else if (selectedWeddingVenue == null) {
            Toast.makeText(this, "Please select wedding venue", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtBudgetVenue.getText().toString())) {
            Toast.makeText(this, "Please enter Venue budget", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(edtGuestCount.getText().toString())) {
            Toast.makeText(this, "Please enter Guest/Attendees", Toast.LENGTH_SHORT).show();
        } else if (guestCount < 25) {
            Toast.makeText(this, "Minimum of 25 Guests needed", Toast.LENGTH_SHORT).show();
        } else if (weddingSpecificCalendar0 == null) {
            Toast.makeText(this, "Please Select Date", Toast.LENGTH_SHORT).show();
        } else if (venueBudget < 25000.0) {
            Toast.makeText(this, "Budget for Venue cannot be less than ₹25,000", Toast.LENGTH_SHORT).show();
        } else if (serviceBudget < 10000.0) {
            Toast.makeText(this, "Budget for services cannot be less than ₹10,000", Toast.LENGTH_SHORT).show();
        } else {
            para1 = new HashMap<>();
            para1.put("user_request[is_short_term]", "0");
            para1.put("categ_id", String.valueOf(selectedWeddingVenue.getParent_id()));
            para1.put("user_request[buysell_category_id]", String.valueOf(selectedWeddingVenue.getId()));

            para1.put("user_request[city]", auto_city.getText().toString());
            para1.put("user_request[locality]", selectedArea);


            SimpleDateFormat serverTimeFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
            para1.put("checkin_date", serverTimeFormat.format(weddingSpecificCalendar0.getTime()));
            para1.put("checkout_date", serverTimeFormat.format(weddingSpecificCalendar0.getTime()));

            if (rbFlexibleDates.isChecked()) {
                if (weddingSpecificCalendar1 != null) {
                    para1.put("checkin_date1", serverTimeFormat.format(weddingSpecificCalendar1.getTime()));
                    para1.put("checkout_date1", serverTimeFormat.format(weddingSpecificCalendar1.getTime()));
                }
                if (weddingSpecificCalendar2 != null) {
                    para1.put("checkin_date2", serverTimeFormat.format(weddingSpecificCalendar2.getTime()));
                    para1.put("checkout_date2", serverTimeFormat.format(weddingSpecificCalendar2.getTime()));
                }
            }

            para1.put("user_request[adult]", edtGuestCount.getText().toString());
            para1.put("user_request[children]", "0");
            para1.put("user_request[no_of_rooms]", "0");
            //group booking

            para1.put("is_group_booking", "GRPBOOKING");
            para1.put("user_request[price]", edtBudgetVenue.getText().toString());
            para1.put("user_request[grand_total]", edtBudgetVenue.getText().toString());
            para1.put("user_request[budget_extra_service]", edtBudgetServices.getText().toString());

            if (req_latitude == null) {
                req_latitude = latitude;
                req_longitude = longitude;
            }

            para1.put("user_request[city_latilong]", req_latitude + "," + req_longitude);
            para1.put("NBlatitude", req_latitude.toString());
            para1.put("NBlongitude", req_longitude.toString());
            para1.put("user_request[currency_code]", currencyCode);
            para1.put("amenity_ids[]", weddingServicesIdList.toString().trim().replace("[", "").replace("]", "").replace(" ", ""));
            para1.put("term_agreement_decision", "on");
            para1.put("is_wedding_exhibition", "true");
            para1.put("auth_token", auth_token);


            Log.e("parameters:", para1.toString());
            progressDialog.show();

            if (ssp.getPRE_user_login()) {
                HttpAsync user_values = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + Constants.POST, para1, 2, "user_values");
                user_values.execute();
            } else {
                progressDialog.dismiss();
                Intent myint = new Intent(UserBooking.this,
                        LoginSignupActivity.class);
                myint.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hashMap", para1);
                bundle.putBoolean("bargainNowButton", true);
                bundle.putString("type", user_type);
                myint.putExtras(bundle);

                selectedWeddingServiceIdList = null;
                selectedWeddingVenueIdList = null;

                weddingVenueIdList = new ArrayList<>();
                weddingServicesIdList = new ArrayList<>();
                startActivity(myint);
            }
        }

    }


    private boolean isValidText(String specialRequestText) {
        String specialChars = "[@+.*\\.*]";
        String possiblePhone = "\\d{6,}";


        Pattern p1 = Pattern.compile(specialChars);
        Matcher m1 = p1.matcher(specialRequestText);

        if (m1.find()) {
            return false;
        }

        Pattern p2 = Pattern.compile(possiblePhone);
        Matcher m2 = p2.matcher(specialRequestText);

        return !m2.find();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!ssp.getPRE_user_login()) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        } else {
            getMenuInflater().inflate(R.menu.notify, menu);
            MenuItem menuItem = menu.findItem(R.id.notification);
            View actionView = menuItem.getActionView();
            actionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserBooking.this, NotificationListActivity.class);
                    startActivityForResult(intent, REFRESH_NOTIFICATION_COUNT);
                }
            });
            tvNotificationCount = (TextView) actionView.findViewById(R.id.count);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        setPushCount();

        return super.onPrepareOptionsMenu(menu);
    }

    private void setPushCount() {
        if (tvNotificationCount != null) {
            if (unread_push_counts > 0) {
                tvNotificationCount.setVisibility(View.VISIBLE);
                tvNotificationCount.setText(String.valueOf(unread_push_counts));
            } else {
                tvNotificationCount.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.notification:
                Intent intent = new Intent(this, NotificationListActivity.class);
                startActivityForResult(intent, REFRESH_NOTIFICATION_COUNT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        drawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.log_in:
                Intent log = new Intent(UserBooking.this, LoginSignupActivity.class);
                log.putExtra("type", user_type);
                log.putExtra("bargainNowButton", false);
                log.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                log.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                log.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(log);
                break;

            case R.id.request_to_offer:
                break;

            case R.id.my_offers:
//                startActivity(new Intent(UserBooking.this, UserLiveRequests.class));
                Intent user_confirm1 = new Intent(UserBooking.this, UserLiveRequests.class);
                user_confirm1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user_confirm1);
                break;

            case R.id.confirm_booking:
                Intent user_confirm = new Intent(UserBooking.this, UserConfirmBooking.class);
                user_confirm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user_confirm);
                break;

            case R.id.cancel_request:
//                startActivity(new Intent(UserBooking.this, UserCancelRequest.class));
                Intent user_confirm2 = new Intent(UserBooking.this, UserCancelRequest.class);
                user_confirm2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(user_confirm2);
                break;

            case R.id.profile:
                Intent prof = new Intent(UserBooking.this, UserProfile.class);
                prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof);
                break;

            case R.id.messages:
//                startActivity(new Intent(UserBooking.this, UserNotifications.class));
                Intent prof1 = new Intent(UserBooking.this, UserNotifications.class);
                prof1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof1);
                break;

            case R.id.send_feedback:
//                startActivity(new Intent(UserBooking.this, UserFeedback.class));
                Intent prof2 = new Intent(UserBooking.this, UserFeedback.class);
                prof2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof2);
                break;

//            case R.id.rate_us:
//                startActivity(new Intent(UserBooking.this, UserRateUs.class));
//                break;

            case R.id.terms_and_conditions:
//                startActivity(new Intent(UserBooking.this, UserTermsandConditions.class));
                Intent prof4 = new Intent(UserBooking.this, UserTermsandConditions.class);
                prof4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof4);
                break;


            case R.id.contact_us:
//                startActivity(new Intent(UserBooking.this, UserContactUs.class));
                Intent prof6 = new Intent(UserBooking.this, UserContactUs.class);
                prof6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(prof6);
                break;

            case R.id.log_out:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                checkConnectionforLogout();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


        }
        return false;
    }

    private void checkConnectionforLogout() {

        if (Common.isNetworkAvailable(getApplicationContext())) {
            disableGcmMessage();
            progressLoadingDialog = new Progressloadingdialog(UserBooking.this, "Logging out..");
            progressLoadingDialog.setCancelable(false);
            progressLoadingDialog.show();

            paraHashMap = new HashMap<>();
            paraHashMap.put("auth_token", auth_token);
            HttpAsync pass = new HttpAsync(getApplicationContext(), asyncTaskListener, Constants.BASE_URL + "my_deals/logout/signout.json?", paraHashMap, 2, "logout");
            pass.execute();
        } else {
            String styledText = "<font color='black'>No Internet Connection</font>.";
            // msnackBar;
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), Html.fromHtml(styledText), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkConnectionforLogout();
                        }
                    })
                    .setActionTextColor(Color.RED);
            View snackBarView = snackbar.getView();


            snackBarView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            snackbar.show();
        }

    }

    private void disableGcmMessage() {
        para2 = new HashMap<>();
        para2.put("uid", ssp.getPRE_USER_PROFILE_id());
        Log.e("para_register", para2.toString());

        HttpAsync httpAsync = new HttpAsync(UserBooking.this, new AsyncTaskListener() {

            @Override
            public void onTaskCancelled(String data) {
                Toast.makeText(UserBooking.this, getResources().getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTaskComplete(String result, String tag) {
//                Log.e("response", result);
                if (result.equalsIgnoreCase(Constants.FAIL)) {
                    Toast.makeText(UserBooking.this, "internet is not available", Toast.LENGTH_SHORT).show();
                } else {
                    parseJsonOfDeleteMessage(result);
                }

            }
        }, Constants.BASE_URL + Constants.GCM_DELETE, para2, 2, null);
        //Constants.BASE_URL + Constants.PURCHASED_DEALS_URL
        httpAsync.execute();
    }

    private void parseJsonOfDeleteMessage(String result) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            if (jsonObject != null) {
                boolean boo = jsonObject.getBoolean("success");
//                Log.e("boolean", boo + "");
                if (boo) {
//                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    ssp.setPRE_GCM_MSG_d_status(true);
                } else {
                    Toast.makeText(getApplicationContext(), jsonObject.getString("msg") + "", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Common.logException(e);
        }
    }


    @Override
    public void onTaskCancelled(String data) {

    }

    @Override
    public void onTaskComplete(String result, String tag) {

        boolean status = true;
        String resultMsg = "";

        JSONObject resultJson = null;
        try {
            resultJson = new JSONObject(result);
            status = resultJson.optBoolean("success");
            resultMsg = resultJson.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (!status && "Invalid Email or Password.".equalsIgnoreCase(resultMsg)) {
            performLogout();
            return;
        }


        //Log.d(TAG, "onTaskComplete " + result + "  ----   " + tag);
        if (result.equalsIgnoreCase("fail")) {

            ShowSnackBar();
            if ("user_values".equals(tag)) {
                progressDialog.dismiss();
            }

        } else if (tag.equalsIgnoreCase("location")) {
            //Log.e("resul", result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {

                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        JSONArray jarry = jsonObject2.getJSONArray("address_components");
                        if (jarry.length() > 0) {
                            for (int i = 0; i < jarry.length(); i++) {
                                JSONObject jsonObject3 = jarry.getJSONObject(i);
                                JSONArray jarry1 = jsonObject3.getJSONArray("types");
                                if (jarry1.length() > 0) {
                                    for (int j = 0; j < jarry1.length(); j++) {
                                        String str = jarry1.getString(j);
                                        if (str.equalsIgnoreCase("locality")) {
                                            loc = jsonObject3.getString("long_name");
                                        }
                                        if (str.equalsIgnoreCase("administrative_area_level_1")) {
                                            state = jsonObject3.getString("long_name");
                                        }
                                        if (str.equalsIgnoreCase("country")) {
                                            country = jsonObject3.getString("long_name");
                                        }
                                    }
                                }
                            }
                        }
                        urarea = jsonObject2.getString("formatted_address");
                        urcity = loc + "," + state + "," + country;
                        auto_city.setText(urarea);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }

        } else if (tag.equalsIgnoreCase("profile_data")) {

            try {
                JSONObject job = new JSONObject(result);

                if (job != null) {

                    JSONObject jo_new = job.getJSONObject("user");
                    if (jo_new != null) {

                        display_name = jo_new.getString("display_name");
                        if (ssp.getPRE_USER_PROFILE_displayname().equalsIgnoreCase("")) {
                            ssp.setPRE_USER_PROFILE_displayname(display_name);
                        }
                        email = jo_new.getString("email");
                        if (ssp.getPRE_USER_PROFILE_email().equalsIgnoreCase("")) {
                            ssp.setPRE_USER_PROFILE_email(email);
                        }
                        uid = jo_new.getString("id");
                        if (ssp.getPRE_USER_PROFILE_id().equalsIgnoreCase("")) {
                            ssp.setPRE_USER_PROFILE_id(uid);
                        }
                        if (navUserName != null) {
                            navUserName.setText(ssp.getPRE_USER_PROFILE_displayname());
                            if (ssp.getPRE_USER_PROFILE_displayname().length() > 1) {
                                String str_name_prefix = ssp.getPRE_USER_PROFILE_displayname().substring(0, 1);
                                name_prefix.setText(str_name_prefix.toUpperCase());
                            }
                        }

                        initGcmWork();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), job.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("counts")) {
            try {
                JSONObject jsoncount = new JSONObject(result);
                lowest_offers = Integer.valueOf(jsoncount.getString("lowest_offers"));
                messages = Integer.valueOf(jsoncount.getString("unread_messages"));
                confirmed_bookings = Integer.valueOf(jsoncount.getString("confirm_bookings"));
                cancelled_counts = Integer.valueOf(jsoncount.getString("cancelled_bookings"));
                unread_push_counts = Integer.valueOf(jsoncount.getString("unread_push_notifications"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            setPushCount();

            if (auth_token != null) {

                nv.getMenu().findItem(R.id.my_offers).setVisible(true).setTitle("Bargained Offers(" + String.valueOf(lowest_offers) + ")");
                if (messages == 0) {
                    nv.getMenu().findItem(R.id.messages).setVisible(false);

                } else {

                    nv.getMenu().findItem(R.id.messages).setVisible(true).setTitle("Messages(" + String.valueOf(messages) + ")");
                }
                if (confirmed_bookings == 0) {
                    nv.getMenu().findItem(R.id.confirm_booking).setVisible(false);
                } else {
                    nv.getMenu().findItem(R.id.confirm_booking).setVisible(true).setTitle("Booking Confirmations(" + String.valueOf(confirmed_bookings) + ")");
                }
                if (cancelled_counts == 0) {
                    nv.getMenu().findItem(R.id.cancel_request).setVisible(false);
                } else {
                    nv.getMenu().findItem(R.id.cancel_request).setVisible(true).setTitle("Cancellations (" + String.valueOf(cancelled_counts) + ")");
                }

            }

        } else if (tag.equalsIgnoreCase("autocomplete")) {
            try {
                JSONObject job1 = new JSONObject(result);
                if (job1 != null) {
                    JSONArray jarry = job1.getJSONArray("predictions");

                    cities = new ArrayList();
                    cityList = new ArrayList<City>();
                    for (int i = 0; i < jarry.length(); i++) {
                        JSONObject jo = jarry.getJSONObject(i);
                        String cityPlaceId = jo.getString("place_id");
                        String cityName = jo.getString("description");
                        cities.add(cityName);
                        cityList.add(new City(cityName, cityPlaceId));
                    }
                    if (citySuggestionAdapter == null) {
                        citySuggestionAdapter = new SimpleDropDownAdapter(this, cities, false);
                        auto_city.setAdapter(citySuggestionAdapter);
                    } else {
                        citySuggestionAdapter.setData(cities);
                    }

                    citySuggestionAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Common.logException(e);
            }
        } else if (tag.equalsIgnoreCase("city_latitude")) {
            try {
                JSONObject job1 = new JSONObject(result);
                if (job1 != null) {
                    JSONObject job2 = job1.getJSONObject("result");

                    JSONArray jsonArray = job2.getJSONArray("address_components");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject zero2 = jsonArray.getJSONObject(i);
                        String long_name = zero2.getString("long_name");
                        String short_name = zero2.getString("short_name");
                        JSONArray mtypes = zero2.getJSONArray("types");
                        String Type = mtypes.getString(0);

                        if (Type.equalsIgnoreCase("country")) {
                            Country = short_name;
                        }

                    }

                    para_curr = new HashMap<>();
                    para_curr.put("cn_code", Country);
                    Log.e("para_country", Country);
                    HttpAsync httpAsync = new HttpAsync(getApplicationContext(), asyncTaskListener, curr_url, para_curr, 1, "curr");
                    httpAsync.execute();

                    JSONObject jsonObject1 = job2.getJSONObject("geometry");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                    try {
                        latitude = Double.valueOf(jsonObject2.getString("lat"));
                        longitude = Double.valueOf(jsonObject2.getString("lng"));
                        city_place_id = job2.optString("place_id");
                        setSelectedCityData();
                    } catch (Exception e) {
                        latitude = 12.8989;
                        longitude = 77.7878;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("timeZonerequest")) {
            try {
                JSONObject job1 = new JSONObject(result);
                Log.e("latlng", "stru" + job1.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("area_search")) {
            try {
                JSONObject job1 = new JSONObject(result);
                JSONArray jarry = job1.getJSONArray("predictions");
                cities = new ArrayList();
                placeids = new ArrayList();

                String str_auto_city_name;
                if (auto_city.getText().toString().contains(",")) {
                    str_auto_city_name = auto_city.getText().toString().substring(0, auto_city.getText().toString().indexOf(","));
                } else {
                    str_auto_city_name = auto_city.getText().toString();
                }

                cities.add("Anywhere in " + str_auto_city_name);
                placeids.add(city_place_id);

                for (int i = 0; i < jarry.length(); i++) {
                    JSONObject jo = jarry.getJSONObject(i);

                    String str_area_name = jo.getString("description");
                    String alternate_city_name = "";

                    if ("Bangalore".equalsIgnoreCase(str_auto_city_name)) {
                        alternate_city_name = "Bengaluru";
                    }

                    if (str_area_name.contains(str_auto_city_name) || (!TextUtils.isEmpty(alternate_city_name) && str_area_name.contains(alternate_city_name))) {
                        cities.add(str_area_name.split(",")[0]);
                        placeids.add(jo.getString("place_id"));
                    }
                }
                if (areaSuggestionAdapter == null) {
                    areaSuggestionAdapter = new SimpleDropDownAdapter(this, cities, true);
                    auto_area.setAdapter(areaSuggestionAdapter);
                } else {
                    areaSuggestionAdapter.setData(cities);
                }

                areaSuggestionAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("latitude")) { // lat-lon for area
            try {
                JSONObject job1 = new JSONObject(result);
                if (job1 != null) {
                    JSONObject job2 = job1.getJSONObject("result");
                    JSONObject jsonObject1 = job2.getJSONObject("geometry");
                    JSONObject jsonObject2 = jsonObject1.getJSONObject("location");
                    req_latitude = Double.valueOf(jsonObject2.getString("lat"));
                    req_longitude = Double.valueOf(jsonObject2.getString("lng"));
                    SharedPreferences.Editor edt = locationPref.edit();
                    edt.putString("lat", String.valueOf(req_latitude));
                    edt.putString("long", String.valueOf(req_longitude));
                    edt.commit();
                    Log.e("latlng", String.valueOf(latitude) + "," + String.valueOf(longitude));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("curr")) {

            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {
                        try {
                            curr_code = job.optString("currency_code");
                            Log.e("curr_code", curr_code);
                            currencyCode = curr_code;
                            setCurrencySymbol();
                        } catch (Exception e) {
                            curr_code = "INR";
                            currencyCode = curr_code;
                            setCurrencySymbol();
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("user_values")) {
            progressDialog.dismiss();
            try {
                JSONObject job = new JSONObject(result);
                if (job != null) {
                    if (job.optBoolean("success", true)) {

                        ssp.setPRE_user_login(true);
                        ssp.setPRE_hotelier_login(false);
                        ssp.setPRE_usertype(job.optString("user_type"));
                        amenities_long = "";
                        amenities = "";
                        checkedIdList = null;
                        checkedIdList = new ArrayList<String>();
//                        SharedPreferences.Editor edt = mpref.edit();
//                        edt.putBoolean("status", true);
//                        edt.putString("type", job.optString("user_type"));
//                        edt.commit();
                        Toast.makeText(getApplicationContext(), getString(R.string.success_booking), Toast.LENGTH_LONG).show();
//                        startActivity(new Intent(UserBooking.this, UserLiveRequests.class));
                        Intent prof = new Intent(UserBooking.this, UserLiveRequests.class);
                        prof.putExtra("loader_msg", "Creating competition now...");
                        prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(prof);
                    } else {

                        Toast.makeText(getApplicationContext(), job.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("logout")) {
            progressLoadingDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    if (jsonObject.optBoolean("success", true)) {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        ssp.clearSharedPreference();
                        Intent prof = new Intent(UserBooking.this, MainActivity.class);
                        prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(prof);
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setSelectedCityData() {
        PlacesAutoCompleteAdapter placesAutoCompleteAdapter = (PlacesAutoCompleteAdapter) auto_area.getAdapter();
        placesAutoCompleteAdapter.setSelectedCityData(selectedCityName, latitude, longitude, city_place_id);
    }

    private void performLogout() {
        Toast.makeText(this, R.string.you_are_logged_out, Toast.LENGTH_LONG).show();
        if (ssp != null) {
            ssp.clearSharedPreference();
        }
        Intent prof = new Intent(UserBooking.this, MainActivity.class);
        prof.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(prof);
        finish();
    }


    private void setDate(int day, int month, int year, TextView textView, TextView textView1, TextView textView2) {
        Date d = new Date();
        d.setMonth(month);
        d.setDate(day);
        textView.setText(new SimpleDateFormat("dd").format(d));
        textView1.setText(new SimpleDateFormat("MMM").format(d));
        textView2.setText(String.valueOf(year));
    }

    private void setTime(String s, TextView txt_time) {
        String time = String.valueOf(s);
        txt_time.setText(time.substring(0,2)+" "+time.substring(time.length()-2,time.length()));
    }


//    private void chktime(String startTime, String endTime) {
//
//        Log.e(startTime, endTime);
////        int am = endTime.substring(6,7);
//        Time time1,time2;
//        DateFormat time = new SimpleDateFormat("h:mm a");
//        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
//        try{
//            time1 = .parse(startTime);
//            time2 = date.parse(endTime);
//        }catch (ParseException e){
//            e.printStackTrace();
//            return false;
//
//        }
//
//    }

    public boolean ckeckDate(String startDate, String endDate) {


        Log.e(startDate, endDate);
        int dash = endDate.indexOf("-");
        int dash2 = endDate.lastIndexOf("-");
        startdate = startDate.substring(dash + 1, dash2);
//        Log.e("gdbd", startdate);
        enddate = endDate.substring(dash + 1, dash2);
        Date date1, date2;
        DateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date1 = format1.parse(startDate);
            date2 = format1.parse(endDate);
            if (date2.after(date1) || date2.equals(date1)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            Common.ExitAppDialog(UserBooking.this);
        }
    }

    private void initGcmWork() {
        if (Build.VERSION.SDK_INT >= 23) {
            //Marshmallow
            marshmellowPermission();
        } else {
            // Pre-Marshmallow
            gcmWork();
        }
    }

    private void marshmellowPermission() {
        // Marshmallow+
        // Here, thisActivity is the current activity
        /*if (ContextCompat.checkSelfPermission(UserBooking.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UserBooking.this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_RECEIVE_SMS);
        } else {
            gcmWork();
        }*/

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            launchPermissionsSettings();
            finish();
        }*/
        switch (requestCode) {
            /*case MY_PERMISSIONS_RECEIVE_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Toast.makeText(MainActivity.this,"REQUEST_FINE if",Toast.LENGTH_SHORT).show();
                    gcmWork();
                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
//                    Toast.makeText(UserBooking.this, "first allow the receive permission", Toast.LENGTH_LONG).show();
                    // mInformationTextView.setText("first allow the receive permission");
                    //  mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                }
                return;
            }
*/
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // showingArea();
                } else {
                    Toast.makeText(getApplicationContext(), "Enable Location Permission!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
            // other 'case' lines to check for other
            // permissions this app might request

        }
    }

    private void launchPermissionsSettings() {
        Toast.makeText(getApplicationContext(), "Enable Permissions!", Toast.LENGTH_SHORT).show();
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);
    }

    private void gcmWork() {

        if (Common.isNetworkAvailable(UserBooking.this)) {

//            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                    //  mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
//                    SharedPreferences sharedPreferences =
//                            PreferenceManager.getDefaultSharedPreferences(context);
//                    boolean sentToken = sharedPreferences
//                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//                    if (sentToken) {
//                        // mInformationTextView.setText(getString(R.string.gcm_send_message));
//                    } else {
//                        //  mInformationTextView.setText(getString(R.string.token_error_message));
//                    }
//                }
//            };

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);

            }

        } else {

        }

    }


    private static final BroadcastReceiver mRegistrationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            boolean sentToken = sharedPreferences
                    .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (sentToken) {
                // mInformationTextView.setText(getString(R.string.gcm_send_message));
            } else {
                //  mInformationTextView.setText(getString(R.string.token_error_message));
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        setAmentities();
        drawer.closeDrawers();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        checkForAppUpdate();
    }

    @Override
    protected void onPause() {
        appUpdateManager.unregisterListener(installStateUpdatedListener);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void oncbClicked(int position, boolean checked) {
        if (checked) {
            subcheckedIdList.add(amenitiesbeanList.get(position).getId());
//            Log.e("checked", amenitiesbeanList.get(position).getId().toString());
        } else {
            subcheckedIdList.remove(amenitiesbeanList.get(position).getId());
//            Log.e("checked", amenitiesbeanList.get(position).getId().toString());
        }
    }

    @Override
    public void onWeddingServiceClicked(int position, boolean checked) {
        if (checked) {
            selectedWeddingServiceIdList.add(weddingServiceDataList.get(position).getId());
        } else {
            selectedWeddingServiceIdList.remove(weddingServiceDataList.get(position).getId());
        }
    }

    /*@Override
    public void onWeddingVenueClicked(int position, boolean checked) {
        if (checked) {
            selectedWeddingVenueIdList.add(weddingVenueDataList.get(position).getId());
        } else {
            selectedWeddingVenueIdList.remove(weddingVenueDataList.get(position).getId());
        }

        if (checked) {
            selectedWeddingVenue = weddingVenueDataList.get(position);
        } else {
            selectedWeddingVenue = null;
        }
    }*/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.e("UserBooking", "Google Places API connected.");
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(UserBooking.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_REQUEST_FINE_LOCATION);

    }

    @Override
    public void onConnectionSuspended(int i) {
        // mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e("UserBooking", "Google Places API connection suspended.");
    }


    public void updateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            startLocationUpdates();
        }
        if (mLocation != null) {
            try {
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
                geoCoder = new Geocoder(this, Locale.getDefault());
                addresses = geoCoder.getFromLocation(mLocation.getLatitude(), mLocation.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postal = addresses.get(0).getPostalCode();

                auto_city.setEnabled(false);
                auto_area.setEnabled(false);
                auto_city.setText(city + "," + state);
                auto_visible.setVisibility(View.VISIBLE);
                clear_butt_city.setVisibility(View.VISIBLE);

                auto_area.setText(addresses.get(0).getFeatureName());

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("UserBooking", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    private void getLocation() {


//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        try {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//        } catch (SecurityException ex) {
//            Log.e("error", "location error");
//        }
//
//
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//        }
        if (locationPref != null) {

//            latitude = Double.valueOf(str_latitude);
//            longitude = Double.valueOf(str_longitude);
            urcity = locationPref.getString("urcity", null);
            city = urcity;


        }


    }


    TimePickerDialog.OnTimeSetListener timeListnerCheckIn = (view, hourOfDay, minute) -> {
        Calendar selCal = Calendar.getInstance();
       // selCal.setTime(hourlyStartCalendar.getTime());
        selCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
//        selCal.set(Calendar.MINUTE, minute);

        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.HOUR_OF_DAY, 1);

        SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");

        Log.e("USERBooking", "USERBOOKINGCHE-" + s.format(selCal.getTimeInMillis()) + " --" + s.format(currentCal.getTimeInMillis()) + "---" + (selCal.getTimeInMillis() >= currentCal.getTimeInMillis()));

        if (selCal.getTimeInMillis() > currentCal.getTimeInMillis()) {
            hourlyStartCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
           // hourlyStartCalendar.set(Calendar.MINUTE, minute);
            DateFormat date = new SimpleDateFormat("hh:mm a");
            setTime(date.format(hourlyStartCalendar.getTimeInMillis()), txt_start_time);

        } else {
            Toast.makeText(getApplicationContext(), "Minimum Check In time should be more than 1hr from current time", Toast.LENGTH_LONG).show();
        }
        timePickerDialogCheckin.dismiss();
    };


    TimePickerDialog.OnTimeSetListener timeListnerCheckOut = (view, hourOfDay, minute) -> {

        Calendar selCal = Calendar.getInstance();
       // selCal.setTime(hourlyStartCalendar.getTime());
        selCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
       // selCal.set(Calendar.MINUTE, minute);

        Calendar currentCal = Calendar.getInstance();
       // currentCal.setTime(hourlyStartCalendar.getTime());
        currentCal.set(Calendar.HOUR_OF_DAY, hourlyStartCalendar.get(Calendar.HOUR_OF_DAY));
       // currentCal.set(Calendar.MINUTE, hourlyStartCalendar.get(Calendar.MINUTE));

        SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");

        if (selCal.getTimeInMillis() > currentCal.getTimeInMillis()) {

            if ((((int) TimeUnit.MILLISECONDS.toHours(Math.abs(selCal.getTimeInMillis() - hourlyStartCalendar.getTimeInMillis()))) >= 1)
            ) {
                hourlyEndCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
               // hourlyEndCalendar.set(Calendar.MINUTE, minute);
                DateFormat date = new SimpleDateFormat("hh:mm a");
                setTime(date.format(hourlyEndCalendar.getTimeInMillis()), txt_end_time);
            } else {
                Toast.makeText(getApplicationContext(), "Minimum Check Out time should be more than 1hr from Check In time", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Minimum Check Out time should be more than 1hr from Check In time", Toast.LENGTH_LONG).show();
        }
    };


    DatePickerDialog.OnDateSetListener nightlyDatePickerCheckin = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Calendar currentCal = Calendar.getInstance();
            currentCal.set(Calendar.HOUR_OF_DAY, 0);
            currentCal.set(Calendar.MINUTE, 0);
            currentCal.set(Calendar.SECOND, 0);
            currentCal.set(Calendar.MILLISECOND, 0);
            SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");

            if (c.getTimeInMillis() >= currentCal.getTimeInMillis()) {
                nightlyStartCalendar.set(Calendar.HOUR_OF_DAY, 0);
                nightlyStartCalendar.set(Calendar.MINUTE, 0);
                nightlyStartCalendar.set(Calendar.SECOND, 0);
                nightlyStartCalendar.set(Calendar.MILLISECOND, 0);

                nightlyStartCalendar.set(year, month, day);
                setDate(day, month, year, chkin_txt_day, chkin_txt_month, chkin_txt_year);
                showCheckoutDialog();
            } else {
                Toast.makeText(getApplicationContext(), "Date Already Expired!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    DatePickerDialog.OnDateSetListener nightlyDatePickerCheckOut = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            Calendar currentCal = Calendar.getInstance();
            // currentCal.add(Calendar.DAY_OF_MONTH,1);
            currentCal.set(Calendar.HOUR_OF_DAY, 0);
            currentCal.set(Calendar.MINUTE, 0);
            currentCal.set(Calendar.SECOND, 0);
            currentCal.set(Calendar.MILLISECOND, 0);
            SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
            // Log.e("USERBooking","USERBOOKINGCHE-"+s.format(c.getTimeInMillis()) +" --"+s.format(currentCal.getTimeInMillis())+"---"+(c.getTimeInMillis()>=currentCal.getTimeInMillis()) );
            if (c.getTimeInMillis() >= currentCal.getTimeInMillis()) {
                //    Log.e("USERBooking","USERBOOKINGvele-"+s.format(c.getTimeInMillis()) +" --"+s.format(nightlyStartCalendar.getTimeInMillis())+"---"+(c.getTimeInMillis()>nightlyStartCalendar.getTimeInMillis()) );

                if (c.getTimeInMillis() > nightlyStartCalendar.getTimeInMillis()) {

                    nightlyEndCalendar.set(year, month, day);

                    setDate(day, month, year, chkout_txt_day, chkout_txt_month, chkout_txt_year);

                } else {
                    Toast.makeText(getApplicationContext(), "CheckOut date should be greater than CheckIn Date!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Date Already Expired!", Toast.LENGTH_SHORT).show();
            }
        }


    };

    private void updateGroupBookingView() {
        int roomCnt = Integer.parseInt(user_rooms.getText().toString());

        if (roomCnt < 5) {
            grpBookingCard.setVisibility(View.GONE);
            return;
        } else {
            grpBookingCard.setVisibility(View.VISIBLE);
        }

        String perNightPrice = grpBookingBudget.getText().toString();
        if (!TextUtils.isEmpty(perNightPrice)) {
            updateTotalGroupBookingPrice(perNightPrice);
        }
    }

    DatePickerDialog.OnDateSetListener hourlyDatePickerCheckIn = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            hourlyStartCalendar.set(year, month, day);
            hourlyEndCalendar.set(year, month, day);
            setDate(day, month, year, chkin_txt_day1, chkin_txt_month1, chkin_txt_year1);
        }

    };

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request


    private static final int ALL_PERMISSIONS_RESULT = 1011;

    private void checlLocationPermissions() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }

    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();

        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }


   /* private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }*/
   /* private FusedLocationProviderClient client;
    private void showingArea(){
        client = LocationServices.getFusedLocationProviderClient(this);
        geoCoder = new Geocoder(this, Locale.getDefault());
        if (ContextCompat.checkSelfPermission(UserBooking.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(UserBooking.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null) {
                    Double lat = location.getLatitude();
                    Double lan = location.getLongitude();
                    try {
                        addresses = geoCoder.getFromLocation(lat, lan, 1);
                        String address = addresses.get(0).getAddressLine(0);
                        String area = addresses.get(0).getLocality();
                        String city = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postal = addresses.get(0).getPostalCode();
                        String fullAddress = address + "\n" + area + "\n" + city + "\n" + country + "\n" + postal;
                        locationText.setText(fullAddress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {


                }
            }
        });
    }
*/
/*   public static String GetZones(String country) {
       List<String> zones = new ArrayList<>();

       for (String i : TimeZone.getAvailableIDs()) {
           if (i.contains(country)) {
               Log.v("zones","zones-"+i);
              //return i;
           }
           else {
               Log.v("zones","Invalid-zones-"+i);
           }
       }
       return null;

   }*/

    private void checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this);

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateManager.registerListener(installStateUpdatedListener);

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate();
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        if (appUpdateManager != null) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
            snackbar.setActionTextColor(
                    getResources().getColor(R.color.white));
            snackbar.show();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }
}






