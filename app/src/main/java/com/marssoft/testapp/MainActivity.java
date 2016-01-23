package com.marssoft.testapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.net.Network;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.marssoft.testapp.network.NetworkApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // UI references.
    private EditText mEtEmail;
    private EditText mEtName;
    private EditText mEtPhone;
    private ScrollView mScrollView;
    private NetworkApi.NetworkCallback mNetworkCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScrollView = (ScrollView) findViewById(R.id.user_form);
        mEtEmail = (EditText) findViewById(R.id.etEmail);
        mEtName = (EditText) findViewById(R.id.etName);
        mEtPhone = (EditText) findViewById(R.id.etPhone);
        mEtPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.etPhone || id == EditorInfo.IME_NULL) {
                    attemptSend();
                    return true;
                }
                return false;
            }
        });
        mEtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        Button mBtSend = (Button) findViewById(R.id.btSend);
        mBtSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }

    private void attemptSend() {
        hideSoftKeyboard();
        // Reset errors.
        mEtEmail.setError(null);
        mEtName.setError(null);
        mEtPhone.setError(null);

        // Store values at the time of the login attempt.
        String email = mEtEmail.getText().toString();
        String name = mEtName.getText().toString();
        String phone = mEtPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            mEtName.setError(getString(R.string.error_field_required));
            focusView = mEtName;
            cancel = true;
        } else if (!isNameValid(name)) {
            mEtName.setError(getString(R.string.error_name));
            focusView = mEtName;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEtEmail.setError(getString(R.string.error_field_required));
            focusView = mEtEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEtEmail.setError(getString(R.string.error_invalid_email));
            focusView = mEtEmail;
            cancel = true;
        }

        // Check for a valid phone.
        if (TextUtils.isEmpty(phone)) {
            mEtPhone.setError(getString(R.string.error_field_required));
            focusView = mEtPhone;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mEtPhone.setError(getString(R.string.error_phone));
            focusView = mEtPhone;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt send and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Kick off a background task to
            // perform the user data send attempt.
            NetworkApi.sendUserDataByPost(name, email, phone, getNetworkCallback());
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isNameValid(String name) {
        //TODO: Replace this with your own logic
        return name.length() > 2;
    }

    private boolean isPhoneValid(String phone) {
        if (phone == null) {
            return false;
        } else {
            if (phone.length() < 6 || phone.length() > 13) {
                return false;
            } else {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            }
        }
    }

    public NetworkApi.NetworkCallback getNetworkCallback() {
        if (mNetworkCallback == null) {
            mNetworkCallback = new NetworkApi.NetworkCallback() {
                @Override
                public void onError(String message) {
                    Snackbar.make(mScrollView, message, Snackbar.LENGTH_SHORT)
                            .setAction(R.string.retry, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    attemptSend();
                                }
                            }).show();
                }

                @Override
                public void onSuccess(Object result) {
                    Snackbar.make(mScrollView, R.string.success, Snackbar.LENGTH_SHORT).show();
                }
            };
        }
        return mNetworkCallback;
    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

}

