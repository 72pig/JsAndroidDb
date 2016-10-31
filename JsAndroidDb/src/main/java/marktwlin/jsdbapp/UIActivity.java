package marktwlin.jsdbapp;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;


public class UIActivity extends Activity {
	private JsDBHelper jsDBHelper = null;
	private WebView webview = null;
	private AndroidCalls androidCalls = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		jsDBHelper = new JsDBHelper(this);
		webview = (WebView)findViewById(R.id.webview);
		androidCalls = new AndroidCalls();

		initWebviwEnvironment();
	}

	@Override
	protected void onDestroy() {
		if (jsDBHelper != null) {
			jsDBHelper.close();
		}
		super.onDestroy();
	}

	private void initWebviwEnvironment() {
		webview.setWebViewClient(webviewClient);
		webview.setWebChromeClient(new WebChromeClient());

		webview.getSettings().setDefaultTextEncodingName("UTF-8");
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		webview.addJavascriptInterface(androidCalls, "AndroidCalls");

		webview.loadUrl("file:///android_asset/index.html");
	}

	private WebViewClient webviewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	public class AndroidCalls {
		private final String dbQueryOneRow = "javascript:dbQueryOneRow";
		private final String dbQueryBatchSize = "javascript:dbQueryBatchSize";

		@JavascriptInterface
		public int getTotalCounter(String table) {
			return jsDBHelper.getTotalCounter(table);
		}

		@JavascriptInterface
		public void query(String sql) {
			System.out.println("sql >> " + sql);
			String[][] result = jsDBHelper.query(sql);
			int batchSize = result.length;
			for (int idx = 0; idx < batchSize; idx++) {
				webview.loadUrl("javascript:dbQueryOneRow(" + result[idx][0] + ",'" + result[idx][1] +
						"','" + result[idx][2] + "','" + result[idx][3] + "')");
			}
			webview.loadUrl("javascript:dbQueryComplete()");
		}

		@JavascriptInterface
		public long insert(String table, String[] columns, String[] values) {
			return jsDBHelper.insert(table, columns, values);
		}

		@JavascriptInterface
		public long update(String table, String[] columns, String[] values, String whereCondition) {
			return jsDBHelper.update(table, columns, values, whereCondition);
		}

		@JavascriptInterface
		public long delete(String table, String whereCondition) {
            System.out.println(whereCondition);
			return jsDBHelper.delete(table, whereCondition);
		}
	}
}
