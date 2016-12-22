package cn.liuyin.x5webmini.myunit;
import android.content.Context;
import android.content.res.Resources;
import cn.liuyin.x5webmini.R;

public class ADFilterTool
{

	

	
	public static boolean hasAD(Context context,String url){
		Resources res=context.getResources();
		String[] adUrls=res.getStringArray(R.array.adBlockUrl);
		for(String adUrl:adUrls){
			if(url.contains(adUrl))
			{return true;}
		}
		return false;
	}
	
}
