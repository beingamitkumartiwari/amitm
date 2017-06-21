package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.EmailValidatorUtil;
import com.getfreerecharge.mpaisafreerecharge.utils.FlipAnimation;
import com.getfreerecharge.mpaisafreerecharge.utils.UserAvatar;
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
 * Created by DEVEN SINGH on 7/16/2015.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private MyPrefs myPrefs;
    private View rootLayout;
    private View cardFace;
    private View cardBack;
    private UserAvatar userAvatar;
    private TextView nameTv;
    private TextView emailTv;
    private TextView mobileTv;
    private ImageView genderIv;
    private EditText userName;
    private EditText userPhoneNum;
    private AutoCompleteTextView userEmail;
    private TextInputLayout userNameTiL;
    private TextInputLayout userNumTiL;
    private TextInputLayout userEmailTiL;
    private Button editProfileBt;
    private Button saveProfileBt;
    private RadioGroup gRg;
    private ArrayList<String> emails = new ArrayList<String>();
    private ProgressBar progressBar;
    String TAG_REQUEST_VERIFICATION_LINK = "tagRequestVerificationLink";
    String TAG_UPDATE_PROFILE = "tagUpdateProfile";
    String TAG_GET_PROFILE = "tagGetProfile";
    private ImageButton resendMail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        getEmailAccounts();
        getProfileFromServer();
    }

    private void getProfileFromServer() {
        if (ConnectionCheck.isConnectionAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceId", AppConstants.getImeiNum(getActivity()));
            ApplicationFL.getInstance().addToRequestQueue(requestGetProfile(new AppUrls(getActivity(),myPrefs).getHostName(10), params), TAG_GET_PROFILE);
        } else {
            updateProfileUi();
        }
    }

    private void updateProfileUi() {
        nameTv.setText(myPrefs.getUserName());
        emailTv.setText(myPrefs.getUserEmail());
        mobileTv.setText(myPrefs.getMobileNumber());
        if (myPrefs.getUserGender().equalsIgnoreCase("Male")) {
            userAvatar.setImageResource(R.mipmap.ic_user_male_light);
            genderIv.setImageResource(R.mipmap.men1);
        } else {
            genderIv.setImageResource(R.mipmap.women1);
            userAvatar.setImageResource(R.mipmap.ic_user_female_light);
        }
    }

    private void getEmailAccounts() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                emails.add(account.name);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, emails.toArray(new String[emails.toArray().length]));
        userEmail.setAdapter(adapter);
    }

    private void initComponents(View view) {
        myPrefs = new MyPrefs(getActivity());
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarProfile);
        rootLayout = view.findViewById(R.id.mainLayout);
        cardFace = view.findViewById(R.id.ll_profile);
        cardBack = view.findViewById(R.id.ll_edit_profile);
        userAvatar = (UserAvatar) view.findViewById(R.id.userPic);
        nameTv = (TextView) view.findViewById(R.id.userNameTv);
        emailTv = (TextView) view.findViewById(R.id.userEmailTv);
        mobileTv = (TextView) view.findViewById(R.id.userPhoneTv);
        genderIv = (ImageView) view.findViewById(R.id.genderIV);
        userName = (EditText) view.findViewById(R.id.userNameP);
        userPhoneNum = (EditText) view.findViewById(R.id.userNumberP);
        userEmail = (AutoCompleteTextView) view.findViewById(R.id.userEmailP);
        userNameTiL = (TextInputLayout) view.findViewById(R.id.userNameTilP);
        userEmailTiL = (TextInputLayout) view.findViewById(R.id.userEmailTilP);
        userNumTiL = (TextInputLayout) view.findViewById(R.id.userNumTilP);
        editProfileBt = (Button) view.findViewById(R.id.editProfileBt);
        saveProfileBt = (Button) view.findViewById(R.id.saveChangesBt);
        resendMail = (ImageButton) view.findViewById(R.id.resendMail);
        if (myPrefs.isVerifiedUser()) {
            resendMail.setVisibility(View.GONE);
        }
        userNameTiL.setErrorEnabled(true);
        userEmailTiL.setErrorEnabled(true);
        userNumTiL.setErrorEnabled(true);
        gRg = (RadioGroup) view.findViewById(R.id.gRgP);
        setAllViewsMethods();
        editProfileBt.setOnClickListener(this);
        saveProfileBt.setOnClickListener(this);
        resendMail.setOnClickListener(this);

    }

    private void setAllViewsMethods() {
        userPhoneNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        userPhoneNum.clearFocus();
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
                if (userPhoneNum.getText().length() > 0 && userPhoneNum.getText().length() < 10) {
                    userNumTiL.setError("Invalid mobile number");
                }
                if (userPhoneNum.getText().length() == 10) {
                    userNumTiL.setError(null);
                }
            }
        });
    }

    private String getUserGender() {
        if (gRg.getCheckedRadioButtonId() == R.id.maleP) {
            return "Male";
        } else if (gRg.getCheckedRadioButtonId() == R.id.femaleP) {
            return "Female";
        } else {
            return null;
        }
    }

    private void flipCard() {
        FlipAnimation flipAnimation = new FlipAnimation(cardFace, cardBack);
        if (cardFace.getVisibility() == View.GONE) {
            flipAnimation.reverse();
        }
        rootLayout.startAnimation(flipAnimation);
    }

    @Override
    public void onClick(View v) {
        if (v == editProfileBt) {
            flipCard();
            setEditTextValues();
        }
        if (v == saveProfileBt) {
            updateProfile();
        }
        if (v == resendMail) {
            if (!myPrefs.isVerifiedUser())
                requestForVerificationLink();
            else
                Toast.makeText(getActivity(), "You already verified your email id.", Toast.LENGTH_LONG).show();
        }
    }

    private void setEditTextValues() {
        userName.setText(myPrefs.getUserName());
        userEmail.setText(myPrefs.getUserEmail());
        userPhoneNum.setText(myPrefs.getMobileNumber());
    }

    private void updateProfile() {
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
        } else if (getUserGender() == null) {
            Toast.makeText(getActivity(), "First select your gender!", Toast.LENGTH_LONG).show();
        } else {
            if (ConnectionCheck.isConnectionAvailable(getActivity())) {
                saveProfileBt.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                String name = userName.getText().toString().trim();
                String email = userEmail.getText().toString().trim();
                String number = userPhoneNum.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceId", AppConstants.getImeiNum(getActivity()));
                params.put("userEmail", email);
                params.put("userName", name);
                params.put("userGender", getUserGender());
                params.put("userNumber", number);
                ApplicationFL.getInstance().addToRequestQueue(requestUpdateProfile(new AppUrls(getActivity(),myPrefs).getHostName(9), params, name, email, number, getUserGender()), TAG_UPDATE_PROFILE);
            } else {
                ConnectionCheck.alertDialog(getActivity());
            }
        }
    }

    private CustomJsonObjectRequest requestUpdateProfile(String url, Map<String, String> params, final String name, final String email, final String number, final String userGender) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("responseCode: ", response.getString("responseCode"));
                    if (response.get("responseCode").toString().equals("1")) {
                        myPrefs.setUserName(name);
                        myPrefs.setUserEmail(email);
                        myPrefs.setMobileNumber(number);
                        myPrefs.setUserGender(userGender);
                        flipCard();
                        updateProfileUi();
                    } else {
                        Toast.makeText(getActivity(), "Error! try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                saveProfileBt.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                saveProfileBt.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Log.e("Volley Error ", String.valueOf(error.getStackTrace()));
                Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();
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

    private CustomJsonObjectRequest requestGetProfile(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("responseCode: ", response.getString("responseCode"));
                    if (response.get("responseCode").toString().equals("1")) {
                        myPrefs.setUserName(response.getString("name"));
                        myPrefs.setUserEmail(response.getString("email"));
                        myPrefs.setMobileNumber(response.getString("mobile"));
                        myPrefs.setUserGender(response.getString("gender"));
                        updateProfileUi();
                    } else {
                        Toast.makeText(getActivity(), "Error! try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("Volley Error ", String.valueOf(error.getStackTrace()));
                Toast.makeText(getActivity(), "Server Error! please try again later.", Toast.LENGTH_SHORT).show();
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


    private void requestForVerificationLink() {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", AppConstants.getImeiNum(getActivity()));
        ApplicationFL.getInstance().addToRequestQueue(requestVerificationLink(new AppUrls(getActivity(),myPrefs).getHostName(11), params), TAG_REQUEST_VERIFICATION_LINK);
    }

    private CustomJsonObjectRequest requestVerificationLink(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("responseCode: ", response.getString("responseCode"));
                    if (response.get("responseCode").toString().equals("1")) {
                        Toast.makeText(getActivity(), "Verification link sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Error! try again.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e("Volley Error ", String.valueOf(error.getStackTrace()));
                Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationFL.getInstance().cancelPendingRequests(TAG_GET_PROFILE);
        ApplicationFL.getInstance().cancelPendingRequests(TAG_REQUEST_VERIFICATION_LINK);
        ApplicationFL.getInstance().cancelPendingRequests(TAG_UPDATE_PROFILE);
    }
}