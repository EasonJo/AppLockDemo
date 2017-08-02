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
	//�ж��ߵ�״̬
	private static boolean isLineState = true;
	//�жϵ��Ƿ�ʵ������
	private static boolean isInitPoint = false;
	//�ж���ָ�Ƿ��뿪��Ļ
	private static boolean isFinish = false;
	//�ж���ָ�����Ļʱ�Ƿ�ѡ���˾Ź����еĵ�
	private static boolean isSelect = false;
	// ����MyPoint������
	private MyPoint[][] points = new MyPoint[3][3];
	// ������Ļ�Ŀ�͸�
	private int screenHeight;
	private int screenWidth;
	// �������ߵ�ͼƬ�İ뾶
	private float pointRandius;
	// �����ߵ�ͼƬ�ĸߣ����ǰ뾶��
	private float lineHeight;
	// ��������ƶ���x��y����
	private float moveX, moveY;
	// ������Ļ�ϵĿ�͸ߵ�ƫ����
	private int screenHeightOffSet = 0;
	private int screenWidthOffSet = 0;
	// ����һ������
	private Paint paint = new Paint( Paint.ANTI_ALIAS_FLAG );
	// ������ԴͼƬ
	private Bitmap bitmap_normal;
	private Bitmap bitmap_press;
	private Bitmap bitmap_error;
	private Bitmap bitmap_line_normal;
	private Bitmap bitmap_line_error;
	// ����һ������
	private Matrix matrix = new Matrix();
	// ����MyPoint���б�
	private List< MyPoint > pointList = new ArrayList< MyPoint >();
	// ʵ��������
	private MyPoint mousePoint = new MyPoint();
	// �û�ȡ��activity�д������������ַ���
	private String password = "";
	
	public MyLockView( Context context, AttributeSet attrs, int defStyleAttr ) {
		super( context , attrs , defStyleAttr );
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public MyLockView( Context context, AttributeSet attrs ) {
		super( context , attrs );
		// TODO �Զ����ɵĹ��캯�����
	}
	
	public MyLockView( Context context ) {
		super( context );
		// TODO �Զ����ɵĹ��캯�����
	}
	
	/**
	 * ����ͻ���
	 */
	@Override
	protected void onDraw ( Canvas canvas ) {
		// TODO �Զ����ɵķ������
		super.onDraw( canvas );
		if ( !isInitPoint ) {
			initPoint();
		}
		/**
		 * ��ʼ����
		 */
		canvasPoint( canvas );
		
		/**
		 * ��ʼ����
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
	 * ��ָ����ֻ���Ļ
	 */
	@Override
	public boolean onTouchEvent ( MotionEvent event ) {
		// TODO �Զ����ɵķ������
		moveX = event.getX();
		moveY = event.getY();
		// �����ƶ��������
		mousePoint.setX( moveX );
		mousePoint.setY( moveY );
		MyPoint mPoint = null;
		switch ( event.getAction() ) {
			case MotionEvent.ACTION_DOWN:
				isLineState = true;
				isFinish = false;
				// ÿ�ε��ʱ�ͻὫpointList��Ԫ������ת��������״̬
				for ( int i = 0; i < pointList.size(); i++ ) {
					pointList.get( i ).setState( MyPoint.BITMAP_NORMAL );
				}
				// ��pointList�е�Ԫ�������
				pointList.clear();
				// �ж��Ƿ�����˾Ź����еĵ�
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
				// �涨����Ҫ��5���㱻���߲��п�������ȷ
				// ������������Ǵ����
				if ( pointList.size() >= 5 ) {// ��ȷ���
					for ( int j = 0; j < pointList.size(); j++ ) {
						password += pointList.get( j ).getIndex();
					}
					//�����ߺ�õ������봫��activity
					mListener.getStringPassword( password );
					password = "";
					//����activity�жϴ������Ƿ���ȷ
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
				}// �������
				else if ( pointList.size() < 5 && pointList.size() > 1 ) {
					for ( int i = 0; i < pointList.size(); i++ ) {
						pointList.get( i ).setState( MyPoint.BITMAP_ERROE );
					}
					isLineState = false;
				}// ���ֻ��һ���㱻����ʱΪ�������
				else if ( pointList.size() == 1 ) {
					for ( int i = 0; i < pointList.size(); i++ ) {
						pointList.get( i ).setState( MyPoint.BITMAP_NORMAL );
					}
				}
				break;
		
		}
		// ��mPoint��ӵ�pointList��
		if ( isSelect && mPoint != null ) {
			if ( mPoint.getState() == MyPoint.BITMAP_NORMAL ) {
				mPoint.setState( MyPoint.BITMAP_PRESS );
				pointList.add( mPoint );
			}
		}
		// ˢ��ҳ��
		postInvalidate();
		return true;
	}
	
	/**
	 * �жϾŹ����е�ĳ�����Ƿ񱻵����ˣ�����ĳ�����ܷ�����
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
	 * ����
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
	 * ����
	 * 
	 * @param canvas
	 */
	private void canvasPoint ( Canvas canvas ) {
		// TODO �Զ����ɵķ������
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
	 * ʵ�����Ź��������е�����е���ԴͼƬ
	 */
	private void initPoint ( ) {
		// TODO �Զ����ɵķ������
		screenHeight = getHeight();
		screenWidth = getWidth();
		if ( screenHeight > screenWidth ) {
			// ��ȡy���ϵ�ƫ����
			screenHeightOffSet = ( screenHeight - screenWidth ) / 2;
			// ����Ļ�ĸ����ó������ȣ�Ŀ����Ϊ��new MyPoint(x,y)ʱ�������
			screenHeight = screenWidth;
		}
		else {
			// ��ȡx���ϵ�ƫ����
			screenWidthOffSet = ( screenWidth - screenHeight ) / 2;
			// ����Ļ�Ŀ����ó������ȣ�Ŀ����Ϊ��new MyPoint(x,y)ʱ�������
			screenWidth = screenHeight;
		}
		
		/**
		 * ʵ�������е���ԴͼƬ
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
		 * ��ʼʵ�����Ź����е�
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
		 * ���þŹ����еĸ���index
		 */
		int index = 1;
		for ( int i = 0; i < points.length; i++ ) {
			for ( int j = 0; j < points[i].length; j++ ) {
				points[i][j].setIndex( index + "" );
				// ��û���κβ����������Ĭ�J���״̬
				points[i][j].setState( MyPoint.BITMAP_NORMAL );
				index++;
			}
		}
		
		/**
		 * ��isInitPoint����Ϊtrue
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
