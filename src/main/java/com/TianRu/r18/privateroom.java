package com.TianRu.r18;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;

public class privateroom extends Activity{

		private String picpath;

		@Override
		public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
				spUtil.setActivity(this);
				
						ActionBar actionBar=getActionBar();
						if(actionBar != null){
						actionBar.hide();
						}
				picpath=this.getFilesDir().toString() + "/private/";
				if(!spUtil.getString("privatepassword").equals("")) startActivity(new Intent(this,inputpassword.class));
				if(!new File(picpath).exists()){
						setContentView(R.layout.nopic);
						TextView t = findViewById(R.id.nopicText);
						t.setText("你还没有往这里保存图片哦\n快去保存到私密空间吧！");
					}
				else{
						setContentView(R.layout.privateroom);
								Titlelayout title=findViewById(R.id.simititle);
								title.setIcon(R.drawable.simi);
								title.hideImageButton(true);
						PicList p = new PicList(this,(ListView)findViewById(R.id.list),picpath);
					}
			}

		public void setpassword(View v){
    final EditText editText = new EditText(privateroom.this);
    AlertDialog.Builder inputDialog = 
					new AlertDialog.Builder(privateroom.this);
    inputDialog.setTitle("请输入密码").setView(editText);
    inputDialog.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,int which){
									Toast.makeText(privateroom.this,
																								"设置成功！！！", 
																								Toast.LENGTH_SHORT).show();
									spUtil.putString("privatepassword",editText.getText().toString());
        }
						}).show();
			}

	}
