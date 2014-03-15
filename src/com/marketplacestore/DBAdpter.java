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

import com.marketplacestore.dto.All_list_Store_dto;
import com.marketplacestore.dto.All_list_home_dto;
import com.marketplacestore.dto.CateDto;
import com.marketplacestore.dto.Closet_dto;
import com.marketplacestore.dto.MyStore_list_dto;
import com.marketplacestore.dto.Mystore_Item_dto;
import com.marketplacestore.dto.UserInfo_dto;
import com.marketplacestore.dto.search_items_dto;

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
			
			Log.v("log"," sUCCESS --> " + jObj.getString("success"));
			Log.v("log"," MessaGE --> " + msg);
			
			if (jObj.getString("success").equals("true")) {
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
					user_info_list.setStoreId(json_objs.getString("store_id"));
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

	public static ArrayList<search_items_dto> getSearchItemsData(String searchText) {
	    ArrayList<search_items_dto> fetch_search_items_data = new ArrayList<search_items_dto>();
	    String result = "";
	    String msg = "";
	    // String success_txt = "";
	    InputStream is = null;

	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("search", searchText));
	   // nameValuePairs.add(new BasicNameValuePair("cat_id", searchCateId));
	    
	    // http post
	    try {
	     HttpClient httpclient = new DefaultHttpClient();
	     HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/store/searchStore");
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
	    
	    // item_id item_name gender category_name item_image store_id store_name store_image
	    
	    try {
			JSONObject jObj = new JSONObject(result);
			
			JSONArray SearchItemsArray = jObj.getJSONArray("item_info");
			// cat_id
			Log.v("log"," Search Items length -->  " + SearchItemsArray.length());
			
			for(int i=0; i< SearchItemsArray.length();i++)
			{
				JSONObject json_objs = SearchItemsArray.getJSONObject(i);
				Log.v("log", " SearchItemsArray obj -- > " + json_objs);
				Log.v("log", " SearchItems  item_name -- > " + json_objs.getString("item_name") + " category_name " + json_objs.getString("category_name"));
				search_items_dto search_items = new search_items_dto();
				
				search_items.setItem_id(json_objs.getString("item_id"));
				search_items.setItem_name(json_objs.getString("item_name"));
				search_items.setCategory_name(json_objs.getString("category_name"));
				search_items.setGender(json_objs.getString("gender"));
				search_items.setItem_image(json_objs.getString("item_image"));
				
				search_items.setStore_id(json_objs.getString("store_id"));
				search_items.setStore_name(json_objs.getString("store_name"));
				search_items.setStore_image(json_objs.getString("store_image"));
				search_items.setViews(json_objs.getString("views"));
				
				fetch_search_items_data.add(search_items);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    
		return fetch_search_items_data;
	}
	
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
		  nameValuePairs.add(new BasicNameValuePair("id", id));
		  

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
		   
		   JSONArray j_Array = jObj.getJSONArray("store_Info");
		     
		     for (int i = 0; i < j_Array.length(); i++) {
		      JSONObject json_objs = j_Array.getJSONObject(i);
		      JSONArray itemInfo = json_objs.getJSONArray("Item_info");
		      
		      for (int j = 0; j < itemInfo.length(); j++) {
		    
	    	 Closet_dto list_closet_data = new Closet_dto();
		    	 
	    	 list_closet_data.setStore_id(json_objs.getString("store_id"));
	    	 list_closet_data.setStore_name(json_objs.getString("store_name"));
	    	 list_closet_data.setStore_image(json_objs.getString("store_image"));
		
	    	 Log.v("log"," store_name " + json_objs.getString("store_name"));
	    	 
		     JSONObject jsonitem = itemInfo.getJSONObject(j);
		     
		     list_closet_data.setItem_id(jsonitem.getString("item_id"));
		     list_closet_data.setCategory_name(jsonitem.getString("category_name"));
		     list_closet_data.setGender(jsonitem.getString("gender"));
		     list_closet_data.setName(jsonitem.getString("item_name"));
		     list_closet_data.setCloseted_item_track(jsonitem.getString("Closeted_item_track"));
		     list_closet_data.setImage(jsonitem.getString("item_image"));
		     
		     Log.v("log"," store_name " + jsonitem.getString("item_name"));
		     
		     Closet_list__data.add(list_closet_data);
		     }
		    }
		  } catch (JSONException e) {
		   e.printStackTrace();
		  }
		  return Closet_list__data;
		 }
	
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

		   JSONObject j_Array = jObj.getJSONObject("store_info");

		   JSONArray itemInfo = j_Array.getJSONArray("Item_info");

		   for (int j = 0; j < itemInfo.length(); j++) {
		    All_list_Store_dto list_store_data = new All_list_Store_dto();

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

		    JSONObject json_objs_items = itemInfo.getJSONObject(j);

		    list_store_data.item_id = json_objs_items.getString("item_id");
		    list_store_data.name = json_objs_items.getString("name");
		    list_store_data.gender = json_objs_items.getString("gender");

		    list_store_data.image = json_objs_items.getString("image");

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
	
	public static String updateUserInfo(String fname, String lname ,String email,String password,String id) {
		  Log.v("log_tag","fname update "+fname);
		  Log.v("log_tag","lname update "+lname);
		  Log.v("log_tag","email update "+email);
		  Log.v("log_tag","password update "+password);
		  
		  String result = "";
		  String msg = "";
		  InputStream is = null;
		//  String user_id = "1";

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
		   	Log.v("log_tag", "Result updated Profile MSg :" + jObj.getString("message"));
		   	
		   	msg = jObj.getString("message");
		   	
		  } catch (JSONException e) {
		   e.printStackTrace();
		  }
		  
		  return msg;
		 }

	public static ArrayList<MyStore_list_dto> getMyStoreData(String Id) {
		ArrayList<MyStore_list_dto> fetch_list_Mystore_data = new ArrayList<MyStore_list_dto>();
		String result = "";

		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user_id", Id));

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

			JSONArray j_Array = jObj.getJSONArray("store_info");

			

			for (int j = 0; j < j_Array.length(); j++) {
				JSONObject json_objs = j_Array.getJSONObject(j);
				MyStore_list_dto list_mystore_data = new MyStore_list_dto();

				list_mystore_data.store_id = json_objs.getString("store_id");
				list_mystore_data.name = json_objs.getString("name");
				list_mystore_data.image = json_objs.getString("image");
				

				fetch_list_Mystore_data.add(list_mystore_data);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return fetch_list_Mystore_data;

	}
	public static ArrayList<Mystore_Item_dto> getMyStoreItemData(String Id) {
		ArrayList<Mystore_Item_dto> fetch_listItem_Mystore_data = new ArrayList<Mystore_Item_dto>();
		String result = "";

		// String success_txt = "";
		InputStream is = null;

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("store_id", Id));

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

			JSONArray j_Array = jObj.getJSONArray("itemInfo");

			

			for (int j = 0; j < j_Array.length(); j++) {
				JSONObject json_objs = j_Array.getJSONObject(j);
				Mystore_Item_dto listitem_mystore_data = new Mystore_Item_dto();

				listitem_mystore_data.item_id = json_objs.getString("item_id");
				listitem_mystore_data.name = json_objs.getString("name");
				listitem_mystore_data.image = json_objs.getString("image");
				listitem_mystore_data.Closeted_item_track = json_objs.getString("Closeted_item_track");
				

				fetch_listItem_Mystore_data.add(listitem_mystore_data);

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
			HttpPost httppost = new HttpPost("http://imprintingdesign.com/Indian_Stores/store/category");
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
			Log.v("log"," Category length -->  " + CateInfoArray.length());
			
			for(int i=0; i< CateInfoArray.length();i++)
			{
				JSONObject json_objs = CateInfoArray.getJSONObject(i);
				Log.v("log", " Cate Jason obj -- > " + json_objs);
				Log.v("log", " Cate Jason name -- > " + json_objs.getString("name") + " cate_id " + json_objs.getString("cat_id"));
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
	// http://imprintingdesign.com/Indian_Stores/store/viewStoreClosetedItems
	// http://imprintingdesign.com/Indian_Stores/store/category
}
