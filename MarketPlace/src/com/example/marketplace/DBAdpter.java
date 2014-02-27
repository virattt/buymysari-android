package com.example.marketplace;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.util.Log;

public class DBAdpter {
	public static String url = "http://imprintingdesign.com/Indian_Stores/users/";

	//public static String loginInUser(String username, String password) {
		public static ArrayList<UserInfo_dto> getAllUserInfo(String username, String password) {
		ArrayList<UserInfo_dto> fetch_UserDetail_data = new ArrayList<UserInfo_dto>();
		String result = "";
		String msg = "";
		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("email", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url + "login");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.v("log", "Result :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			msg = jObj.getString("message");
			JSONArray j_Array = jObj.getJSONArray("user_info");
			
			for (int i = 0; i < j_Array.length(); i++) {
				JSONObject json_objs = j_Array.getJSONObject(i);
				UserInfo_dto user_info_list = new UserInfo_dto();
				user_info_list.user_id = json_objs.getString("user_id");
				user_info_list.email = json_objs.getString("email");
				user_info_list.first_name = json_objs.getString("first_name");
				user_info_list.last_name = json_objs.getString("first_name");
				user_info_list.mobile = json_objs.getString("mobile");
				user_info_list.msg = msg;
				fetch_UserDetail_data.add(user_info_list);

				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_UserDetail_data;
		
	}

	public static String registerInUser(String firstname, String password,
			String lastname, String email, String city, String state,
			String country, String mobile) {
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fname", firstname));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("lname", lastname));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("country", country));
		nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url + "registration");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();

			is = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result = sb.toString();
			Log.v("log", "Result :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			msg = jObj.getString("Message");
			Log.v("login Object", msg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}
}
