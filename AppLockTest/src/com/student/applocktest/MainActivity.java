package com.student.applocktest;

import mylock.MyLockView;
import mylock.MyLockView.lockListener;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity implements lockListener {
	
	private MyLockView lockView;
	private String passwordString = "";
	
	@Override
	protected void onCreate ( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		lockView = ( MyLockView ) findViewById( R.id.lockView );
		lockView.setLockListener( this );
	}
	
	@Override
	public void getStringPassword ( String password ) {
		// TODO 自动生成的方法存根
		passwordString = password;
		Toast.makeText( this , password , Toast.LENGTH_SHORT ).show();
	}
	
	@Override
	public boolean isPassword ( ) {
		// TODO 自动生成的方法存根
		if ( passwordString.equals( "123456" ) ) {
			return true;
		}
		return false;
	}
}
