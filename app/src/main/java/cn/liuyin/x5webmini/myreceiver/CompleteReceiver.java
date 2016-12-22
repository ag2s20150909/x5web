package cn.liuyin.x5webmini.myreceiver;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import android.app.DownloadManager;

public class CompleteReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO: Implement this method
		long competeDownoadId=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
		//ShowNotification("下载完成^ε^ ^ε^");
		Toast.makeText(context,"下载完成"+"\n"+competeDownoadId,Toast.LENGTH_LONG).show();
	}

}
