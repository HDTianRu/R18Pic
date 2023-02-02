package com.TianRu.r18;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;

public class inputpassword extends Activity
{
	EditText e;
	String password;
		protected void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				spUtil.setActivity(this);
				setContentView(R.layout.inputpassword);
				e = findViewById(R.id.inputpassword);
				password = spUtil.getString("privatepassword");
				}
				public void check(View v){
					if(e.getText().toString().equals(password)) finish();
						else Toast.makeText(this, "密码错误", 500).show();
				}
}
