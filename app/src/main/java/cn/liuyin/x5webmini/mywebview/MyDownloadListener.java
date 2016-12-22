package cn.liuyin.x5webmini.mywebview;
import com.tencent.smtt.sdk.DownloadListener;
import cn.liuyin.x5webmini.myunit.MyDownloadTool;
import android.app.AlertDialog;
import cn.liuyin.x5webmini.R;
import java.io.IOException;
import android.content.DialogInterface;

public class MyDownloadListener implements DownloadListener
{

	MyDownloadTool mdt=new MyDownloadTool();
	@Override
	public void onDownloadStart(final String p1, String p2, String p3, String p4, long p5)
	{
		// TODO: Implement this method
		new AlertDialog.Builder(null)
			.setIcon(R.drawable.ic_launcher)
			.setTitle("提示")
			.setMessage("确定要下载该文件吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog , int which)
				{



					try
					{
						mdt.down_file(null, p1, "/sdcard/ADM/");
					}
					catch (IOException e)
					{}


				}
			})
			.setNegativeButton("取消", null)
			.show();
	}

}
