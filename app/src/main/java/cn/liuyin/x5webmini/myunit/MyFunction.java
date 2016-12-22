package cn.liuyin.x5webmini.myunit;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.liuyin.x5webmini.R;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;

public class MyFunction
{
	public boolean b=false;
	
	public String search(String url,String searchengine )
	{
		String tagUrl;
		String url1=url;
		// Pattern pattern=Pattern.compile("^([hH][tT][tT][pP]://)(([A-Za-z0-9-~]+)\\/)+([A-Za-z0-9-~\\/])+$");//支持file协议
		
		String searchengine1="http://m.baidu.com/s?word=";
		
		if (searchengine == null || searchengine == "")
		{
			searchengine = searchengine1;
		}
		if (url.startsWith("file:///")||url.startsWith("javascript:"))
		{
			tagUrl=url;
		}
		else if (url.startsWith("http://") || url.startsWith("https://"))
		{
			tagUrl=url;
		}
		else
		{
			url = "http://" + url;
			if (Patterns.WEB_URL.matcher(url).matches())
			{ 
				//符合标准 
				tagUrl=url;
			}
			else
			{ 
				//不符合标准 
				url = url1;
				url = searchengine + url;
				tagUrl=url;
			} 
		}
		return tagUrl;

		//搜索();
	}
	public boolean ChoseBox(Context context,String message)
	{   
	
		new AlertDialog.Builder(context)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("提示")
			.setMessage(message)
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog , int which)
				{
				boolean a=true;
				b=a;
					
				}
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog , int which)
				{
					
                  boolean a=false;
				  b=a;

				}
			})
			.show();
		return b;
	}
	
	public void ShowAlertDialog(Context context,String message){
	new AlertDialog.Builder(context)
	.setTitle("警告")
	.setMessage(message)
	.create()
	.show();
	}
	public void ShowAlertDialog(Context context,String title,String message){
		new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(message)
			.create()
			.show();
	}
	public void ShowToast(Context context,String message,int lenth){
		Toast.makeText(context,message,lenth).show();
	}
	
	public void ShowDiaog(final Context context,final String message){
		final Dialog dialog=new Dialog(context);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.souce, null);
		
		TextView tv=(TextView)layout.findViewById(R.id.souceTextView);
		tv.setText(message);
		tv.setTextIsSelectable(true);
		Button btn_cancler=(Button)layout.findViewById(R.id.souceButtonCancer);
		Button btn_copy=(Button)layout.findViewById(R.id.souceButtonCopy);
		dialog.setContentView(layout);

        dialog.setTitle("查看源码");
		
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
		lp.x = 0; // 新位置X坐标
        lp.y = 100; // 新位置Y坐标
        lp.width = 700; // 宽度
        lp.height = 1000; // 高度
        //lp.alpha = 0.7f; // 透明度
		dialogWindow.setAttributes(lp);
		dialog.show();
		btn_cancler.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					dialog.dismiss();
				}
			});
		btn_copy.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					// TODO: Implement this method
					dialog.dismiss();
					
					ClipboardManager cm = (ClipboardManager)context. getSystemService(Context.CLIPBOARD_SERVICE);
					cm.setText(message);
					Toast.makeText(context, "源码已经复制到剪贴板:)", 1).show();
				}
			});
	}
	public void showDialog(Context context,Bitmap bitmap){
		Dialog dialog=new Dialog(context);
		ImageView iv=new ImageView(context);
		iv.setImageBitmap(bitmap);
		dialog.setContentView(iv);
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.TOP);
		lp.x = 0; // 新位置X坐标
        lp.y = 100; // 新位置Y坐标
        lp.width = 700; // 宽度
        lp.height = 1000; // 高度
        lp.alpha = 0.7f; // 透明度
		dialogWindow.setAttributes(lp);
		dialog.show();
	}

	
	
	public Bitmap getWebBitMap(String url) { 
		URL myFileUrl = null; 
		Bitmap bitmap = null; 
		try { 
			myFileUrl = new URL(url); 
		} catch (MalformedURLException e) { 
			e.printStackTrace(); 
		} 
		try { 
			HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection(); 
			conn.setDoInput(true); 
			conn.connect(); 
			InputStream is = conn.getInputStream(); 
			bitmap = BitmapFactory.decodeStream(is); 
			is.close(); 
		} catch (IOException e) { 
			e.printStackTrace(); 
		} 
		return bitmap; 
	} 
	public String getHtml(String url){
		
	
	// 定义即将访问的链接
		//url = "http://apiv.ga/magnet/";
	// 定义一个字符串用来存储网页内容
	String result = "";
	// 定义一个缓冲字符输入流
	BufferedReader in = null;
	try {
		// 将string转成url对象
		URL realUrl = new URL(url);
		// 初始化一个链接到那个url的连接
		URLConnection connection = realUrl.openConnection();
		// 开始实际的连接
		connection.connect();
		// 初始化 BufferedReader输入流来读取URL的响应
		in = new BufferedReader(new InputStreamReader(					connection.getInputStream()));
		// 用来临时存储抓取到的每一行的数据
		String line;
		while ((line = in.readLine()) != null) {
			//遍历抓取到的每一行并将其存储到result里面
			result += line+"\n";
		}
	} catch (Exception e) {
		e.printStackTrace();
		result="发送GET请求出现异常！\n" + e.toString();
	}
	// 使用finally来关闭输入流
	finally {
		try {
			if (in != null) {
				in.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	return result;
	}
	
	/**
	 * 从指定的URL中获取数组
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public static String Myget(String urlPath)  {  
		String result=null;
		try{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
			byte[] data = new byte[1024];  
			int len = 0;  
			InputStream inStream = null;
			URL url = new URL(urlPath); 
			if(urlPath.startsWith("https://")){
				HttpsURLConnection conn=(HttpsURLConnection) url.openConnection();
				conn.setRequestProperty("User-Agent", "okhttp/3.4.1");

				inStream = conn.getInputStream();  
			}
			else{
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
				conn.setRequestProperty("User-Agent", "okhttp/3.4.1");
				inStream = conn.getInputStream();  
			}
			while ((len = inStream.read(data)) != -1) {  
				outStream.write(data, 0, len);  
			}  
			inStream.close();  
			result= new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据  
		}
		catch(Exception e){
			result=e.toString();
		}
		return result;
	}


	
	public String getNeativeFile2String(String path){
		String result;
		try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);  
            String str = "";   
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
            	str = str+mimeTypeLine;
    		} 
           result =str;
		} catch (Exception e) {
			e.printStackTrace();
			result=e.toString();
		}
		return result;
	}
	
	
	//读取文本文件中的内容
    public static String[] getNeativeFile2Strings(String strFilePath)
    {
        String path = strFilePath;
		int i=0;
        String[] content =null; //文件内容字符串
		//打开文件
		File file = new File(path);
		//如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory())
		{
			content=null;
		}
		else
		{
			try {
				InputStream instream = new FileInputStream(file); 
				if (instream != null) 
				{
					InputStreamReader inputreader = new InputStreamReader(instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					//分行读取
					while (( line = buffreader.readLine()) != null) {
						content[i]=line;
						i++;
					}                
					instream.close();
				}
			}
			catch (java.io.FileNotFoundException e) 
			{
				content=null;
			} 
			catch (IOException e) 
			{
				content=null;
			}
		}
		return content;
    }
		
	
	
	/*
	*vip视频解析。
	*/
	public String getVipVideo(String url){
		String api1="http://jx.71ki.com/index.php?url=";
		String api2="http://jx.71ki.com/tong/index.php?url=";
		String api3="http://www.wmxz.wang/video.php?url=";
		String api4="http://player.gakui.top/?url=";
		String api5="http://47.89.49.245/video.php?url=";
	    String api6="http://www.yydy8.com/Common/?url=";
		String api7="http://www.97zxkan.com/jiexi/97zxkanapi.php?url=";
		String api8="http://www.wmxz.wang/video.php?url=";
		String tx="https://m.v.qq.com/x/cover/";
		String yk="http://m.youku.com/video/id_";
		String aqy="http://m.iqiyi.com/";
		String ls="http://m.le.com/";
		String sh="http://m.film.sohu.com/album/";
		String mg="http://m.mgtv.com/";
		if(url.startsWith(tx)){
			url=url.replace("https://m.","http://");
			url=url.substring(0,url.indexOf("?="));
		}
		if(url.startsWith(yk)){
			url=url.replace("http://m.","http://v.");
			url=url.substring(0,url.indexOf("?"));
		}
		if(url.startsWith(aqy)){
			url=url.replace("http://m.","http://www.");

		}
		if(url.startsWith(ls)){
			//url=url.replace("http://m.","http://www.");
			//url=url.replace("vplay_","ptv/vplay/");
			String str1,str2,str3;
			str2="http://api.mmhhw.cn/huan6/letvvip.php?vid=";
			str1=url.substring(url.indexOf("vplay_")+6,url.indexOf(".html"));
			str1=str2+str1;
			return str1;

		}
		url=api7+url;

		return url;
	}
	public void ShowNetImage(String  imgurl,final ImageView iv){

		

		
	}
	
	
	
	
	
}
