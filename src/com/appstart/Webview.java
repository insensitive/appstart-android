package com.appstart;

import com.appstart.application.Appstartapplication;
import com.appstart.tabgroup.TabGroupActivity;
import com.appstart.utility.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Webview extends Activity {

	WebView webView;

	Button btn_back, btn_fwd, btn_back_web, btn_reload;
	boolean isProgress = false;
	Appstartapplication app;
	RelativeLayout web_lay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		app = new Appstartapplication();
		
		Bundle b = getIntent().getExtras();
		String title=b.getString("title");
		String url = b.getString("website_url");
		boolean showback=b.getBoolean("showBack");
		
		btn_back = (Button) findViewById(R.id.ib_back_music);
		
		if(showback){
			btn_back.setVisibility(View.VISIBLE);
		}else{
			btn_back.setVisibility(View.INVISIBLE);
		}
		
		((TextView) findViewById(R.id.txt_music)).setText(title);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				TabGroupActivity parentActivity = (TabGroupActivity) getParent();
				parentActivity.onBackPressed();

			}
		});
		
		setHeaderBackground();
		
		web_lay=(RelativeLayout)findViewById(R.id.web_lay);
		
		
		System.out.println("Loading website url::::"
				+ b.getString("website_url"));
		
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		
		WebSettings webSettings = webView.getSettings();
		
		webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webView.getSettings().setPluginsEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		
		
		
		webView.loadUrl(url);
		
		
		
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setWebChromeClient(new WebChromeClient());
		
		
		
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);

				System.out.println("url::::"+url);
				if (!isProgress) {
					app.startProgressDialog(getParent(), "", "Loading...");
					isProgress = true;
				}

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);

				app.stopProgressDialog();
				isProgress = false;
				
			}

		});
		
		
		btn_back_web = (Button) findViewById(R.id.btn_back_web);
		btn_fwd = (Button) findViewById(R.id.btn_fwd);
		btn_reload = (Button) findViewById(R.id.btn_reload);


		btn_back_web.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (webView.canGoBack()) {

					webView.goBack();

				}
			}
		});

		btn_fwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (webView.canGoForward()) {

					webView.goForward();
				}
			}
		});

		btn_reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				webView.reload();

			}
		});

	}
	
	
	public void setHeaderBackground() {

		((RelativeLayout) findViewById(R.id.rel_music)).setBackgroundDrawable(Util
				.HeaderBackground());

	}
	
}
