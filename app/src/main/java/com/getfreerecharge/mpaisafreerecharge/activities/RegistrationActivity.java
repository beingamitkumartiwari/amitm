package com.getfreerecharge.mpaisafreerecharge.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.EmailValidatorUtil;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.ConnectionCheck;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by DEVEN SINGH on 7/9/2015.
 */
public class RegistrationActivity extends AppCompatActivity {

    private EditText userName;
    private EditText userPhoneNum;
    private EditText referralCode;
    private AutoCompleteTextView userEmail;
    private TextInputLayout userNameTiL;
    private TextInputLayout userNumTiL;
    private TextInputLayout userEmailTiL;
    private TextInputLayout referralTil;
    private Button registerMe;
    private TextView tos;
    private RadioGroup gRg;
    private ArrayList<String> emails = new ArrayList<String>();
    private MyPrefs myPrefs;
    private ProgressBar progressBar;
    private String TAG_REGISTRATION = "registrationRequest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initComponents();
        getEmailAccounts();
    }

    private void getEmailAccounts() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, emails.toArray(new String[emails.toArray().length]));
        userEmail.setAdapter(adapter);
    }

    private void initComponents() {
        myPrefs = new MyPrefs(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        userName = (EditText) findViewById(R.id.userName);
        userPhoneNum = (EditText) findViewById(R.id.userNumber);
        referralCode = (EditText) findViewById(R.id.referral);
        userEmail = (AutoCompleteTextView) findViewById(R.id.userEmail);
        userNameTiL = (TextInputLayout) findViewById(R.id.userNameTil);
        userEmailTiL = (TextInputLayout) findViewById(R.id.userEmailTil);
        userNumTiL = (TextInputLayout) findViewById(R.id.userNumTil);
        referralTil = (TextInputLayout) findViewById(R.id.referralTil);
        userNameTiL.setErrorEnabled(true);
        userEmailTiL.setErrorEnabled(true);
        userNumTiL.setErrorEnabled(true);
        referralTil.setErrorEnabled(true);
        gRg = (RadioGroup) findViewById(R.id.gRg);
        tos = (TextView) findViewById(R.id.ppCB);
        registerMe = (Button) findViewById(R.id.registerMe);
        tos.setText(Html.fromHtml(
                "By creating an account, you accept our " +
                        "<a href=\"http://www.mpaisa.info/mPaisaTC.aspx\">Terms of Service</a> "));
        tos.setMovementMethod(LinkMovementMethod.getInstance());
        setAllViewsMethods();
        registerMe.setOnClickListener(mRegisterButtonClickListner);
    }

    private void setAllViewsMethods() {
        referralCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        referralCode.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        userEmail.clearFocus();
                        userName.clearFocus();
                        userPhoneNum.clearFocus();
                    } catch (Exception e) {
                    }
                }
                return false;
            }
        });
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable edt) {
                if (userName.getText().length() > 0 && userName.getText().length() < 3) {
                    userNameTiL.setError("Username must be at least 3 characters.");
                }
                if (userName.getText().length() > 2) {
                    userNameTiL.setError(null);
                }
            }
        });

        userEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (EmailValidatorUtil.validate(userEmail.getText().toString())) {
                    userEmailTiL.setError(null);
                }
            }
        });
        userPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userPhoneNum.getText().length() == 10) {
                    userNumTiL.setError(null);
                }
            }
        });
        referralCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (referralCode.getText().length() >= 7) {
                    referralTil.setError(null);
                }
            }
        });
    }

    View.OnClickListener mRegisterButtonClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerUser();
        }
    };

    private void registerUser() {
        if (TextUtils.isEmpty(userName.getText())) {
            userNameTiL.setError("This field should not be left blank.");
        } else if (userName.getText().length() < 3) {
            userNameTiL.setError("Username must be at least 3 characters.");
        } else if (TextUtils.isEmpty(userEmail.getText())) {
            userEmailTiL.setError("This field should not be left blank.");
        } else if (!EmailValidatorUtil.validate(userEmail.getText().toString())) {
            userEmailTiL.setError("Fill correct Email address.");
        } else if (TextUtils.isEmpty(userPhoneNum.getText())) {
            userNumTiL.setError("This field should not be left blank.");
        } else if (userPhoneNum.length() < 10) {
            userNumTiL.setError("Fill correct mobile number.");
        } else if (referralCode.getText().length() > 0 && referralCode.getText().length() < 7) {
            referralTil.setError("Invalid referral code.");
        } else if (getUserGender() == null) {
            Toast.makeText(this, "First select your gender!", Toast.LENGTH_LONG).show();
        } else {
            if (ConnectionCheck.isConnectionAvailable(this)) {
                registerMe.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String number = userPhoneNum.getText().toString();
                String referral = "not";
                if (referralCode.getText().toString().trim().length() >= 7) {
                    referral = referralCode.getText().toString().trim();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", AppConstants.getImeiNum(this));
                params.put("userEmail", email);
                params.put("userName", name);
                params.put("userGender", getUserGender());
                params.put("userNumber", number);
                params.put("refCode", referral);
                ApplicationFL.getInstance().addToRequestQueue(requestCustomRegistration(new AppUrls(RegistrationActivity.this,myPrefs).getHostName(1), params, name, email, number, getUserGender()), TAG_REGISTRATION);
            } else {
                ConnectionCheck.alertDialog(this);
            }
        }
    }

    private CustomJsonObjectRequest requestCustomRegistration(String url, Map<String, String> params, final String name, final String email, final String phoneNum, final String gender) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    VolleyLog.d("TAg Volley ", "Response: " + response.toString());
                    Log.d("SHY", "Response: " + response.toString());
                    Log.e("responseCode: ", response.getString("responseCode"));
                    if (response.get("responseCode").toString().equals("1")) {
                        myPrefs.setUserId(response.get("key").toString());
                        myPrefs.setUserName(name);
                        myPrefs.setUserEmail(email);
                        myPrefs.setMobileNumber(phoneNum);
                        myPrefs.setUserGender(gender);
                        myPrefs.setUserKey(AppConstants.getUserKey(RegistrationActivity.this, myPrefs));
                        myPrefs.setReferralCode(name.substring(0, 3) + AppConstants.getReferralCode(RegistrationActivity.this, myPrefs));
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                        finish();
                    } else if (response.get("responseCode").toString().equals("2")) {
                        Toast.makeText(getApplicationContext(), "Invalid Referral code. Enter valid referral code or leave it blank ", Toast.LENGTH_SHORT).show();
                    } else if (response.get("responseCode").toString().equals("3")) {
                        Toast.makeText(getApplicationContext(), "It seems you are already registered. Please exit from app and restart app.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                registerMe.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                registerMe.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Log.e("Volley Error ", String.valueOf(error.getStackTrace()));
                Toast.makeText(RegistrationActivity.this, "Server Error! registration failed, please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        customJsonObjectRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        return customJsonObjectRequest;
    }

    private String getUserGender() {
        if (gRg.getCheckedRadioButtonId() == R.id.male) {
            return "Male";
        } else if (gRg.getCheckedRadioButtonId() == R.id.female) {
            return "Female";
        } else {
            return null;
        }
    }

}
