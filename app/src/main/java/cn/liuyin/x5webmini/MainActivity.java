package cn.liuyin.x5webmini;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.liuyin.x5webmini.myhandler.MyHandler;
import cn.liuyin.x5webmini.myreceiver.CompleteReceiver;
import cn.liuyin.x5webmini.myreceiver.NetworkChangeReceiver;
import cn.liuyin.x5webmini.myunit.MyDownloadTool;
import cn.liuyin.x5webmini.myunit.MyFileTool;
import cn.liuyin.x5webmini.myunit.MyFunction;
import cn.liuyin.x5webmini.myunit.MyUiClass;
import cn.liuyin.x5webmini.mywebview.X5WebView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebIconDatabase;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import java.io.File;
import java.io.IOException;
import cn.liuyin.x5webmini.myunit.ADFilterTool;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;



//QbSdk.PreInitCallback



public class MainActivity extends Activity 
{



	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();
	private CompleteReceiver completereceiver=new CompleteReceiver();

	
	private cn.liuyin.x5webmini.mywebview.X5WebView wView=null;//=new X5WebView(MainActivity.this);
	private String resourceUrl="";
	private Point hitPoint;
	private long exitTime = 0;
	private ProgressBar web_progress;
	public LinearLayout top_layout,top_title,top_search;
	public TextView web_title;public EditText search_text;
	public ImageView img_webicon,img_searchicon;Button btn_cleartext;
	public ImageButton btn_exit,btn_goback,btn_goforward,btn_refresh,btn_search;
	public MyFunction mf=new MyFunction();
	public MyFileTool mft=new MyFileTool();
	public MyUiClass muc=new MyUiClass();
	MyDownloadTool mdt=new MyDownloadTool();
	
	//收到后处理的部分
	private MyHandler mHandler=new MyHandler(MainActivity.this);
	String searchengine=null;boolean isExit=false;boolean isX5WebViewEnabled;
	public FrameLayout vieolayout;
	private ValueCallback<Uri> uploadFile;
	
	
	public Context context=MainActivity.this;
	private String url;//网页的地址
	private String imgurl;//网页图片链接。
    private String homeurl = "file:///android_asset/index.html";//首页的地址。

	
	private Handler timehandler = new Handler();
    private Runnable timerunnable = new Runnable() {
		public void run()
		{
			timehandler.postDelayed(timerunnable, 100);
			web_title.setText(wView.getTitle());
			url = wView.getUrl();
			wView.loadUrl("javascript:myFunction()");
			//search_text.setText(url);
			int b=(int)(1 + Math.random() * (100 - 1 + 1));
			//web_progress.setSecondaryProgress(b);
			if (wView.getFavicon() != null)
			{
				img_webicon.setImageBitmap(wView.getFavicon());
			}
			else
			{
				Bitmap bt=BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.settings);
				img_webicon.setImageBitmap(bt);
			}
			if (wView.canGoBack())
			{
				btn_exit.setVisibility(View.GONE);
				btn_goback.setVisibility(View.VISIBLE);
			}
			else
			{
				btn_goback.setVisibility(View.GONE);
				btn_exit.setVisibility(View.VISIBLE);
			}


		}
    };

	private String mIntentUrl;


	//Activit生命周期


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		//初始化数据储存
		editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        pref = getSharedPreferences("data", MODE_PRIVATE);
		homeurl = pref.getString("HomeUrl", homeurl);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.

									 SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.

									 SOFT_INPUT_STATE_HIDDEN);
		
		initView();
		wView.loadUrl(homeurl);
		initIntentEvent();
		initWebViewSettings();
		RegisterReceiver();
	
		
		
		mft.deleteDirectory("/sdcard/VideoCache/cn.liuyin.x5web/main/");
		mft.deleteDirectory("/sdcard/QQBrowser/");
		mft.deleteDirectory("/sdcard/TunnyBrowser/");
		mft.deleteDirectory("/sdcard/baidu/");
		
		
		

    }

	@Override
	protected void onStart()
	{
		// TODO: Implement this method

	
		
		//开始计时
		timehandler.removeCallbacks(timerunnable);
		timehandler.postDelayed(timerunnable, 100);
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method

		//开始计时
		timehandler.removeCallbacks(timerunnable);
		timehandler.postDelayed(timerunnable, 100);
		super.onResume();
	}

	@Override
	protected void onStop()
	{
		// TODO: Implement this method
		//停止计时
		timehandler.removeCallbacks(timerunnable);
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		UnregisterReceiver();
		if (wView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = wView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(wView);
            }

            wView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            wView.getSettings().setJavaScriptEnabled(false);
            wView.clearHistory();
            wView.clearView();
            wView.removeAllViews();
			wView.clearCache(true);

            try {
                wView.destroy();
            } catch (Throwable ex) {

            }
        }
		
		
		// TODO: Implement this method
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode
		//		 + ",resultCode:" + resultCode);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 0:
					if (null != uploadFile) {
						Uri result = data == null || resultCode != RESULT_OK ? null
							: data.getData();
						uploadFile.onReceiveValue(result);
						uploadFile = null;
					}
					break;
				case 1: 

					Uri uri = data.getData();
					String path = uri.getPath();


					break;
				default:
					break;
			}
		} 
		else if (resultCode == RESULT_CANCELED) {
			if (null != uploadFile) {
				uploadFile.onReceiveValue(null);
				uploadFile = null;
			}

		}

	}
	



