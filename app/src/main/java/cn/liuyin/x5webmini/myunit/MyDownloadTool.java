package cn.liuyin.x5webmini.myunit;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import android.os.Message;
import android.app.DownloadManager;
import android.net.Uri;
import android.content.Context;

public class MyDownloadTool
{

	private int fileSize;
	/**
	 * 文件下载
	 * @param url：文件的下载地址
	 * @param path：文件保存到本地的地址
	 * @throws IOException
	 */
	public void down_file(Context context,String url,String path) throws IOException{  
		//下载函数     
		String filename=url.substring(url.lastIndexOf("/") + 1);
		//获取文件名  
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri); 
		request.addRequestHeader("User-Agent","X5WEB"); // 添加一个Http请求报头
		request.setDestinationInExternalPublicDir("X5Web", filename);
        //request.setMimeType("");//设置文件的mineType
		long download_id = downloadManager.enqueue(request);
		//返回值download_id是一个比较重要的参数，主要用来查询

	}  
	public void down_file(Context context,String url,String path,String ua) throws IOException{  
		//下载函数     
		String filename=url.substring(url.lastIndexOf("/") + 1);
		//获取文件名  
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri); 
		request.addRequestHeader("User-Agent",ua); // 添加一个Http请求报头
		request.setDestinationInExternalPublicDir("X5Web", filename);
        //request.setMimeType("");//设置文件的mineType
		long download_id = downloadManager.enqueue(request);
		//返回值download_id是一个比较重要的参数，主要用来查询

	}  
	public void down_file(Context context,String url,String path,String ua,String minetype) throws IOException{  
		//下载函数     
		String filename=url.substring(url.lastIndexOf("/") + 1);
		//获取文件名  
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
		Uri uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(uri); 
		request.addRequestHeader("User-Agent","X5WEB"); // 添加一个Http请求报头
		request.setDestinationInExternalPublicDir("X5Web", filename);
        request.setMimeType(minetype);//设置文件的mineType
		long download_id = downloadManager.enqueue(request);
		//返回值download_id是一个比较重要的参数，主要用来查询

	}  
	

	//在线程中向Handler发送文件的下载量，进行UI界面的更新
	
}
