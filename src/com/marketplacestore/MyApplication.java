package com.marketplacestore;

import android.app.Application;

public class MyApplication extends Application {
	public String userID;
	public String store_id;

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	public String getStoreId() {
		return store_id;
	}
	public void setStoreId(String store_Id) {
		this.store_id = store_Id;
	}
}
