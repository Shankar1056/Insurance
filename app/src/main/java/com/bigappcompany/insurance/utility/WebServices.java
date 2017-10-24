package com.bigappcompany.insurance.utility;

/**
 * @author Samuel Robert <sam@spotsoon.com>
 * @created on 07 Oct 2017 at 5:02 PM
 */

public class WebServices {
	public static final String BASE_URL = "http://35.154.231.41/api/";
	public static final String API_KEY = "NffrwDM2gIcFmNboeHiKu";
	public static final String SEND_OTP = BASE_URL+"send-otp";
	public static final String VERIFY_OTP = BASE_URL+"verify-otp";
	public static final String UPDATE_USER_DETAILS = BASE_URL+"update-user-details";
	public static final String FIND_VEHICLE_NUMBER = BASE_URL+"find-vehicle-no";
	public static final String GET_POLICY_CAPTION = BASE_URL+"get-policy-caption";
	public static final String SET_POLICY_DETAILS = BASE_URL+"set-policy-details";
	public static final String GET_POLICY_PROVIDER = BASE_URL+"get-policy-provider";
	public static final String UPDATE_POLICY = BASE_URL+"update-policy";
	public static final String GET_VEHICLE_TYPE = BASE_URL+"get-vehicle-type";
	public static final String GET_INSURANCE_PREMIUM = BASE_URL+"get-insurance-premium";
}
