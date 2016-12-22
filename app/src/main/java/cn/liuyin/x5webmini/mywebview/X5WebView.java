package cn.liuyin.x5webmini.mywebview;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.tencent.smtt.sdk.WebView;
import android.graphics.Paint;
import com.tencent.smtt.sdk.QbSdk;
import android.os.Build;

public class X5WebView extends WebView
{
	private X5WebView xwebview;
	public Context context;
	private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
	public X5WebView(Context context)
	{
		super(context);
		this.context=context;
		setBackgroundColor(85621);
//		xwebview.setWebChromeClient(new MyWebChromeClient(context));
		xwebview.setWebViewClient(new MyWebViewClient(context));
	}
	public X5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public X5WebView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context,attrs,defStyleAttr);
	}
	public X5WebView (Context context, 
	AttributeSet attrs, 
	int defStyleAttr, 
	int defStyleRes){
		super(context,attrs,defStyleAttr);
	}
	private ISmartScrollChangedListener mSmartScrollChangedListener;

    /** 定义监听接口 */
    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();
        void onScrolledToTop();
    }
	@Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (android.os.Build.VERSION.SDK_INT < 9) {  // API 9及之后走onOverScrolled方法监听
            if (getScrollY() == 0) {    // 小心踩坑1: 这里不能是getScrollY() <= 0
                isScrolledToTop = true;
                isScrolledToBottom = false;
            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
                // 小心踩坑2: 这里不能是 >= 
			 // 小心踩坑3（可能忽视的细节2）：这里最容易忽视的就是ScrollView上下的padding　
                isScrolledToBottom = true;
                isScrolledToTop = false;
            } else {
                isScrolledToTop = false;
                isScrolledToBottom = false;
            }
            notifyScrollChangedListeners();
        }
      
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }
	

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime)
	{
		// TODO: Implement this method
		//return super.drawChild(canvas, child, drawingTime);
		boolean ret = super.drawChild(canvas, child, drawingTime);
//		canvas.save();
//		Paint paint = new Paint();
//		paint.setColor(0x7fff0000);
//		paint.setTextSize(24.f);
//		paint.setAntiAlias(true);
//		if (getX5WebViewExtension() != null) {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:" + android.os.Process.myPid(), 100, 50, paint);
//			canvas.drawText("X5  Core:" + QbSdk.getTbsVersion(this.getContext()), 100, 100, paint);
//		} else {
//			canvas.drawText(this.getContext().getPackageName() + "-pid:" + android.os.Process.myPid(), 100, 50, paint);
//			canvas.drawText("Sys Core", 100, 100, paint);
//		}
//		canvas.drawText(Build.MANUFACTURER, 100, 150, paint);
//		canvas.drawText(Build.MODEL, 100, 200, paint);
//		canvas.restore();
		return ret;
	}

	
	
	
	
	
	
} 
