package com.getfreerecharge.mpaisafreerecharge.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.getfreerecharge.mpaisafreerecharge.R;

/**
 * Created by DEVEN SINGH on 7/9/2015.
 */
public class MyPrefs {


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    private final String DATABASE_NAME = "mPaisaFreeRechargeDb";
    private final String MOBILE_NUMBER = "mobileNumber";
    private final String REGISTERED = "registered";
    private final String TIME = "time";
    private final String SERVICE_RUNNING = "serviceRunning";
    private final String USER_GCM_REG_ID = "gcmRegId";
    private final String GCM_REG_ID_SERVER = "gcmRegIdSavedInServer";
    private final String USER_AVATAR = "userAvatar";
    private final String USER_NAME = "userName";
    private final String USER_EMAIL = "userEmail";
    private final String USER_GENDER = "userGender";
    private final String USER_UNIQUE_KEY = "userUniqueKey";
    private final String USER_ID = "userId";
    private final String VERIFIED_USER = "verifiedUser";
    private final String REFERRAL_CODE = "referralCode";
    private final String CONVERSION_RATE = "conversionRate";
    private final String CONVERSION_RATE_POLLFISH = "conversionRatePollfish";
    private final String WALLET_BALANCE = "walletBalance";
    private final String FIRST_OFFER_COMPLETED = "firstOfferCompleted";
    private final String SUPERSONIC_OLD_BALANCE = "superSonicTotalBalance";
    private String PACKAGE_NAME = "packageName";
    private String G_AD_ID = "gAdId";
    private String HOST_PORT = "hostPort";
    private String PARTICIPATION_LUCKY_DRAW = "participationLuckyDraw";
    private String LUCKY_DRAW_DATE = "luckyDrawDate";
    private String LUCKY_DRAW_APP_PACKAGE = "luckyDrawAppPackage";
    private final String SHOW_CAMPAIGN = "showCampaign";
    private final String CAMPAIGN_TYPE = "campaignType";
    private final String CAMPAIGN_IMGURL = "campaignImgurl";
    private final String CAMPAIGN_APPANAME = "campaignAppname";
    private final String CAMPAIGN_DESCRICPTION = "campaignDescription";
    private final String CAMPAIGN_PLAYSTORELINK = "campaignPlaystoreLink";
    private final String CAMPAIGN_PAYOUT = "campaignPayout";
    private final String CAMPAIGN_PACKAGENAME = "campaignPackagename";
    private final String SUPERSONIC_ID = "supersonicId";
    private final String WOOBI_ID = "woobiId";
    private final String NATIVEX_ID = "nativexId";
    private final String ADMOBFULLPAGEID = "admobFullPageId";
    private final String ADMOBFULLPAGEMONEY = "admobFullPageMoney";
    private final String CURRENT_TIME_STAMP = "currentTimeStamp";
    private final String SHOWADMOB = "showAdmob";
    private final String AD_CLICKED = "adClicked";
    private final String TRANSPARENTCLOSED = "transparentClosed";


    boolean transparentClosed;
    private Context context;

    public MyPrefs(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
    }

    public String getgAdId() {
        return sharedPreferences.getString(G_AD_ID, "");
    }

    public void setgAdId(String gAdId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(G_AD_ID, gAdId);
        spEditor.commit();
    }

    public String getWalletBalance() {
        return sharedPreferences.getString(WALLET_BALANCE, "0");
    }

