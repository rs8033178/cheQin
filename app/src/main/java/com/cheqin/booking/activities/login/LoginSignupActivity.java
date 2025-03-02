package com.cheqin.booking.activities.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.ViewModelProvider;
import androidx.transition.TransitionManager;

import com.cheqin.booking.R;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class LoginSignupActivity extends AppCompatActivity implements FragmentListener {
    private ConstraintLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        container = findViewById(R.id.container);
        LoginSignupViewModel model = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginSignupViewModel.class);
        model.setFragmentListener(this);
    }

    public void startLayoutForSignup() {
        ConstraintSet newConstraintSet = new ConstraintSet();
        newConstraintSet.clone(this, R.layout.activity_layout_for_signup);
        newConstraintSet.applyTo(container);
        TransitionManager.beginDelayedTransition(container);
    }


    @Override
    public void doSignupProcess() {
        startLayoutForSignup();
    }
}
