package com.marketplacestore;

import android.app.Application;

public class MyApplication extends Application {
	public String userID;
	

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
}
