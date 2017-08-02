package mylock;

public class MyPoint {
	//正常
	public static int	BITMAP_NORMAL	= 0;
	//错误
	public static int	BITMAP_ERROE	= 1;
	//按下
	public static int	BITMAP_PRESS	= 2;
	//九宫格中的点的下标（即是每个点代表一个值）
	private String		index;
	//点的状态
	private int			state;
	//点的坐标
	private float		x;
	private float		y;
	
	public MyPoint() {
		super();
		// TODO 自动生成的构造函数存根
	}
	
	public MyPoint( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * @return index
	 */
	public String getIndex() {
		return index;
	}
	
	/**
	 * @return state
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * @return x
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * @return y
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * @param index
	 *            要设置的 index
	 */
	public void setIndex( String index ) {
		this.index = index;
	}
	
	/**
	 * @param state
	 *            要设置的 state
	 */
	public void setState( int state ) {
		this.state = state;
	}
	
	/**
	 * @param x
	 *            要设置的 x
	 */
	public void setX( float x ) {
		this.x = x;
	}
	
	/**
	 * @param y
	 *            要设置的 y
	 */
	public void setY( float y ) {
		this.y = y;
	}
	
	/**
	 * 判断屏幕上的九宫格中的点能否可以进行连线
	 * 
	 * @param a
	 * @param moveX
	 * @param moveY
	 * @param r
	 * @return 布尔型
	 */	
	public boolean isWith( MyPoint a, float moveX, float moveY, float r ) {
		float result = ( float ) Math.sqrt( ( a.getX() - moveX )
				* ( a.getX() - moveX ) + ( a.getY() - moveY )
				* ( a.getY() - moveY ) );
		if ( result < 5 * r / 4 ) {
			return true;
		}
		return false;
		
	}
	
}
