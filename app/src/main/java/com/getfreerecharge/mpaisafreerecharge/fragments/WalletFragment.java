package com.getfreerecharge.mpaisafreerecharge.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.getfreerecharge.mpaisafreerecharge.R;
import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;
import com.getfreerecharge.mpaisafreerecharge.utils.ApplicationFL;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppConstants;
import com.getfreerecharge.mpaisafreerecharge.utils.app_constants.AppUrls;
import com.getfreerecharge.mpaisafreerecharge.volley_works.ConnectionCheck;
import com.getfreerecharge.mpaisafreerecharge.volley_works.CustomJsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DEVEN SINGH on 7/1/2015.
 */

public class WalletFragment extends Fragment implements View.OnClickListener {

    private TextView walletBalance;
    private String TAG_BALANCE = "walletBalanceTag";
    private String TAG_REDEEM = "redeemTag";
    private ProgressBar progressBar;
    private MyPrefs myPrefs;
    private ScrollView llRedeem;
    private LinearLayout notEnoughBalance;
    private RadioGroup radioGroup;
    private RadioButton selfRadio;
    private RadioButton othersRadio;
    private Button redeemButton;
    private TextView selectOperator;
    private TextView selectAmount;
    private EditText redeemNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        requestForWalletBalance();
    }

    private void initComponents(View view) {
        myPrefs = new MyPrefs(getActivity());
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarWallet);
        walletBalance = (TextView) view.findViewById(R.id.walletBalance);
        llRedeem = (ScrollView) view.findViewById(R.id.ll_redeem);
        notEnoughBalance = (LinearLayout) view.findViewById(R.id.notEnoughBalance);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        selfRadio = (RadioButton) view.findViewById(R.id.radioSelf);
        othersRadio = (RadioButton) view.findViewById(R.id.radioOthers);
        redeemNumber = (EditText) view.findViewById(R.id.redeemMobileNum);
        redeemButton = (Button) view.findViewById(R.id.redeemButton);
        selectOperator = (TextView) view.findViewById(R.id.select_operator);
        selectAmount = (TextView) view.findViewById(R.id.select_ammount);
        radioGroup.setOnCheckedChangeListener(radioGroupCheckedListner);
        redeemNumber.setText(myPrefs.getMobileNumber());
        redeemNumber.setEnabled(false);
        redeemNumber.setTextColor(Color.BLACK);
        redeemButton.setOnClickListener(this);
        selectAmount.setOnClickListener(this);
        selectOperator.setOnClickListener(this);
        updateUI(myPrefs.getWalletBalance());
    }

    RadioGroup.OnCheckedChangeListener radioGroupCheckedListner = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radioSelf:
                    redeemNumber.setEnabled(false);
                    redeemNumber.setText(myPrefs.getMobileNumber().toString().trim());
                    redeemNumber.setTextColor(Color.BLACK);
                    break;
                case R.id.radioOthers:
                    redeemNumber.setEnabled(true);
                    redeemNumber.setText("");
                    break;
            }
        }
    };

    private void requestForWalletBalance() {
        progressBar.setVisibility(View.VISIBLE);
        Map<String, String> params = new HashMap<String, String>();
        params.put("deviceId", AppConstants.getImeiNum(getActivity()));
        ApplicationFL.getInstance().addToRequestQueue(requestFromWalletBalance(new AppUrls(getActivity(), myPrefs).getHostName(3), params), TAG_BALANCE);
    }

    private CustomJsonObjectRequest requestFromWalletBalance(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("SHY", "Response: " + response.toString());
                    Log.e("responseCode: ", response.getString("responseCode"));
                    if (response.get("responseCode").toString().equals("1")) {
                        myPrefs.setWalletBalance(response.getString("remainingBalance"));
                        walletBalance.setText(Html.fromHtml("You have " + "<b>" + response.get("remainingBalance") + " Rs.</b>" + " in your wallet."));
                        updateUI(myPrefs.getWalletBalance());
                    } else {
                        Toast.makeText(getActivity(), "Error! Try again!", Toast.LENGTH_SHORT).show();
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

    private void updateUI(String walletBalance) {
        if (Float.parseFloat(myPrefs.getWalletBalance()) < 50f) {
            llRedeem.setVisibility(View.GONE);
            notEnoughBalance.setVisibility(View.VISIBLE);
        } else {
            notEnoughBalance.setVisibility(View.GONE);
            llRedeem.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == selectAmount) {
            setRedeemValues("Select Amount", R.array.amount, "amount");
        }
        if (v == selectOperator) {
            setRedeemValues("Select Operator", R.array.operator, "operator");
        }
        if (v == redeemButton) {
            if (myPrefs.isVerifiedUser()) {
                if (ConnectionCheck.isConnectionAvailable(getActivity())) {
                    redeemAmountForRecharge();
                } else {
                    ConnectionCheck.alertDialog(getActivity());
                }
            } else {
                alertNoticeForVerification();
            }
        }
    }

    private void redeemAmountForRecharge() {
        if (redeemNumber.getText().toString().equals("") || redeemNumber.getText().toString().length() < 10 || selectOperator.getText().toString().trim().contains("Select Operator") || selectAmount.getText().toString().trim().contains("Select Amount")) {
            Toast.makeText(getActivity(), "Please fill details correctly", Toast.LENGTH_LONG).show();
        } else if (Float.parseFloat(selectAmount.getText().toString().trim()) > Float.parseFloat(myPrefs.getWalletBalance())) {
            Toast.makeText(getActivity(), "You don't have enough balance.", Toast.LENGTH_LONG).show();
        } else {
            verifyPopUp(redeemNumber.getText().toString().trim(), Integer.parseInt(selectAmount.getText().toString().trim()), selectOperator.getText().toString());
        }
    }

    private void verifyPopUp(final String num, final int amnt, final String opertor) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Redeem ?");
        alertDialogBuilder
                .setMessage("Mobile Number: " + num + "\n" + "Amount: " + amnt + "\n" + "Operator: " + opertor)
                .setCancelable(true)
                .setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                myPrefs.setWalletBalance(String.valueOf(Float.parseFloat(myPrefs.getWalletBalance()) - amnt));
                                requestForRedeemAmount(num, String.valueOf(amnt), opertor);
                            }
                        })
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    private void requestForRedeemAmount(String rechargeMobile, String amount, String operator) {
        if (ConnectionCheck.isConnectionAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            Map<String, String> params = new HashMap<String, String>();
            params.put("deviceid", AppConstants.getImeiNum(getActivity()));
            params.put("uniqueid", myPrefs.getUserKey());
            params.put("rechargemobile", rechargeMobile);
            params.put("redeemamount", amount);
            params.put("operatorr", operator);
            ApplicationFL.getInstance().addToRequestQueue(requestRedeemForRecharge(new AppUrls(getActivity(), myPrefs).getHostName(7), params), TAG_REDEEM);
        } else {
            ConnectionCheck.alertDialog(getActivity());
        }
    }

    private CustomJsonObjectRequest requestRedeemForRecharge(String url, Map<String, String> params) {
        CustomJsonObjectRequest customJsonObjectRequest = new CustomJsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Log.e("Remaining Balance ", response.toString());
                try {
                    myPrefs.setWalletBalance(response.getString("remainingBalance").toString().trim());
                    alertPopUp(response.getString("remainingBalance"));
                    walletBalance.setText(Html.fromHtml("You have " + "<b>" + response.getString("remainingBalance") + " Rs.</b>" + " in your wallet."));
                    updateUI(myPrefs.getWalletBalance());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error! your request can't be process right now, please try again later.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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

    private void setRedeemValues(String tittle, final int array, final String fromWhich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        builder.setTitle(tittle)
                .setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (fromWhich == "amount") {
                            selectAmount.setText(AppConstants.AMOUNT[which]);
                        }
                        if (fromWhich == "operator") {
                            selectOperator.setText(AppConstants.OPERATOR[which]);
                        }
                    }
                });
        Dialog dialog = builder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        dialog.show();
    }

    private void alertPopUp(final String balance) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Offer");
        alertDialogBuilder
                .setMessage("Your recharge will be done within 48 hours. In case any delay please contact support team with your registered email ID." + "\nYour remaining balance are: " + balance)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
//                                updateUI(myPrefs.getWalletBalance());
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }


    private void alertNoticeForVerification() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(), R.style.DevenDialogTheme);
        alertDialogBuilder.setTitle("Offer");
        alertDialogBuilder
                .setMessage("Please verify your email id first. To verify your account click verification link sent by us to your registered email id. If you have not gotten link yet, then ask for the same from profile page.")
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Window window = alertDialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(
                    android.graphics.Color.TRANSPARENT));
        }
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApplicationFL.getInstance().cancelPendingRequests(TAG_BALANCE);
        ApplicationFL.getInstance().cancelPendingRequests(TAG_REDEEM);
    }
}