private void initIntentEvent(){
	Intent intent = getIntent();
	if (intent != null)
	{
		try
		{
			//mIntentUrl = new URL(intent.getData().toString());
			mIntentUrl = intent.getData().toString();
			mIntentUrl=mf.search(mIntentUrl, searchengine);
			wView.loadUrl(mIntentUrl);
		}
		catch (NullPointerException e)
		{

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	//
	try
	{
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11)
		{
			getWindow()
				.setFlags(
				android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

}
	public void RegisterReceiver(){

		//启动服务
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

		registerReceiver(networkChangeReceiver, intentFilter);

		registerReceiver(completereceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
		
	}
	public void UnregisterReceiver(){
		unregisterReceiver(networkChangeReceiver);


		unregisterReceiver(completereceiver);
	}
	




	private void initView()
	{

		
		
		//LinearLayout mll      = (LinearLayout)findViewById(R.id.container);
		
		
		if(wView==null){
			//X5WebView wView = new X5WebView(getApplicationContext());
			//mll.addView(wView);
		wView=(X5WebView)findViewById(R.id.container);
		}
		web_progress = (ProgressBar)findViewById(R.id.web_ProgressBar);
		top_layout = (LinearLayout)findViewById(R.id.top_layout);
		top_title = (LinearLayout)findViewById(R.id.top_title);
		top_search = (LinearLayout)findViewById(R.id.top_secrch);
		web_title = (TextView)findViewById(R.id.web_title);
		search_text = (EditText)findViewById(R.id.search_text);
		btn_exit = (ImageButton)findViewById(R.id.web_exit);
		btn_goback = (ImageButton)findViewById(R.id.web_goback);
		btn_goforward = (ImageButton)findViewById(R.id.web_goforward);
		img_webicon = (ImageView)findViewById(R.id.web_icon);
		btn_refresh = (ImageButton)findViewById(R.id.web_refresh);
		img_searchicon = (ImageView)findViewById(R.id.search_icon);
		btn_search = (ImageButton)findViewById(R.id.web_search);
		btn_cleartext = (Button)findViewById(R.id.btn_cleartext);
		
		
		
		

		web_progress.setVisibility(View.GONE);
		btn_exit.setOnClickListener(new MyOnClickListener());
		btn_exit.setOnLongClickListener(new MyOnLongClickListener());
		btn_goback.setOnClickListener(new MyOnClickListener());
		btn_goback.setOnLongClickListener(new MyOnLongClickListener());
		btn_goforward.setOnClickListener(new MyOnClickListener());
		btn_goforward.setOnLongClickListener(new MyOnLongClickListener());
		btn_refresh.setOnClickListener(new MyOnClickListener());
		btn_refresh.setOnLongClickListener(new MyOnLongClickListener());
		btn_search.setOnClickListener(new MyOnClickListener());
		btn_search.setOnLongClickListener(new MyOnLongClickListener());
		web_title.setOnClickListener(new MyOnClickListener());
		web_title.setOnLongClickListener(new MyOnLongClickListener());
		img_webicon.setOnClickListener(new MyOnClickListener());
		img_webicon.setOnLongClickListener(new MyOnLongClickListener());
		img_searchicon.setOnClickListener(new MyOnClickListener());
		img_searchicon.setOnLongClickListener(new MyOnLongClickListener());
		btn_cleartext.setOnClickListener(new MyOnClickListener());
		btn_cleartext.setOnLongClickListener(new MyOnLongClickListener());

		
		

		//图片长按事件。
		wView.getView().setOnLongClickListener(new View.OnLongClickListener() {

				@Override
				public boolean onLongClick(View v)
				{
					// TODO Auto-generated method stub
					WebView.HitTestResult hitTestResult=MainActivity.this.wView.getHitTestResult();
					final String path=resourceUrl = hitTestResult.getExtra();
					//HitTestResult
					//getExtra() >>String
					//Gets additional type-dependant information about the result.
					//getType()
					//Gets the type of the hit test result.
					imgurl = path;
					switch (hitTestResult.getType())
					{
						case WebView.HitTestResult.ANCHOR_TYPE:
							//This constant was deprecated in API level 14. This type is no longer used.
							break;
						case WebView.HitTestResult.EDIT_TEXT_TYPE:
							//HitTestResult for hitting an edit text area.
							break;
						case WebView.HitTestResult.EMAIL_TYPE:
							//HitTestResult for hitting an email address.
							break;
						case WebView.HitTestResult.GEO_TYPE:
							//HitTestResult for hitting a map address.
							break;
						case WebView.HitTestResult.PHONE_TYPE:
							//HitTestResult for hitting a phone number.
							break;
						case WebView.HitTestResult.SRC_ANCHOR_TYPE:
							//HitTestResult for hitting a HTML::a tag with src=http.
							break;
						case WebView.HitTestResult.UNKNOWN_TYPE:
							//Default HitTestResult, where the target is unknown.
							break;
						case WebView.HitTestResult.IMAGE_TYPE://获取点击的标签是否为图片
						    //HitTestResult for hitting an HTML::img tag.
							Toast.makeText(MainActivity.this, "当前选定的图片的URL是" + path, Toast.LENGTH_LONG).show();
						case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE://获取点击的标签是否为图片
						    //HitTestResult for hitting a HTML::a tag with src=http + HTML::img.
							Toast.makeText(MainActivity.this, "当前选定的图片的URL是" + path, Toast.LENGTH_LONG).show();
							showDialog(path, path);
							break;
					}
					return false;
				}
			});

		wView.getView().setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

				@Override
				public void onCreateContextMenu(ContextMenu menu, View v,
												ContextMenuInfo menuInfo)
				{
					// TODO Auto-generated method stub
					return;
				}
			});

		//网站图标
		WebIconDatabase.getInstance().open(getDirs(getCacheDir().getAbsolutePath() + "/icons/"));
        wView.setWebViewClient(new MyWebViewClient());	
		wView.setWebChromeClient(new MyWebChromeClient());
		wView.setDownloadListener(new MyDownloadListener());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
	{

        if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			top_title.setVisibility(View.VISIBLE);
			top_search.setVisibility(View.GONE);
			Exit();
			
        }





        return true;//;super.onKeyDown(keyCode, event);
    }
	private void showDialog(final String msg, final String path)
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("保存图片");
		builder.setMessage("url是" + path);
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					//5WebView x5=new X5WebView(getApplicationContext());
					//x5.loadUrl(imgurl);
					//mf.showDialog(getApplicationContext(),x5);
					//Thread mThread = new Thread(imgrunnable);
					//x5.loadData(linkCss, "text/html", "utf-8");
					//Download(path);
					try
					{
						mdt.down_file(getApplicationContext(), path, "/sdcard/ADM/");
					}
					catch (IOException e)
					{}
					dialog.dismiss();
				}
			});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					Thread n=new Thread();
					n.start();
					dialog.dismiss();
				}
			});
		builder.show();
	}





	

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		if (this.wView != null && hitPoint != null)
		{
			hitPoint.x = (int) event.getX();
			hitPoint.y = (int) event.getY();
		}
		return super.onTouchEvent(event);
	}


	private void initWebViewSettings()
	{
		WebSettings webSetting = wView.getSettings();
		webSetting.setJavaScriptEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setAllowFileAccess(true);
		//webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setAppCacheEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		//webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		webSetting.setUserAgentString(webSetting.getUserAgentString() + "  X5WEB");

		System.currentTimeMillis();
        CookieSyncManager.createInstance((Context)this);
        CookieSyncManager.getInstance().sync();
		//this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
		// settings 的设计
	}
	public class MyOnClickListener implements OnClickListener
	{

		@Override
		public void onClick(View p1)
		{
			// TODO: Implement this method
			switch (p1.getId())
			{
				case R.id.web_exit:
					//mf.ShowAlertDialog(MainActivity.this,mf.getHtml(""));

					finish();
					break;
				case R.id.web_goback:
					wView.goBack();
					break;
				case R.id.web_goforward:
					wView.goForward();
					break;
				case R.id.web_refresh:
					wView.clearCache(true);
					wView.reload();
					break;
				case R.id.web_search:

					top_title.setVisibility(View.VISIBLE);
					top_search.setVisibility(View.GONE);
					if (!wView.getUrl().equals(search_text.getText().toString()))
					{
						wView.loadUrl(mf.search(search_text.getText().toString(), searchengine));
						Toast.makeText(getApplicationContext(), "开始发送请求", Toast.LENGTH_SHORT).show();
						search_text.setText(url);
					}
					break;
				case R.id.web_title:
					search_text.setText(url);
					top_title.setVisibility(View.GONE);
					top_search.setVisibility(View.VISIBLE);
					break;
				case R.id.web_icon:
					addFunction();
					search_text.setText(url);
//					top_title.setVisibility(View.GONE);
//					top_search.setVisibility(View.VISIBLE);
					break;
				case R.id.search_icon:
					top_title.setVisibility(View.VISIBLE);
					top_search.setVisibility(View.GONE);
					break;
//				case R.id.tbsContent:
//					top_title.setVisibility(View.VISIBLE);
//					top_search.setVisibility(View.GONE);
//					break;
				case R.id.btn_cleartext:
					search_text.setText("");
					break;
				case R.id.menuImageButtonExit:
					finish();
					break;
				case R.id.menuImageButtonHome:
					wView.clearHistory();
					wView.loadUrl(homeurl);
					break;
				case R.id.menuImageButtonCode:
					Thread mthread=new Thread(soucerunnable);
					mthread.start();
					break;

				default:
				    break;

			}
		}


	}

	public class MyOnLongClickListener implements OnLongClickListener
	{

		@Override
		public boolean onLongClick(View p1)
		{
			// TODO: Implement this method
			switch (p1.getId())
			{
				case R.id.search_icon:
					Toast.makeText(getApplicationContext(), "切换搜索引擎功能还没有做好。", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.web_icon:
					//Toast.makeText(getApplicationContext(), "设置功能还没有做好。", Toast.LENGTH_SHORT).show();
					addFunction();
					return true;
				case R.id.web_goback:
					wView.clearHistory();
					wView.loadUrl(homeurl);
					return true;
				case R.id.web_goforward:
					wView.loadUrl(mf.getVipVideo(wView.getUrl()));
					return true;
				case R.id.menuImageButtonHome:
					ChageHomePage();
				    return true;

				default:
					return false ;
			}
		}
	}






	public static String getDirs(String path)
	{
		File dir = new File(path);
		if (!dir.exists())
		{
			dir.mkdirs();
		}
		return path;
	}

	public class MyDownloadListener implements DownloadListener
	{

		@Override
		public void onDownloadStart(String durl, String userAgent, String contentDisposition, String mimetype, long contentLength)
		{
			// TODO: Implement this method
			//mf.ShowToast(getApplicationContext(),p1+p2+p3+p4,1);
			Download(durl);
		}



	}

	//功能性方法

	public void addFunction()
	{
		final Dialog dialog=new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.menu, null);
		ImageButton btn_menu_exit=(ImageButton)layout.findViewById(R.id.menuImageButtonExit);
		btn_menu_exit.setOnClickListener(new MyOnClickListener());
		ImageButton btn_menu_home=(ImageButton)layout.findViewById(R.id.menuImageButtonHome);
		btn_menu_home.setOnClickListener(new MyOnClickListener());
		btn_menu_home.setOnLongClickListener(new MyOnLongClickListener());
		ImageButton btn_menu_code=(ImageButton)layout.findViewById(R.id.menuImageButtonCode);
		btn_menu_code.setOnClickListener(new MyOnClickListener());
		dialog.setContentView(layout);
		dialog.show();

	}


	public void ChageHomePage()
	{
		final Dialog dialog=new Dialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout lv=new LinearLayout(MainActivity.this);
		lv.setGravity(Gravity.CENTER);
		lv.setOrientation(1);
		final EditText et=new EditText(MainActivity.this);
		et.setHeight(80);
		et.setWidth(5000);
		Button btn1=new Button(MainActivity.this);

		btn1.setText("设为首页");

		btn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					homeurl = et.getText().toString();
					editor.putString("HomeUrl", homeurl);
					editor.apply();
					dialog.dismiss();
					// TODO: Implement this method
				}

			});

		lv.addView(et);
		lv.addView(btn1);
		dialog.setContentView(lv);
		dialog.show();

	}
	
	private void Exit(){
		
		if (wView.canGoBack())
		{
			wView.goBack();// 返回前一个页面
		}
		else{ 
		  if((System.currentTimeMillis()-exitTime) > 1000){  
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
            exitTime = System.currentTimeMillis();   
           } 
		  else {
            finish();
           }
		}
	}

	public void Download(final String durl)
	{
		new AlertDialog.Builder(MainActivity.this)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("提示")
			.setMessage("确定要下载该文件吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog , int which)
				{


					
					try
					{
						mdt.down_file(getApplicationContext(), durl, "/sdcard/ADM/");
					}
					catch (IOException e)
					{}


				}
			})
			.setNegativeButton("取消", null)
			.show();
	}

	private void fullScreen()
	{
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	//退出按钮功能
	String s1="javascript:function removeAD(){" + mf.getNeativeFile2String("/sdcard/Download/removead.js")+"}";
	String s2="javascript:function JsAtStart(){" + mf.getNeativeFile2String("/sdcard/Download/start.js")+"}";
	String s3="javascript:function JsAtFinish(){" + mf.getNeativeFile2String("/sdcard/Download/finish.js")+"}";
	private void removeAD(){
		
		wView.loadUrl(s1);
		wView.loadUrl("javascript:removeAD()");
	}
	private void runJsAtStart(){
		
		wView.loadUrl(s2);
		wView.loadUrl("javascript:JsAtStart()");

		
	}
	private void runJsAtFinish(){
		
		wView.loadUrl(s3);
		wView.loadUrl("javascript:JsAtFinish()");
	}
	

	public void sentRequest(String url)
	{

	}



	/*

	 */

	public class MyWebChromeClient extends WebChromeClient
	{

		Dialog dialog;
		private ValueCallback<Uri> mUploadMessage;
		@Override
		public void onReceivedTitle(WebView p1, String p2)
		{
			// TODO: Implement this method
			//
			search_text.setText(url);
			web_title.setText(p2);
			super.onReceivedTitle(p1, p2);
		}

		@Override
		public void onReceivedIcon(WebView p1, Bitmap p2)
		{
			// TODO: Implement this method
			img_webicon.setImageBitmap(p2);
			super.onReceivedIcon(p1, p2);
		}

		@Override
		public void onProgressChanged(WebView p1, int p2)
		{
			// TODO: Implement this method
			super.onProgressChanged(p1, p2);
			web_progress.setProgress(p2);
			//search_text.setText(url);
			removeAD();
			if (p2 <= 90)
			{
				web_progress.setVisibility(View.VISIBLE);
			}
			else
			{
				web_progress.setVisibility(View.GONE);
			}
		}
		
		@Override
		public boolean onShowFileChooser(WebView arg0,
										 ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
			// TODO Auto-generated method stub
			//Log.e("app", "onShowFileChooser");
			return super.onShowFileChooser(arg0, arg1, arg2);
		}

		@Override
		public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String captureType) {
			MainActivity.this.uploadFile = uploadFile;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			startActivityForResult(Intent.createChooser(i, "test"), 0);
		}

		
		
		
	}



	public class MyWebViewClient extends WebViewClient
	{
		@Override
		public void onPageStarted(WebView p1, String p2, Bitmap p3)
		{
			//search_text.setText(url);
			top_title.setVisibility(View.VISIBLE);
			top_search.setVisibility(View.GONE);
			runJsAtStart();
			
			
			
			ObjectAnimator//
				.ofFloat(p1, "rotationY", 0.0F, 360.0F)//
				.setDuration(500)//
				.start();
			

			removeAD();
			// TODO: Implement this method
			super.onPageStarted(p1, p2, p3);
		}


		@Override
		public boolean shouldOverrideUrlLoading(WebView wView, final String url)
		{

			// TODO: Implement this method
			//search_text.setText(url);
			if(ADFilterTool.hasAD(context, url)){
				Toast.makeText(MainActivity.this, "页面包含广告，以屏蔽:)", 1).show();
				return true;
			}
			
		   else if (url.trim().startsWith("http://") | url.trim().startsWith("https://") | url.trim().startsWith("file:///") | url.trim().startsWith("javascript:"))
			{
				return false;
			}
			else
			{

				if (url.trim().startsWith("sited://"))
				{
					try
					{
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
					}
					catch (Exception e)
					{
						Toast.makeText(MainActivity.this, "打开应用失败", Toast.LENGTH_LONG).show();
					}
					finally
					{
						ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						cm.setText(url);
						Toast.makeText(MainActivity.this, "网址已经复制到剪贴板:)", 1).show();
					}
				}
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(url);
				Toast.makeText(MainActivity.this, "网址已经复制到剪贴板:)", 1).show();
				return true;
			}
		}
		@Override
		public void onPageFinished(WebView p1, String p2)
		{
			runJsAtFinish();
			ObjectAnimator//
				.ofFloat(p1, "rotationY", 0.0F, 360.0F)//
				.setDuration(500)//
				.start();
			// TODO: Implement this method
			super.onPageFinished(p1, p2);
			removeAD();
			if (wView.canGoBack())
			{
				btn_exit.setVisibility(View.GONE);
				btn_goback.setVisibility(View.VISIBLE);
			}
			else
			{
				btn_goback.setVisibility(View.GONE);
				btn_exit.setVisibility(View.VISIBLE);
			}
			
		}
		
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			url = url.toLowerCase();
			if(!url.contains(homeurl)){
				if (!(ADFilterTool.hasAD(context, url))) {
					return super.shouldInterceptRequest(view, url);
				}else{
					return new WebResourceResponse(null,null,null);
				}
			}else{
				return super.shouldInterceptRequest(view, url);
			}


		}
	}











    Runnable soucerunnable = new Runnable() {
        // 重写run()方法，此方法在新的线程中运行
		String tag;
        @Override
        public void run()
		{


            try
			{

				tag = mf.getHtml(url);

            }
			catch (Exception e)
			{
                mHandler.obtainMessage(0).sendToTarget();// 获取图片失败
                return;
            }

            // 获取图片成功，向UI线程发送MSG_SUCCESS标识和bitmap对象
            mHandler.obtainMessage(1, tag).sendToTarget();
        }
    };

	


}
