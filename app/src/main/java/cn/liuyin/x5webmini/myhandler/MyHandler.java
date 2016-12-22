package cn.liuyin.x5webmini.myhandler;
import android.os.Handler;
import android.content.Context;
import android.os.Message;
import cn.liuyin.x5webmini.myunit.MyFunction;
import android.widget.Toast;

public class MyHandler extends Handler
{
	public int RECEIVE_ERROR=0;
	public int WEBPAGE_SOUCE=1;
	MyFunction mf=new MyFunction();
	Context context;
	public MyHandler(Context context){
		this.context=context;
	}
	public MyHandler(){
		
	}
	// 重写handleMessage()方法，此方法在UI线程运行
	@Override
	public void handleMessage(Message msg)  {
		switch (msg.what) {
				// 如果成功，则显示从网络获取到字符串
			case 1:
				String saveString=(String)msg.obj;
//					
				mf.ShowDiaog(context, (String)msg.obj);
				Toast.makeText(context,"接收数据成功。-_^。",Toast.LENGTH_LONG).show();  
				break;
			case 2:
				
				break;
				// 否则提示失败
			case 0:
				//tv.setText("获取失败");
				Toast.makeText(context,"获取数据失败。一_一|",Toast.LENGTH_LONG).show();  

				break;
		}
	}
}
