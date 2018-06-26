package me.steffenjacobs.fetchgrades.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import me.steffenjacobs.fetchgrades.gradedisplay.GradeDisplayActivity;
import me.steffenjacobs.fetchgrades.R;
import me.steffenjacobs.fetchgrades.gradefetcher.FetchGrades;
import me.steffenjacobs.fetchgrades.util.AndroidUtil;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private AuthenticatorService authenticatorService;
    private SettingsStorageService settingsStorageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle(getString(R.string.login_activity_title));
        authenticatorService = new AuthenticatorService(this);
        settingsStorageService = new SettingsStorageService(this);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                goToNextActivity();
                return true;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        if (loadCredentials() && !isLogoutRedirect()) {
            goToNextActivity();
        }
    }

    private boolean isLogoutRedirect() {
        final Bundle b = getIntent().getExtras();
        return b != null && b.getBoolean("logout-redirect");
    }

    private boolean loadCredentials() {
        if (settingsStorageService.getUsername() != null) {
            mEmailView.setText(settingsStorageService.getUsername());
            mPasswordView.setText(settingsStorageService.getPassword());
            return true;
        }
        return false;
    }

    private void goToNextActivity() {

        if (((CheckBox) findViewById(R.id.checkbox_save_login)).isChecked()) {
            settingsStorageService.saveCredentials(mEmailView.getText().toString(), mPasswordView.getText().toString());
        }

        /*String cipher = authenticatorService.encryptString("Test");
        System.out.println("Cipher: " + cipher);
        String plain_back = authenticatorService.decryptString(cipher);
        System.out.println("Plain-Back: " + plain_back);*/

        //enable network activity in main thread
        AndroidUtil.allowNetworkOnMainThread();

        //TODO: move to background service
        FetchGrades grades = new FetchGrades(mEmailView.getText().toString(), mPasswordView.getText().toString());
        try {
            if (!grades.hasGrades()) {
                mEmailView.setError(getString(R.string.login_incorrect_login));
                mPasswordView.setError(getString(R.string.login_incorrect_login));
                return;
            }
        } catch (IOException e) {
            mEmailView.setError(getString(R.string.login_service_unavailable));
            mPasswordView.setError(getString(R.string.login_service_unavailable));
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent(LoginActivity.this, GradeDisplayActivity.class);

        Bundle b = new Bundle();
        b.putString("username", mEmailView.getText().toString());
        b.putString("password", mPasswordView.getText().toString());
        intent.putExtras(b);
        startActivity(intent);
    }


    private void execAuthTask() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            System.out.println(mEmail + " - " + mPassword);
//            setContentView(R.layout.content_grade_display);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.login_incorrect_login));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

