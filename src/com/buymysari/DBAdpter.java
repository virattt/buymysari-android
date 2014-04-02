package com.buymysari;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.buymysari.dto.All_list_Store_dto;
import com.buymysari.dto.All_list_home_dto;
import com.buymysari.dto.CateDto;
import com.buymysari.dto.Closet_dto;
import com.buymysari.dto.MyStore_list_dto;
import com.buymysari.dto.Mystore_Item_dto;
import com.buymysari.dto.Store_profile_dto;
import com.buymysari.dto.UserInfo_dto;
import com.buymysari.dto.search_items_dto;

public class DBAdpter {
	public static String url = "http://imprintingdesign.com/Indian_Stores/users/";
	// public static String loginInUser(String username, String password) {
	public static ArrayList<UserInfo_dto> fetch_UserDetail_data;
	public static ArrayList<Closet_dto> Closet_list__data;
	public static ArrayList<search_items_dto> fetch_search_items_data;
	public static ArrayList<All_list_home_dto> fetch_list_home_data;
	public static ArrayList<Store_profile_dto> fetch_list_StoreProfile_data;
	
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
			Log.v("log_tag", "RegisterMsg " + msg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static ArrayList<Store_profile_dto> createUserStore(String name,
			String email, String website, String phone, String city,
			String state, String country, String user_id) {

		fetch_list_StoreProfile_data = new ArrayList<Store_profile_dto>();

		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		Log.v("log", " getUserID  " + user_id);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("website", website));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("phone", phone));
		nameValuePairs.add(new BasicNameValuePair("city", city));
		nameValuePairs.add(new BasicNameValuePair("state", state));
		nameValuePairs.add(new BasicNameValuePair("country", country));
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/createStore");
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

		//
		//

