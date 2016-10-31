# This is example - how do javascript use android sqlite
To learn how do javascript use android sqlite, not html5 database.

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
I provide a set of basical interface for one table. 
You can modify it for your demand.

### android provide interface for js
	// table is string
	getTotalCounter(table)
	// sql is string
	query(sql)
	// table is string
	// columns is field of array
	// values is value of array corresponding to columns
	insert(table, columns, values)
	// table is string
	// columns is field of array
	// values is value of array corresponding to columns
	// whereCondition is string, ex: "where id = 1"
	update(table, columns, values, whereCondition)
	// table is string
	// whereCondition is string, ex: "where id = 1"
	delete(table, whereCondition)

### js provide interface for android
	// beacause android can't return 2d-array to js
	// we provide interface that android call it one by one after selecting
	dbQueryOneRow(sid, name, price, description)
	// we provide interface that android call complete callback after calling 
	dbQueryComplete()
-------------------
# future
I will try to make it easy.

-------------------
# Reference - Android SQLiteDatabase
https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html


