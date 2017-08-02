package mylock;

public class MyPoint {
	//����
	public static int	BITMAP_NORMAL	= 0;
	//����
	public static int	BITMAP_ERROE	= 1;
	//����
	public static int	BITMAP_PRESS	= 2;
	//�Ź����еĵ���±꣨����ÿ�������һ��ֵ��
	private String		index;
	//���״̬
	private int			state;
	//�������
	private float		x;
	private float		y;
	
	public MyPoint() {
		super();
		// TODO �Զ����ɵĹ��캯�����
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
	 *            Ҫ���õ� index
	 */
	public void setIndex( String index ) {
		this.index = index;
	}
	
	/**
	 * @param state
	 *            Ҫ���õ� state
	 */
	public void setState( int state ) {
		this.state = state;
	}
	
	/**
	 * @param x
	 *            Ҫ���õ� x
	 */
	public void setX( float x ) {
		this.x = x;
	}
	
	/**
	 * @param y
	 *            Ҫ���õ� y
	 */
	public void setY( float y ) {
		this.y = y;
	}
	
	/**
	 * �ж���Ļ�ϵľŹ����еĵ��ܷ���Խ�������
	 * 
	 * @param a
	 * @param moveX
	 * @param moveY
	 * @param r
	 * @return ������
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