		try {
			JSONObject jObj = new JSONObject(result);
			// status = jObj.getString("success");
			msg = jObj.getString("message");

			Boolean success_con = jObj.getBoolean("success");

			if (success_con == true) {
				Log.v("log", "if login ");
				JSONArray j_Array = jObj.getJSONArray("user_info");

				for (int i = 0; i < j_Array.length(); i++) {
					JSONObject json_objs = j_Array.getJSONObject(i);
					Store_profile_dto store_profile_list_data = new Store_profile_dto();

					storeId = json_objs.getString("store_id");
					/*store_profile_list_data.setStrore_prof_user_id(json_objs
							.getString("user_id"));*/

					if (!json_objs.getString("store_id").equals("")) {
						store_profile_list_data.setStrore_prof_id(json_objs
								.getString("store_id"));
						store_profile_list_data.setStrore_prof_name(json_objs
								.getString("store_name"));
						store_profile_list_data.setStrore_prof_email(json_objs
								.getString("store_email"));
						store_profile_list_data.setStrore_prof_city(json_objs
								.getString("store_city"));
						store_profile_list_data.setStrore_prof_state(json_objs
								.getString("store_state"));
						store_profile_list_data
								.setStrore_prof_country(json_objs
										.getString("store_country"));
						store_profile_list_data.setStrore_prof_image(json_objs
								.getString("store_image"));
						store_profile_list_data.setStrore_prof_mobile(json_objs
								.getString("store_mobile"));
						store_profile_list_data
								.setStrore_prof_website(json_objs
										.getString("website"));
						store_profile_list_data.setStrore_prof_msg(msg);
					} else {
						store_profile_list_data.setStrore_prof_id(json_objs
								.getString("store_id"));
					}

					fetch_list_StoreProfile_data.add(store_profile_list_data);
					
					Log.v("log_tag", "create store name"+fetch_list_StoreProfile_data.get(0).getStrore_prof_name());
				}
			} else {
				Log.v("log", "else login ");

				Store_profile_dto store_profile_list_data = new Store_profile_dto();
				store_profile_list_data.setStrore_prof_msg(msg);
				fetch_list_StoreProfile_data.add(store_profile_list_data);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_list_StoreProfile_data;
	}


	public static String uploadStorePhoto(String base64String) {

		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		Log.v("log", " getUserID  " + storeId);

		Log.v("log", "base64String  " + base64String);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("img", base64String));
		nameValuePairs.add(new BasicNameValuePair("id", storeId));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/uploadImage");
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
			Log.v("login Object", msg + " status " + status);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	// public static String loginInUser(String username, String password) {
	public static ArrayList<UserInfo_dto> getAllUserInfo(String username,
			String password) {
		fetch_UserDetail_data = new ArrayList<UserInfo_dto>();
		fetch_list_StoreProfile_data = new ArrayList<Store_profile_dto>();
		
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

			Log.v("log", " sUCCESS --> " + jObj.getString("success"));
			Log.v("log", " MessaGE --> " + msg);

			Boolean success_con = jObj.getBoolean("success");

			if (success_con == true) {
				Log.v("log", "if login ");
				JSONArray j_Array = jObj.getJSONArray("user_info");

				for (int i = 0; i < j_Array.length(); i++) {
					JSONObject json_objs = j_Array.getJSONObject(i);
					UserInfo_dto user_info_list = new UserInfo_dto();
					Store_profile_dto store_profile_info_list = new Store_profile_dto();
					
					user_info_list.setUser_id(json_objs.getString("user_id"));

					user_info_list.setEmail(json_objs.getString("email"));
					user_info_list.setFirst_name(json_objs
							.getString("first_name"));
					user_info_list.setLast_name(json_objs
							.getString("last_name"));
					user_info_list.setMobile(json_objs.getString("mobile"));
					user_info_list.setMsg(msg);

					if (!json_objs.getString("store_id").equals("")) {
						user_info_list.setStoreId(json_objs
								.getString("store_id"));
						
						store_profile_info_list.setStrore_prof_id(json_objs
								.getString("store_id"));
						store_profile_info_list.setStrore_prof_name(json_objs
								.getString("store_name"));
						store_profile_info_list.setStrore_prof_email(json_objs
								.getString("store_email"));
						store_profile_info_list.setStrore_prof_city(json_objs
								.getString("store_city"));
						store_profile_info_list.setStrore_prof_state(json_objs
								.getString("store_state"));
						store_profile_info_list.setStrore_prof_country(json_objs
								.getString("store_country"));
						store_profile_info_list.setStrore_prof_image(json_objs
								.getString("store_image"));
						store_profile_info_list.setStrore_prof_mobile(json_objs
								.getString("store_mobile"));
						store_profile_info_list.setStrore_prof_website(json_objs
								.getString("website"));
						/*user_info_list.setStoreName(json_objs
								.getString("store_name"));
						user_info_list.setStoreEmail(json_objs
								.getString("store_email"));
						user_info_list.setStoreCity(json_objs
								.getString("store_city"));
						user_info_list.setStoreState(json_objs
								.getString("store_state"));
						user_info_list.setStoreCountry(json_objs
								.getString("store_country"));
						user_info_list.setStoreImage(json_objs
								.getString("store_image"));
						user_info_list.setStoreMobile(json_objs
								.getString("store_mobile"));
						user_info_list.setStoreWebsite(json_objs
								.getString("website"));*/
					} else {
						user_info_list.setStoreId(json_objs
								.getString("store_id"));
					}

					fetch_UserDetail_data.add(user_info_list);
					fetch_list_StoreProfile_data.add(store_profile_info_list);
				}
			} else {
				Log.v("log", "else login ");

				UserInfo_dto user_info_list = new UserInfo_dto();
				user_info_list.setMsg(msg);
				fetch_UserDetail_data.add(user_info_list);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fetch_UserDetail_data;

	}

	public static ArrayList<search_items_dto> getSearchItemsData(
			String searchText, String searchCateId, String pageNumber) {
		fetch_search_items_data = new ArrayList<search_items_dto>();
		String result = "";
		String msg = "";
		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("search", searchText));
		nameValuePairs.add(new BasicNameValuePair("cat_id", searchCateId));
		nameValuePairs.add(new BasicNameValuePair("page_number", pageNumber));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/searchStore");
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
			Log.v("log", "Result  Search Items --> :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// item_id item_name gender category_name item_image store_id store_name
		// store_image

		try {
			JSONObject jObj = new JSONObject(result);

			Boolean success_con = jObj.getBoolean("success");

			if (success_con) {

				JSONArray SearchItemsArray = jObj.getJSONArray("item_info");
				Log.v("log",
						" Search Items length -->  "
								+ SearchItemsArray.length());

				for (int i = 0; i < SearchItemsArray.length(); i++) {
					JSONObject json_objs = SearchItemsArray.getJSONObject(i);
					Log.v("log", " SearchItemsArray obj -- > " + json_objs);
					Log.v("log",
							" SearchItems  item_name -- > "
									+ json_objs.getString("item_name"));
					search_items_dto search_items = new search_items_dto();

					search_items.setItem_id(json_objs.getString("item_id"));
					search_items.setItem_name(json_objs.getString("item_name"));
					search_items.setGender(json_objs.getString("gender"));
					search_items.setItem_image(json_objs
							.getString("item_image"));

					search_items.setStore_id(json_objs.getString("store_id"));
					search_items.setStore_name(json_objs
							.getString("store_name"));
					search_items.setStore_image(json_objs
							.getString("store_image"));
					search_items.setViews(json_objs.getString("views"));

					fetch_search_items_data.add(search_items);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fetch_search_items_data;
	}

	public static ArrayList<All_list_home_dto> getNewsData(String cityname,
			String pageNumber) {
		fetch_list_home_data = new ArrayList<All_list_home_dto>();
		String result = "";
		InputStream is = null;

		Log.v("log", " pageNumber --> " + pageNumber);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", cityname));
		nameValuePairs.add(new BasicNameValuePair("page_number", pageNumber));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/home/newsFeed");
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

			Boolean success_con = jObj.getBoolean("success");
			if (success_con) {
				JSONArray j_Array = jObj.getJSONArray("store_info");

				for (int i = 0; i < j_Array.length(); i++) {
					JSONObject json_objs = j_Array.getJSONObject(i);
					JSONArray itemInfo = json_objs.getJSONArray("item_Info");

					for (int j = 0; j < itemInfo.length(); j++) {
						All_list_home_dto list_home_data = new All_list_home_dto();
						Log.v("log_tag",
								"Storename ::000  "
										+ json_objs.getString("store_name"));
						list_home_data.store_id = json_objs.getString("id");
						list_home_data.store_name = json_objs
								.getString("store_name");
						list_home_data.city = json_objs.getString("city");
						list_home_data.website = json_objs.getString("website");
						list_home_data.state = json_objs.getString("state");
						list_home_data.picture = json_objs.getString("picture");

						JSONObject json_objs_items = itemInfo.getJSONObject(j);
						Log.v("log_tag",
								"json_objs_items "
										+ json_objs_items.getString("name"));
						list_home_data.item_id = json_objs_items
								.getString("item_id");
						list_home_data.name = json_objs_items.getString("name");
						list_home_data.gender = json_objs_items
								.getString("gender");
						// list_home_data.category_name =
						// json_objs_items.getString("category_name");
						list_home_data.image = json_objs_items
								.getString("image");
						list_home_data.views = json_objs_items
								.getString("views");
						fetch_list_home_data.add(list_home_data);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_list_home_data;

	}

	public static ArrayList<Closet_dto> getUserClosetData(String id , String pageNumber) {
		Closet_list__data = new ArrayList<Closet_dto>();

		String result = "";
		String msg = "";
		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", id));
		nameValuePairs.add(new BasicNameValuePair("page_number", pageNumber));
		
		// http://imprintingdesign.com/Indian_Stores/store/viewStoreClosetedItems
		// http post

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/users/viewUserClosetedItems");
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

			Boolean success_con = jObj.getBoolean("success");

			if (success_con) {

				JSONArray itemInfo = jObj.getJSONArray("item_info");

				for (int j = 0; j < itemInfo.length(); j++) {
					// Closeted_item_track
					// store_id
					// store_name
					// store_image
					Closet_dto list_closet_data = new Closet_dto();

					JSONObject jsonitem = itemInfo.getJSONObject(j);

					list_closet_data.setItem_id(jsonitem.getString("item_id"));
					list_closet_data.setCategory_name(jsonitem
							.getString("category_name"));
					list_closet_data.setGender(jsonitem.getString("gender"));
					list_closet_data.setName(jsonitem.getString("item_name"));
					list_closet_data.setImage(jsonitem.getString("item_image"));

					list_closet_data
							.setStore_id(jsonitem.getString("store_id"));
					list_closet_data.setCloseted_item_track(jsonitem
							.getString("Closeted_item_track"));
					list_closet_data.setStore_name(jsonitem
							.getString("store_name"));
					list_closet_data.setStore_image(jsonitem
							.getString("store_image"));

					Log.v("log",
							" item_name " + jsonitem.getString("item_name"));

					Closet_list__data.add(list_closet_data);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return Closet_list__data;
	}

	/*
	 * public static ArrayList<All_list_Store_dto> getStoreData(String Id) {
	 * ArrayList<All_list_Store_dto> fetch_list_store_data = new
	 * ArrayList<All_list_Store_dto>(); String result = "";
	 * 
	 * // String success_txt = ""; InputStream is = null;
	 * 
	 * ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	 * nameValuePairs.add(new BasicNameValuePair("id", Id));
	 * 
	 * // http post try { HttpClient httpclient = new DefaultHttpClient();
	 * HttpPost httppost = new HttpPost(
	 * "http://imprintingdesign.com/Indian_Stores/store/viewStoreInformation");
	 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	 * HttpResponse response = httpclient.execute(httppost); HttpEntity entity =
	 * response.getEntity();
	 * 
	 * is = entity.getContent(); } catch (Exception e) { Log.e("log_tag",
	 * "Error in http connection " + e.toString()); } // convert response to
	 * string try { BufferedReader reader = new BufferedReader(new
	 * InputStreamReader( is, "iso-8859-1"), 8); StringBuilder sb = new
	 * StringBuilder(); String line = null; while ((line = reader.readLine()) !=
	 * null) { sb.append(line + "\n"); } is.close(); result = sb.toString();
	 * Log.v("log", "Result :" + result); } catch (Exception e) {
	 * Log.e("log_tag", "Error converting result " + e.toString()); }
	 * 
	 * try { JSONObject jObj = new JSONObject(result);
	 * 
	 * JSONObject j_Array = jObj.getJSONObject("store_info");
	 * 
	 * JSONArray itemInfo = j_Array.getJSONArray("Item_info");
	 * 
	 * for (int j = 0; j < itemInfo.length(); j++) { All_list_Store_dto
	 * list_store_data = new All_list_Store_dto();
	 * 
	 * list_store_data.store_id = j_Array.getString("store_id");
	 * list_store_data.store_name = j_Array.getString("store_name");
	 * list_store_data.email = j_Array.getString("email"); list_store_data.city
	 * = j_Array.getString("city"); list_store_data.state =
	 * j_Array.getString("state"); list_store_data.country =
	 * j_Array.getString("country"); list_store_data.mobile =
	 * j_Array.getString("mobile"); list_store_data.website =
	 * j_Array.getString("website"); list_store_data.closeted_item_count =
	 * j_Array.getString("closeted_item_count");
	 * list_store_data.subscribed_store_count =
	 * j_Array.getString("subscribed_store_count"); list_store_data.store_image
	 * = j_Array.getString("store_image");
	 * 
	 * JSONObject json_objs_items = itemInfo.getJSONObject(j);
	 * 
	 * list_store_data.item_id = json_objs_items.getString("item_id");
	 * list_store_data.name = json_objs_items.getString("name");
	 * list_store_data.gender = json_objs_items.getString("gender");
	 * list_store_data.image = json_objs_items.getString("image");
	 * 
	 * fetch_list_store_data.add(list_store_data);
	 * 
	 * }
	 * 
	 * } catch (JSONException e) { e.printStackTrace(); } return
	 * fetch_list_store_data;
	 * 
	 * }
	 */

	public static ArrayList<All_list_Store_dto> getStoreData(String Id) {
		ArrayList<All_list_Store_dto> fetch_list_store_data = new ArrayList<All_list_Store_dto>();
		String result = "";

		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", Id));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/viewStoreInformation");
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

			Log.v("log", " try ");

			JSONObject j_Array = jObj.getJSONObject("store_info");

			JSONArray itemInfo = j_Array.getJSONArray("Item_info");

			Log.v("log", " images length  " + itemInfo.length());

			if (itemInfo.length() > 0) {
				for (int j = 0; j < itemInfo.length(); j++) {
					All_list_Store_dto list_store_data = new All_list_Store_dto();

					Log.v("log", " for if itemInfo length " + itemInfo.length());

					JSONObject json_objs_items = itemInfo.getJSONObject(j);

					list_store_data.store_id = j_Array.getString("store_id");
					list_store_data.store_name = j_Array
							.getString("store_name");
					list_store_data.email = j_Array.getString("email");
					list_store_data.city = j_Array.getString("city");
					list_store_data.state = j_Array.getString("state");
					list_store_data.country = j_Array.getString("country");
					list_store_data.mobile = j_Array.getString("mobile");
					list_store_data.website = j_Array.getString("website");
					list_store_data.closeted_item_count = j_Array
							.getString("closeted_item_count");
					list_store_data.subscribed_store_count = j_Array
							.getString("subscribed_store_count");
					list_store_data.store_image = j_Array
							.getString("store_image");

					list_store_data.item_id = json_objs_items
							.getString("item_id");
					list_store_data.name = json_objs_items.getString("name");
					list_store_data.gender = json_objs_items
							.getString("gender");
					list_store_data.image = json_objs_items.getString("image");

					fetch_list_store_data.add(list_store_data);

				}
			} else {
				All_list_Store_dto list_store_data = new All_list_Store_dto();

				Log.v("log", " else itemInfo length " + itemInfo.length());

				list_store_data.store_id = j_Array.getString("store_id");
				list_store_data.store_name = j_Array.getString("store_name");
				list_store_data.email = j_Array.getString("email");
				list_store_data.city = j_Array.getString("city");
				list_store_data.state = j_Array.getString("state");
				list_store_data.country = j_Array.getString("country");
				list_store_data.mobile = j_Array.getString("mobile");
				list_store_data.website = j_Array.getString("website");
				list_store_data.closeted_item_count = j_Array
						.getString("closeted_item_count");
				list_store_data.subscribed_store_count = j_Array
						.getString("subscribed_store_count");
				list_store_data.store_image = j_Array.getString("store_image");

				list_store_data.item_id = "";
				list_store_data.name = "";
				list_store_data.gender = "";
				list_store_data.image = "";
				fetch_list_store_data.add(list_store_data);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_list_store_data;

	}

	public static String updateItemView(String itemId) {

		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", itemId));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/uploadItems/trackItemCount");
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
			Log.v("log", "Result update Views :" + result);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	public static String userClosestStore(String item_id, String store_id,
			String user_id) {

		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("item_id", item_id));
		nameValuePairs.add(new BasicNameValuePair("store_id", store_id));
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/users/userClosetedItems");
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
			Log.v("log_tag", "Result DBADPTER 196 :" + result);
			msg = jObj.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}
	
	public static ArrayList<UserInfo_dto> updateUserInfo(String id, String fname, String lname,
			String email, String password) {
		Log.v("log_tag", "fname update " + fname);
		Log.v("log_tag", "lname update " + lname);
		Log.v("log_tag", "email update " + email);
		Log.v("log_tag", "password update " + password);

		fetch_UserDetail_data = new ArrayList<UserInfo_dto>();
		
		String result = "";
		String msg = "";
		InputStream is = null;
		// String user_id = "1";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("fname", fname));
		nameValuePairs.add(new BasicNameValuePair("lname", lname));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/users/updateUserInfo");
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
			Log.v("log", "Result Update User INfo :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			Log.v("log_tag",
					"Result updated Profile MSg :" + jObj.getString("message"));

			msg = jObj.getString("message");
			Boolean success_con = jObj.getBoolean("success");
			
			if (success_con == true) {
				Log.v("log", "if login ");
				JSONArray j_Array = jObj.getJSONArray("user_info");

				for (int i = 0; i < j_Array.length(); i++) {
					JSONObject json_objs = j_Array.getJSONObject(i);
					UserInfo_dto user_info_list = new UserInfo_dto();

					user_info_list.setUser_id(json_objs.getString("user_id"));

					user_info_list.setEmail(json_objs.getString("email"));
					user_info_list.setFirst_name(json_objs
							.getString("first_name"));
					user_info_list.setLast_name(json_objs
							.getString("last_name"));
					user_info_list.setMobile(json_objs.getString("mobile"));
					user_info_list.setMsg(msg);

					fetch_UserDetail_data.add(user_info_list);
				}
			} else {
				Log.v("log", "else login ");

				UserInfo_dto user_info_list = new UserInfo_dto();
				user_info_list.setMsg(msg);
				fetch_UserDetail_data.add(user_info_list);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fetch_UserDetail_data;
	}

	public static ArrayList<MyStore_list_dto> getMyStoreData(String Id,
			   String pageNumber) {
			  ArrayList<MyStore_list_dto> fetch_list_Mystore_data = new ArrayList<MyStore_list_dto>();
			  String result = "";

			  // String success_txt = "";
			  InputStream is = null;

			  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			  nameValuePairs.add(new BasicNameValuePair("user_id", Id));
			  nameValuePairs.add(new BasicNameValuePair("page_number", pageNumber));

			  // http post
			  try {
			   HttpClient httpclient = new DefaultHttpClient();
			   HttpPost httppost = new HttpPost(
			     "http://imprintingdesign.com/Indian_Stores/users/viewUserSubscribedStore");
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
			   Log.v("log", "Result ListMyStoreData :" + result);
			  } catch (Exception e) {
			   Log.e("log_tag", "Error converting result " + e.toString());
			  }

			  try {
			   JSONObject jObj = new JSONObject(result);

			   Boolean status = jObj.getBoolean("success");

			   if (status) {

			    JSONArray j_Array = jObj.getJSONArray("store_info");

			    for (int j = 0; j < j_Array.length(); j++) {
			     JSONObject json_objs = j_Array.getJSONObject(j);
			     MyStore_list_dto list_mystore_data = new MyStore_list_dto();

			     list_mystore_data.store_id = json_objs
			       .getString("store_id");
			     list_mystore_data.name = json_objs.getString("name");
			     list_mystore_data.image = json_objs.getString("image");

			     fetch_list_Mystore_data.add(list_mystore_data);

			    }
			   }

			  } catch (JSONException e) {
			   e.printStackTrace();
			  }
			  return fetch_list_Mystore_data;

			 }
	
	public static ArrayList<Mystore_Item_dto> getMyStoreItemData(String Id,
			   String pageNumber) {
			  ArrayList<Mystore_Item_dto> fetch_listItem_Mystore_data = new ArrayList<Mystore_Item_dto>();
			  String result = "";

			  // String success_txt = "";
			  InputStream is = null;

			  ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			  nameValuePairs.add(new BasicNameValuePair("store_id", Id));
			  nameValuePairs.add(new BasicNameValuePair("page_number", pageNumber));

			  // http post
			  try {
			   HttpClient httpclient = new DefaultHttpClient();
			   HttpPost httppost = new HttpPost(
			     "http://imprintingdesign.com/Indian_Stores/users/viewStoreItems");
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
			   Log.v("log", "Result ListMyStoreData :" + result);
			  } catch (Exception e) {
			   Log.e("log_tag", "Error converting result " + e.toString());
			  }

			  try {
			   JSONObject jObj = new JSONObject(result);
			   Boolean status = jObj.getBoolean("success");

			   if (status) {
			    JSONArray j_Array = jObj.getJSONArray("itemInfo");

			    for (int j = 0; j < j_Array.length(); j++) {
			     JSONObject json_objs = j_Array.getJSONObject(j);
			     Mystore_Item_dto listitem_mystore_data = new Mystore_Item_dto();

			     listitem_mystore_data.item_id = json_objs
			       .getString("item_id");
			     listitem_mystore_data.name = json_objs.getString("name");
			     listitem_mystore_data.image = json_objs.getString("image");
			     listitem_mystore_data.Closeted_item_track = json_objs
			       .getString("Closeted_item_track");

			     fetch_listItem_Mystore_data.add(listitem_mystore_data);

			    }
			   }

			  } catch (JSONException e) {
			   e.printStackTrace();
			  }
			  return fetch_listItem_Mystore_data;

			 }
	
	public static String storeSubscribeData(String user_id, String store_id) {
		// String city, String state,String country, String mobile
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
		nameValuePairs.add(new BasicNameValuePair("store_id", store_id));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url + "userSubscribedStore");
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
			Log.v("log_tag", "RegisterMsg " + msg);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;

	}

	public static ArrayList<CateDto> fetchCategoryNames() {
		// String city, String state,String country, String mobile
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<CateDto> fetch_cate_data = new ArrayList<CateDto>();

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/category");
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
			Log.v("log_tag", "Result category Names --> " + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);

			JSONArray CateInfoArray = jObj.getJSONArray("cat_Info");
			// cat_id
			Log.v("log", " Category length -->  " + CateInfoArray.length());

			for (int i = 0; i < CateInfoArray.length(); i++) {
				JSONObject json_objs = CateInfoArray.getJSONObject(i);
				Log.v("log", " Cate Jason obj -- > " + json_objs);
				Log.v("log",
						" Cate Jason name -- > " + json_objs.getString("name")
								+ " cate_id " + json_objs.getString("cat_id"));
				CateDto cate_dto = new CateDto();

				cate_dto.setCate_id(json_objs.getString("cat_id"));
				cate_dto.setCategory_name(json_objs.getString("name"));

				fetch_cate_data.add(cate_dto);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fetch_cate_data;
	}

	public static ArrayList<Store_profile_dto> updateUserStore(String str_store_id, String str_name,
			String str_email, String str_website, String str_mobile,
			String str_city, String base64string) {

		
		fetch_list_StoreProfile_data = new ArrayList<Store_profile_dto>();
		
		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", str_store_id));
		nameValuePairs.add(new BasicNameValuePair("name", str_name));
		nameValuePairs.add(new BasicNameValuePair("email", str_email));
		nameValuePairs.add(new BasicNameValuePair("website", str_website));
		nameValuePairs.add(new BasicNameValuePair("city", str_city));
		nameValuePairs.add(new BasicNameValuePair("phone", str_mobile));
		nameValuePairs.add(new BasicNameValuePair("img", base64string));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/store/updateStoreInfo");
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
			Log.v("log", "Result Update Store :" + result);
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		try {
			JSONObject jObj = new JSONObject(result);
			// status = jObj.getString("success");
			msg = jObj.getString("message");
			Boolean success_con = jObj.getBoolean("success");

			if (success_con == true) {
				
				JSONObject jObjStoreInfo = new JSONObject(jObj.getString("store_info"));

				
					Store_profile_dto store_profile_list_data = new Store_profile_dto();

					if (!jObjStoreInfo.getString("store_id").equals("")) {
						store_profile_list_data.setStrore_prof_id(jObjStoreInfo
								.getString("store_id"));
						store_profile_list_data.setStrore_prof_name(jObjStoreInfo
								.getString("name"));
						store_profile_list_data.setStrore_prof_email(jObjStoreInfo
								.getString("email"));
						store_profile_list_data.setStrore_prof_city(jObjStoreInfo
								.getString("city"));
						store_profile_list_data.setStrore_prof_state(jObjStoreInfo
								.getString("state"));
						store_profile_list_data
								.setStrore_prof_country(jObjStoreInfo
										.getString("country"));
						store_profile_list_data.setStrore_prof_image(jObjStoreInfo
								.getString("image"));
						store_profile_list_data.setStrore_prof_mobile(jObjStoreInfo
								.getString("mobile"));
						store_profile_list_data
								.setStrore_prof_website(jObjStoreInfo
										.getString("website"));
						store_profile_list_data.setStrore_prof_msg(msg);
					} else {
						store_profile_list_data.setStrore_prof_id(jObjStoreInfo
								.getString("store_id"));
					}

					fetch_list_StoreProfile_data.add(store_profile_list_data);
				
			} else {
				Log.v("log", "else login ");

				Store_profile_dto store_profile_list_data = new Store_profile_dto();
				store_profile_list_data.setStrore_prof_msg(msg);
				fetch_list_StoreProfile_data.add(store_profile_list_data);
			}
			
			Log.v("log_tag", " Store Profile Name updated " +fetch_list_StoreProfile_data.get(0).getStrore_prof_name());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_list_StoreProfile_data;
	}

	public static Bitmap downloadBitmap(String url) {
		// initilize the default HTTP client object
		Bitmap image = null;

		final DefaultHttpClient client = new DefaultHttpClient();

		// forming a HttoGet request
		final HttpGet getRequest = new HttpGet(url);
		try {

			HttpResponse response = client.execute(getRequest);

			// check 200 OK for success
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;

			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					// getting contents from the stream
					inputStream = entity.getContent();

					// decoding stream data back into image Bitmap that android
					// understands
					image = BitmapFactory.decodeStream(inputStream);

				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// You Could provide a more explicit error message for IOException
			getRequest.abort();
			Log.e("ImageDownloader", "Something went wrong while"
					+ " retrieving bitmap from " + url + e.toString());
		}

		return image;
	}

	public static String userUpdateImageStore(String id, String name,
			String cat_id, String img, String gender) {

		String status = "";
		String result = "";
		String msg = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("cat_id", cat_id));
		nameValuePairs.add(new BasicNameValuePair("img", img));
		nameValuePairs.add(new BasicNameValuePair("gender", gender));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://imprintingdesign.com/Indian_Stores/uploadItems/uploadItemToGallery");
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
			Log.v("log_tag", "Result DBADPTER 196 :" + result);
			msg = jObj.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return msg;
	}

	// http://imprintingdesign.com/Indian_Stores/store/viewStoreClosetedItems
	// http://imprintingdesign.com/Indian_Stores/store/category
}
