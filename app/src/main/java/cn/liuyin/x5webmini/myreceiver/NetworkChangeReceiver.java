package cn.liuyin.x5webmini.myreceiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO: Implement this method
		ConnectivityManager connectionManager = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			Toast.makeText(context, "网络可用:)",
						   Toast.LENGTH_SHORT).show();
			

		} else {
			Toast.makeText(context, "网络不可用:(",
						   Toast.LENGTH_SHORT).show();
			
		}
	}

}
