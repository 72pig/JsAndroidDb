# This is example - how do javascript use android sqlite
We learn how do javascript use android sqlite, not html5 database.


### how do javascript call android
	// instantiate WebView
	WebView webview = new WebView();
	// set android object that js can call
	// first parameter is java object, later is variable of js that represent java object
	webview.addJavascriptInterface(androidCalls, "AndroidCalls");
	// android call javascript 
	webview.loadUrl("javascript:dbQueryComplete()");
	// see more in UIActivity.java 
	
### how android call javascript
	// javascript call android
	AndroidCalls.getTotalCounter(table);
	// see more in assets/index.html

### restriction of parameter
android can't return 2d array to javascript.

-------------------
# CRUD in example


# Reference - Android SQLiteDatabase
https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html


