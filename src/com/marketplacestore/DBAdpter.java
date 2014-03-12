package com.marketplacestore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.marketplacestore.dto.All_list_home_dto;
import com.marketplacestore.dto.Closet_dto;
import com.marketplacestore.dto.UserInfo_dto;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class DBAdpter {
	public static String url = "http://imprintingdesign.com/Indian_Stores/users/";
	// public static String loginInUser(String username, String password) {
	public static ArrayList<UserInfo_dto> fetch_UserDetail_data;
	public static ArrayList<Closet_dto> Closet_list__data;
	public static String storeId;
	
	public static String registerInUser(String firstname, String password,
			String lastname, String email) {
		// String city, String state,String country, String mobile
		String result = "";
		String msg = "";
		InputStream is = null;
	
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("fname", firstname));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		nameValuePairs.add(new BasicNameValuePair("lname", lastname));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		// nameValuePairs.add(new BasicNameValuePair("city", city));
		// nameValuePairs.add(new BasicNameValuePair("state", state));
		// nameValuePairs.add(new BasicNameValuePair("country", country));
		// nameValuePairs.add(new BasicNameValuePair("mobile", mobile));

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
			Log.v("log_tag", "Result :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			msg = jObj.getString("message");
			Log.v("log_tag","RegisterMsg "+ msg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public static String createUserStore(String name, String email,
			String website, String phone, String city, String state,
			String country) {
		
		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;
		
		Log.v("log"," getUserID  " + fetch_UserDetail_data.get(0).getUser_id().toString());

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("website", website));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("country", country));
		nameValuePairs.add(new BasicNameValuePair("user_id", "1"));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/store/createStore");
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
			Log.v("log", "Result Create Store :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			status = jObj.getString("success");
			msg = jObj.getString("message");
			storeId = jObj.getString("store_id");
			
			Log.v("login Object", msg + " Store_ID " + storeId);
			//

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public static String uploadStorePhoto(String base64String) {
		
		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;
		
		Log.v("log"," getUserID  " + storeId);

		Log.v("log", "base64String  " +base64String);
		
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("img", base64String));
		nameValuePairs.add(new BasicNameValuePair("id", storeId));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/store/uploadImage");
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
			Log.v("log", "Result upload photo :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			status = jObj.getString("success");
			msg = jObj.getString("message");
			Log.v("login Object", msg + " status " +status);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	// public static String loginInUser(String username, String password) {
	public static ArrayList<UserInfo_dto> getAllUserInfo(String username,
			String password) {
		fetch_UserDetail_data = new ArrayList<UserInfo_dto>();
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
			
			
			if(jObj.getString("success").equals(true))
			{
				
			JSONArray j_Array = jObj.getJSONArray("user_info");

			for (int i = 0; i < j_Array.length(); i++) {
				JSONObject json_objs = j_Array.getJSONObject(i);
				UserInfo_dto user_info_list = new UserInfo_dto();
				
				Log.v("log", " UserID : :: " + json_objs.getString("user_id"));
				
				user_info_list.setUser_id(json_objs.getString("user_id"));
				
				Log.v("log", " userID getting -->  " + json_objs.getString("user_id"));
				
				user_info_list.setEmail(json_objs.getString("email"));
				user_info_list.setFirst_name(json_objs.getString("first_name"));
				user_info_list.setLast_name(json_objs.getString("first_name"));
				user_info_list.setMobile(json_objs.getString("mobile"));
				user_info_list.setMsg(msg);
				fetch_UserDetail_data.add(user_info_list);
			}
			}
			else
			{
				UserInfo_dto user_info_list = new UserInfo_dto();
				user_info_list.setMsg(msg);
				fetch_UserDetail_data.add(user_info_list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return fetch_UserDetail_data;

	}

/*	public static ArrayList<All_list_home_dto> getNewsData(String cityname) {
		  ArrayList<All_list_home_dto> fetch_list_home_data = new ArrayList<All_list_home_dto>();
		  String result = "";
		  String msg = "";
		  // String success_txt = "";
		  InputStream is = null;

		  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		  nameValuePairs.add(new BasicNameValuePair("name", cityname));
		  

		  // http post
		  try {
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/home/newsFeed");
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
		   
		   JSONArray j_Array = jObj.getJSONArray("store_info");
		   
		   for (int i = 0; i < j_Array.length(); i++) {
		    JSONObject json_objs = j_Array.getJSONObject(i);
		    JSONArray itemInfo = json_objs.getJSONArray("item_info");
		    
		    for (int j = 0; j < itemInfo.length(); j++) {
		     All_list_home_dto list_home_data = new All_list_home_dto();
		     Log.v("log_tag","Storename ::000  "+json_objs.getString("store_name"));
		     list_home_data.store_id = json_objs.getString("id");
		     list_home_data.store_name = json_objs.getString("store_name");
		     list_home_data.city = json_objs.getString("city");
		     list_home_data.website = json_objs.getString("website");
		     list_home_data.state = json_objs.getString("state");
		     list_home_data.picture = json_objs.getString("picture");
		     
		     JSONObject json_objs_items = itemInfo.getJSONObject(j);
		     Log.v("log_tag","json_objs_items "+json_objs_items.getString("name"));
		     list_home_data.item_id=json_objs_items.getString("item_id");
		     list_home_data.name= json_objs_items.getString("name");
		     list_home_data.gender= json_objs_items.getString("gender");
		     list_home_data.category_name= json_objs_items.getString("category_name");
		     list_home_data.image= json_objs_items.getString("image");
		     list_home_data.views= json_objs_items.getString("image");
		     fetch_list_home_data.add(list_home_data);
		    }
		    

		   }
		   
		  } catch (JSONException e) {
		   e.printStackTrace();
		  }
		  return fetch_list_home_data;

		 }
	*/
	
	public static ArrayList<All_list_home_dto> getNewsData(String cityname) {
	    ArrayList<All_list_home_dto> fetch_list_home_data = new ArrayList<All_list_home_dto>();
	    String result = "";
	    String msg = "";
	    // String success_txt = "";
	    InputStream is = null;

	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("name", cityname));
	    

	    // http post
	    try {
	     HttpClient httpclient = new DefaultHttpClient();
	     HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/home/newsFeed");
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
	     
	     JSONArray j_Array = jObj.getJSONArray("store_info");
	     
	     for (int i = 0; i < j_Array.length(); i++) {
	      JSONObject json_objs = j_Array.getJSONObject(i);
	      JSONArray itemInfo = json_objs.getJSONArray("item_info");
	      
	      for (int j = 0; j < itemInfo.length(); j++) {
	       All_list_home_dto list_home_data = new All_list_home_dto();
	       Log.v("log_tag","Storename ::000  "+json_objs.getString("store_name"));
	       list_home_data.store_id = json_objs.getString("id");
	       list_home_data.store_name = json_objs.getString("store_name");
	       list_home_data.city = json_objs.getString("city");
	       list_home_data.website = json_objs.getString("website");
	       list_home_data.state = json_objs.getString("state");
	       list_home_data.picture = json_objs.getString("picture");
	       
	       JSONObject json_objs_items = itemInfo.getJSONObject(j);
	       Log.v("log_tag","json_objs_items "+json_objs_items.getString("name"));
	       list_home_data.item_id=json_objs_items.getString("item_id");
	       list_home_data.name= json_objs_items.getString("name");
	       list_home_data.gender= json_objs_items.getString("gender");
	       list_home_data.category_name= json_objs_items.getString("category_name");
	       list_home_data.image= json_objs_items.getString("image");
	       list_home_data.views=json_objs_items.getString("views");
	       fetch_list_home_data.add(list_home_data);
	      }
	      

	     }
	     
	    } catch (JSONException e) {
	     e.printStackTrace();
	    }
	    return fetch_list_home_data;

	   }
	
	public static ArrayList<Closet_dto> getClosetData(String id) {
			Closet_list__data = new ArrayList<Closet_dto>();
		  
		   String result = "";
		  String msg = "";
		  // String success_txt = "";
		  InputStream is = null;

		  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		  nameValuePairs.add(new BasicNameValuePair("id", "1"));
		  

		  // http post
		  try {
		   HttpClient httpclient = new DefaultHttpClient();
		   HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/store/viewStoreClosetedItems");
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
		   Log.v("log", "Result closet Items :" + result);
		  } catch (Exception e) {
		   Log.e("log_tag", "Error converting result " + e.toString());
		  }

		 try {
		   JSONObject jObj = new JSONObject(result);
		   
		   JSONArray j_Array = jObj.getJSONArray("Item_info");
		   
		   for (int i = 0; i < j_Array.length(); i++) {
		    JSONObject json_objs = j_Array.getJSONObject(i);
		    
		     Closet_dto list_closet_data = new Closet_dto();
		     Closet_dto.setItem_id(json_objs.getString("item_id"));
		     Closet_dto.setCategory_name(json_objs.getString("category_name"));
		     Closet_dto.setGender(json_objs.getString("gender"));
		     Closet_dto.setName(json_objs.getString("name"));
		     Closet_dto.setImage(json_objs.getString("image"));
		     
		     Closet_list__data.add(list_closet_data);
		    }
		   
		  } catch (JSONException e) {
		   e.printStackTrace();
		  }
		  return Closet_list__data;
		 }
	
	// http://imprintingdesign.com/Indian_Stores/store/viewStoreClosetedItems
	
}
