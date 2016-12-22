package cn.liuyin.x5webmini.mywebview;
import com.tencent.smtt.sdk.WebViewClient;
import android.content.Context;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import cn.liuyin.x5webmini.myunit.ADFilterTool;
import com.tencent.smtt.sdk.WebView;

public class MyWebViewClient extends WebViewClient
{
	private Context context;
	private String homeurl="file:///";
	public MyWebViewClient(Context context){
		this.context=context;
	}
	@Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        url = url.toLowerCase();
        if(!url.contains(homeurl)){
            if (!ADFilterTool.hasAD(context, url)) {
                return super.shouldInterceptRequest(view, url);
            }else{
                return new WebResourceResponse(null,null,null);
            }
        }else{
            return super.shouldInterceptRequest(view, url);
        }


    }
}
