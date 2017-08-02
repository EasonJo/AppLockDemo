package mylock;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyLockView extends View {
	//判断线的状态
	private static boolean isLineState = true;
	//判断点是否被实例化了
	private static boolean isInitPoint = false;
	//判断手指是否离开屏幕
	private static boolean isFinish = false;
	//判断手指点击屏幕时是否选中了九宫格中的点
	private static boolean isSelect = false;
	// 创建MyPoint的数组
	private MyPoint[][] points = new MyPoint[3][3];
	// 声明屏幕的宽和高
	private int screenHeight;
	private int screenWidth;
	// 声明点线的图片的半径
	private float pointRandius;
	// 声明线的图片的高（即是半径）
	private float lineHeight;
	// 声明鼠标移动的x，y坐标
	private float moveX, moveY;
	// 声明屏幕上的宽和高的偏移量
	private int screenHeightOffSet = 0;
	private int screenWidthOffSet = 0;
	// 创建一个画笔
	private Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
	// 声明资源图片
	private Bitmap bitmap_normal;
	private Bitmap bitmap_press;
	private Bitmap bitmap_error;
	private Bitmap bitmap_line_normal;
	private Bitmap bitmap_line_error;
	// 创建一个矩阵
	private Matrix matrix = new Matrix();
	// 创建MyPoint的列表
	private List< MyPoint > pointList = new ArrayList< MyPoint >();
	// 实例化鼠标点
	private MyPoint mousePoint = new MyPoint();
	// 用获取从activity中传过来的密码字符串
	private String password = "";
	
	public MyLockView( Context context, AttributeSet attrs, int defStyleAttr ) {
		super( context , attrs , defStyleAttr );
		// TODO 自动生成的构造函数存根
	}
	
	public MyLockView( Context context, AttributeSet attrs ) {
		super( context , attrs );
		// TODO 自动生成的构造函数存根
	}
	
	public MyLockView( Context context ) {
		super( context );
		// TODO 自动生成的构造函数存根
	}
	
	/**
	 * 画点和画线
	 */
	@Override
	protected void onDraw ( Canvas canvas ) {
		// TODO 自动生成的方法存根
		super.onDraw( canvas );
		if ( !isInitPoint ) {
			initPoint();
		}
		/**
		 * 开始画点
		 */
		canvasPoint( canvas );
		
		/**
		 * 开始画线
		 */
		if ( pointList.size() > 0 ) {
			MyPoint b = null;
			MyPoint a = pointList.get( 0 );
			for ( int i = 1; i < pointList.size(); i++ ) {
				b = pointList.get( i );
				canvasLine( a , b , canvas );
				a = b;
			}
			if ( !isFinish ) {
				canvasLine( a , mousePoint , canvas );
			}
		}
	}
	
	/**
	 * 手指点击手机屏幕
	 */
	@Override
	public boolean onTouchEvent ( MotionEvent event ) {
		// TODO 自动生成的方法存根
		moveX = event.getX();
		moveY = event.getY();
		// 设置移动点的坐标
		mousePoint.setX( moveX );
		mousePoint.setY( moveY );
		MyPoint mPoint = null;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				isLineState = true;
				isFinish = false;
				// 每次点击时就会将pointList中元素设置转化成正常状态
				for ( int i = 0; i < pointList.size(); i++ ) {
					pointList.get( i ).setState( MyPoint.BITMAP_NORMAL );
				}
				// 将pointList中的元素清除掉
				pointList.clear();
				// 判断是否点中了九宫格中的点
				mPoint = getIsSelectedPoint( moveX , moveY );
				if ( mPoint != null ) {
					isSelect = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if ( isSelect == true ) {
					mPoint = getIsSelectedPoint( moveX , moveY );
				}
				
				break;
			case MotionEvent.ACTION_UP:
				isFinish = true;
				isSelect = false;
				// 规定至少要有5个点被连线才有可能是正确
				// 其他种情况都是错误的
				if ( pointList.size() >= 5 ) {// 正确情况
					for ( int j = 0; j < pointList.size(); j++ ) {
						password += pointList.get( j ).getIndex();
					}
					//将连线后得到的密码传给activity
					mListener.getStringPassword( password );
					password = "";
					//经过activity判断传过来是否正确
					if ( mListener.isPassword() ) {
						for ( int i = 0; i < pointList.size(); i++ ) {
							pointList.get( i ).setState( MyPoint.BITMAP_PRESS );
						}
					}
					else {
						for ( int i = 0; i < pointList.size(); i++ ) {
							pointList.get( i ).setState( MyPoint.BITMAP_ERROE );
						}
						isLineState = false;
					}
				}// 错误情况
				else if ( pointList.size() < 5 && pointList.size() > 1 ) {
					for ( int i = 0; i < pointList.size(); i++ ) {
						pointList.get( i ).setState( MyPoint.BITMAP_ERROE );
					}
					isLineState = false;
				}// 如果只有一个点被点中时为正常情况
				else if ( pointList.size() == 1 ) {
					for ( int i = 0; i < pointList.size(); i++ ) {
						pointList.get( i ).setState( MyPoint.BITMAP_NORMAL );
					}
				}
				break;
		
		}
		// 将mPoint添加到pointList中
		if ( isSelect && mPoint != null ) {
			if ( mPoint.getState() == MyPoint.BITMAP_NORMAL ) {
				mPoint.setState( MyPoint.BITMAP_PRESS );
				pointList.add( mPoint );
			}
		}
		// 刷新页面
		postInvalidate();
		return true;
	}
	
	/**
	 * 判断九宫格中的某个点是否被点中了，或者某个点能否被连线
	 * 
	 * @param moveX
	 * @param moveY
	 * @return
	 */
	private MyPoint getIsSelectedPoint ( float moveX , float moveY ) {
		MyPoint myPoint = null;
		for ( int i = 0; i < points.length; i++ ) {
			for ( int j = 0; j < points[i].length; j++ ) {
				if ( points[i][j].isWith( points[i][j] , moveX , moveY ,
						pointRandius ) ) {
					myPoint = points[i][j];
				}
			}
		}
		
		return myPoint;
	}
	
	/**
	 * 画线
	 * 
	 * @param a
	 * @param b
	 * @param canvas
	 */
	private void canvasLine ( MyPoint a , MyPoint b , Canvas canvas ) {
		float abInstance = ( float ) Math.sqrt( ( a.getX() - b.getX() )
				* ( a.getX() - b.getX() ) + ( a.getY() - b.getY() )
				* ( a.getY() - b.getY() ) );
		canvas.rotate( RotateDegrees.getDegrees( a , b ) , a.getX() , a.getY() );
		matrix.setScale( abInstance / lineHeight , 1 );
		matrix.postTranslate( a.getX() , a.getY() );
		if ( isLineState ) {
			canvas.drawBitmap( bitmap_line_normal , matrix , paint );
		}
		else {
			canvas.drawBitmap( bitmap_line_error , matrix , paint );
		}
		canvas.rotate( -RotateDegrees.getDegrees( a , b ) , a.getX() , a.getY() );
	}
	
	/**
	 * 画点
	 * 
	 * @param canvas
	 */
	private void canvasPoint ( Canvas canvas ) {
		// TODO 自动生成的方法存根
		for ( int i = 0; i < points.length; i++ ) {
			for ( int j = 0; j < points[i].length; j++ ) {
				if ( points[i][j].getState() == MyPoint.BITMAP_NORMAL ) {
					canvas.drawBitmap( bitmap_normal , points[i][j].getX()
							- pointRandius ,
							points[i][j].getY() - pointRandius , paint );
				}
				else if ( points[i][j].getState() == MyPoint.BITMAP_PRESS ) {
					canvas.drawBitmap( bitmap_press , points[i][j].getX()
							- pointRandius ,
							points[i][j].getY() - pointRandius , paint );
				}
				else {
					canvas.drawBitmap( bitmap_error , points[i][j].getX()
							- pointRandius ,
							points[i][j].getY() - pointRandius , paint );
				}
			}
		}
	}
	
	/**
	 * 实例化九宫格中所有点和所有的资源图片
	 */
	private void initPoint ( ) {
		// TODO 自动生成的方法存根
		screenHeight = getHeight();
		screenWidth = getWidth();
		if ( screenHeight > screenWidth ) {
			// 获取y轴上的偏移量
			screenHeightOffSet = ( screenHeight - screenWidth ) / 2;
			// 将屏幕的高设置成与宽相等，目的是为了new MyPoint(x,y)时方便操作
			screenHeight = screenWidth;
		}
		else {
			// 获取x轴上的偏移量
			screenWidthOffSet = ( screenWidth - screenHeight ) / 2;
			// 将屏幕的宽设置成与高相等，目的是为了new MyPoint(x,y)时方便操作
			screenWidth = screenHeight;
		}
		
		/**
		 * 实例化所有的资源图片
		 */
		bitmap_error = BitmapFactory.decodeResource( getResources() ,
				com.student.applocktest.R.drawable.error );
		bitmap_normal = BitmapFactory.decodeResource( getResources() ,
				com.student.applocktest.R.drawable.nomal );
		bitmap_press = BitmapFactory.decodeResource( getResources() ,
				com.student.applocktest.R.drawable.press );
		bitmap_line_error = BitmapFactory.decodeResource( getResources() ,
				com.student.applocktest.R.drawable.error_line );
		bitmap_line_normal = BitmapFactory.decodeResource( getResources() ,
				com.student.applocktest.R.drawable.normal_line );
		
		pointRandius = bitmap_normal.getWidth() / 2;
		lineHeight = bitmap_line_normal.getHeight();
		
		/**
		 * 开始实例化九宫格中点
		 */
		points[0][0] = new MyPoint( screenWidthOffSet + screenWidth / 4 ,
				screenHeightOffSet + screenHeight / 4 );
		points[0][1] = new MyPoint( screenWidthOffSet + screenWidth / 2 ,
				screenHeightOffSet + screenHeight / 4 );
		points[0][2] = new MyPoint( screenWidthOffSet + screenWidth * 3 / 4 ,
				screenHeightOffSet + screenHeight / 4 );
		
		points[1][0] = new MyPoint( screenWidthOffSet + screenWidth / 4 ,
				screenHeightOffSet + screenHeight / 2 );
		points[1][1] = new MyPoint( screenWidthOffSet + screenWidth / 2 ,
				screenHeightOffSet + screenHeight / 2 );
		points[1][2] = new MyPoint( screenWidthOffSet + screenWidth * 3 / 4 ,
				screenHeightOffSet + screenHeight / 2 );
		
		points[2][0] = new MyPoint( screenWidthOffSet + screenWidth / 4 ,
				screenHeightOffSet + screenHeight * 3 / 4 );
		points[2][1] = new MyPoint( screenWidthOffSet + screenWidth / 2 ,
				screenHeightOffSet + screenHeight * 3 / 4 );
		points[2][2] = new MyPoint( screenWidthOffSet + screenWidth * 3 / 4 ,
				screenHeightOffSet + screenHeight * 3 / 4 );
		
		/**
		 * 设置九宫格中的各个index
		 */
		int index = 1;
		for ( int i = 0; i < points.length; i++ ) {
			for ( int j = 0; j < points[i].length; j++ ) {
				points[i][j].setIndex( index + "" );
				// 在没有任何操作的情况下默認点的状态
				points[i][j].setState( MyPoint.BITMAP_NORMAL );
				index++;
			}
		}
		
		/**
		 * 将isInitPoint设置为true
		 */
		isInitPoint = true;
	}
	
	public interface lockListener {
		public void getStringPassword ( String password );
		
		public boolean isPassword ( );
	}
	
	private lockListener mListener;
	
	public void setLockListener ( lockListener listener ) {
		this.mListener = listener;
	}
	
}
