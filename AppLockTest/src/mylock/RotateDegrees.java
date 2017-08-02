package mylock;

public class RotateDegrees {
	public static float getDegrees ( MyPoint a , MyPoint b ) {
		float degrees = 0;
		float ax = a.getX();
		float ay = a.getY();
		float bx = b.getX();
		float by = b.getY();
		if ( ax == bx ) {
			if ( by > ay ) {
				degrees = 90;
			}
			else {
				degrees = 270;
			}
		}
		else if ( by == ay ) {
			if ( ax > bx ) {
				degrees = 180;
			}
			else {
				degrees = 0;
			}
		}
		else {
			if ( ax > bx ) {
				if ( ay > by ) {// ��������
					degrees = 180 + ( float ) ( Math.atan2( ay - by , ax - bx ) * 180 / Math.PI );
				}
				else {// �ڶ�����
					degrees = 180 - ( float ) ( Math.atan2( by - ay , ax - bx ) * 180 / Math.PI );
				}
			}
			else {
				if ( ay > by ) {// ��������
					degrees = 360 - ( float ) ( Math.atan2( ay - by , bx - ax ) * 180 / Math.PI );
				}
				else {// ��һ����
					degrees = ( float ) ( Math.atan2( by - ay , bx - ax ) * 180 / Math.PI );
				}
			}
		}
		return degrees;
		
	}
}
