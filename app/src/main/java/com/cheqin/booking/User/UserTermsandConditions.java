package com.cheqin.booking.User;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.cheqin.booking.Adapter.FaqPagerAdapter;
import com.cheqin.booking.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by user on 06-11-2015.
 */
public class UserTermsandConditions extends AppCompatActivity {

    protected Context context = null;
    private PagerAdapter faq_adapter = null;
    private ViewPager faq_viewPager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Terms and Conditions");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.faqtab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("terms"));
        tabLayout.addTab(tabLayout.newTab().setText("policy"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        context = getApplicationContext();

//        Log.e(getPackageName().getClass().toString(), "entered in pager class");

        faq_viewPager = (ViewPager) findViewById(R.id.faq_pager);
        faq_viewPager.setOffscreenPageLimit(0);
        faq_adapter = new FaqPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        faq_viewPager.setAdapter(faq_adapter);
        faq_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                faq_viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

}