    public void setWalletBalance(String walletBalance) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(WALLET_BALANCE, walletBalance);
        spEditor.commit();
    }

    public boolean isFirstOfferCompleted() {
        return sharedPreferences.getBoolean(FIRST_OFFER_COMPLETED, false);
    }

    public void setFirstOfferCompleted(boolean firstOfferCompleted) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(FIRST_OFFER_COMPLETED, firstOfferCompleted);
        spEditor.commit();
    }

    public String getMobileNumber() {
        return sharedPreferences.getString(MOBILE_NUMBER, "");
    }

    public void setMobileNumber(String mobileNumber) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(MOBILE_NUMBER, mobileNumber);
        spEditor.commit();
    }

    public boolean isRegistered() {
        return sharedPreferences.getBoolean(REGISTERED, false);
    }

    public void setRegistered(boolean registered) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(REGISTERED, registered);
        spEditor.commit();
    }

    public long getTime() {
        return sharedPreferences.getLong(TIME, 0);
    }

    public void setTime(long time) {
        spEditor = sharedPreferences.edit();
        spEditor.putLong(TIME, time);
        spEditor.commit();
    }

    public boolean isServiceRunning() {
        return sharedPreferences.getBoolean(SERVICE_RUNNING, false);
    }

    public void setServiceRunning(boolean serviceRunning) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(SERVICE_RUNNING, serviceRunning);
        spEditor.commit();
    }


    public String getGcmRegId() {
        return sharedPreferences.getString(USER_GCM_REG_ID, "");
    }

    public void setGcmRegId(String gcmRegId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_GCM_REG_ID, gcmRegId);
        spEditor.commit();
    }

    public boolean isGcmRegIdSavedInServer() {
        return sharedPreferences.getBoolean(GCM_REG_ID_SERVER, false);
    }

    public void setGcmRegIdSavedInServer(boolean gcmRegIdSavedInServer) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(GCM_REG_ID_SERVER, gcmRegIdSavedInServer);
        spEditor.commit();
    }

    public String getUserAvatar() {
        return sharedPreferences.getString(USER_AVATAR, "");
    }

    public void setUserAvatar(String userAvatar) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_AVATAR, userAvatar);
        spEditor.commit();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_NAME, userName);
        spEditor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_EMAIL, userEmail);
        spEditor.commit();
    }


    public String getUserGender() {
        return sharedPreferences.getString(USER_GENDER, "Male");
    }

    public void setUserGender(String userGender) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_GENDER, userGender);
        spEditor.commit();
    }

    public String getUserKey() {
        return sharedPreferences.getString(USER_UNIQUE_KEY, "");
    }

    public void setUserKey(String userKey) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_UNIQUE_KEY, userKey);
        spEditor.commit();
    }

    public String getUserDev() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_ID, userId);
        spEditor.commit();
    }

    public boolean isVerifiedUser() {
        return sharedPreferences.getBoolean(VERIFIED_USER, false);
    }

    public void setVerifiedUser(boolean verifiedUser) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(VERIFIED_USER, verifiedUser);
        spEditor.commit();
    }

    public String getReferralCode() {
        return sharedPreferences.getString(REFERRAL_CODE, "");
    }

    public void setReferralCode(String referralCode) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(REFERRAL_CODE, referralCode);
        spEditor.commit();
    }

    public int getConversionRate() {
        return sharedPreferences.getInt(CONVERSION_RATE, 10);
    }

    public void setConversionRate(int conversionRate) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(CONVERSION_RATE, conversionRate);
        spEditor.commit();
    }

    public String getConversionRatePollfish() {
        return sharedPreferences.getString(CONVERSION_RATE_POLLFISH, ".4");
    }

    public void setConversionRatePollfish(String conversionRatePollfish) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CONVERSION_RATE_POLLFISH, conversionRatePollfish);
        spEditor.commit();
    }

    public String getSuperSonicTotalBalance() {
        return sharedPreferences.getString(SUPERSONIC_OLD_BALANCE, "555");
    }

    public void setSuperSonicTotalBalance(String superSonicTotalBalance) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SUPERSONIC_OLD_BALANCE, superSonicTotalBalance);
        spEditor.commit();
    }

    public String getPackageName() {
        return sharedPreferences.getString(PACKAGE_NAME, "");
    }

    public void setPackageName(String packageName) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(PACKAGE_NAME, packageName);
        spEditor.commit();
    }

    public String getHostPort() {
        return sharedPreferences.getString(HOST_PORT, context.getResources().getString(R.string.base_url));
    }

    public int getLuckyDrawDate() {
        return sharedPreferences.getInt(LUCKY_DRAW_DATE, 0);
    }

    public void setLuckyDrawDate(int luckyDrawDate) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(LUCKY_DRAW_DATE, luckyDrawDate);
        spEditor.commit();
    }

    public boolean isParticipated() {
        return sharedPreferences.getBoolean(PARTICIPATION_LUCKY_DRAW, false);
    }

    public void setIsParticipated(boolean isParticipated) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(PARTICIPATION_LUCKY_DRAW, isParticipated);
        spEditor.commit();
    }

    public String getLuckyDrawAppPackage() {
        return sharedPreferences.getString(LUCKY_DRAW_APP_PACKAGE, "com.deven.sample");
    }

    public void setLuckyDrawAppPackage(String luckyDrawAppPackage) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(LUCKY_DRAW_APP_PACKAGE, luckyDrawAppPackage);
        spEditor.commit();
    }

    public boolean isShowCampaign() {
        return sharedPreferences.getBoolean(SHOW_CAMPAIGN, false);
    }

    public void setShowCampaign(boolean showCampaign) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(SHOW_CAMPAIGN, showCampaign);
        spEditor.commit();
    }

    public String getCampaignType() {
        return sharedPreferences.getString(CAMPAIGN_TYPE, "novalue");
    }

    public void setCampaignType(String campaignType) {
       spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_TYPE, campaignType);
        spEditor.commit();
    }

    public String getCampaignImgurl() {
        return sharedPreferences.getString(CAMPAIGN_IMGURL, "novalue");
    }

    public void setCampaignImgurl(String campaignImgurl) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_IMGURL,campaignImgurl);
        spEditor.commit();
    }

    public String getCampaignAppname() {
        return sharedPreferences.getString(CAMPAIGN_APPANAME, "novalue");
    }

    public void setCampaignAppname(String campaignAppname) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_APPANAME,campaignAppname);
        spEditor.commit();
    }

    public String getCampaignDescription() {
        return sharedPreferences.getString(CAMPAIGN_DESCRICPTION, "novalue");
    }

    public void setCampaignDescription(String campaignDescription) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_DESCRICPTION,campaignDescription);
        spEditor.commit();
    }

    public String getCampaignPlaystoreLink() {
        return sharedPreferences.getString(CAMPAIGN_PLAYSTORELINK, "novalue");
    }

    public void setCampaignPlaystoreLink(String campaignPlaystoreLink) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PLAYSTORELINK,campaignPlaystoreLink);
        spEditor.commit();
    }

    public String getCampaignPayout() {
        return sharedPreferences.getString(CAMPAIGN_PAYOUT, "novalue");
    }

    public void setCampaignPayout(String campaignPayout) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PAYOUT,campaignPayout);
        spEditor.commit();
    }

    public String getCampaignPackagename() {
        return sharedPreferences.getString(CAMPAIGN_PACKAGENAME,"novalue");
    }

    public void setCampaignPackagename(String campaignPackagename) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PACKAGENAME,campaignPackagename);
        spEditor.commit();
    }

    public String getSupersonicId() {
        return sharedPreferences.getString(SUPERSONIC_ID,"3d445841");
    }

    public void setSupersonicId(String supersonicId) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(SUPERSONIC_ID,supersonicId);
        spEditor.commit();
    }

    public String getWoobiId() {
        return sharedPreferences.getString(WOOBI_ID,"17056");
    }

    public void setWoobiId(String woobiId) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(WOOBI_ID,woobiId);
        spEditor.commit();
    }

    public String getNativexId() {
        return sharedPreferences.getString(NATIVEX_ID,"28067");
    }

    public void setNativexId(String nativexId) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(NATIVEX_ID,nativexId);
        spEditor.commit();
    }

    public String getAdmobFullPageId() {
        return sharedPreferences.getString(ADMOBFULLPAGEID, "ca-app-pub-8785790318965048/9011461613");
    }

    public void setAdmobFullPageId(String admobFullPageId) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(ADMOBFULLPAGEID, admobFullPageId);
        spEditor.commit();
    }

    public int getAdmobFullPageMoney() {
        return sharedPreferences.getInt(ADMOBFULLPAGEMONEY, 5);
    }

    public void setAdmobFullPageMoney(int admobFullPageMoney) {
        spEditor=sharedPreferences.edit();
        spEditor.putInt(ADMOBFULLPAGEMONEY, admobFullPageMoney);
        spEditor.commit();
    }

    public long getCurrentTimeStamp() {
        return sharedPreferences.getLong(CURRENT_TIME_STAMP, 0);
    }

    public void setCurrentTimeStamp(long currentTimeStamp) {
        spEditor = sharedPreferences.edit();
        spEditor.putLong(CURRENT_TIME_STAMP, currentTimeStamp);
        spEditor.commit();
    }

    public String getShowAdmob() {
        return sharedPreferences.getString(SHOWADMOB, "1");
    }

    public void setShowAdmob(String showAdmob) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(SHOWADMOB, showAdmob);
        spEditor.commit();
    }
    public boolean isAdClicked() {
        return sharedPreferences.getBoolean(AD_CLICKED, false);
    }

    public void setAdClicked(boolean adClicked) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(AD_CLICKED, adClicked);
        spEditor.commit();
    }

    public boolean isTransparentClosed() {
        return sharedPreferences.getBoolean(TRANSPARENTCLOSED,false);
    }

    public void setTransparentClosed(boolean transparentClosed) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(TRANSPARENTCLOSED, transparentClosed);
        spEditor.commit();
    }
}
