package com.TianRu.r18;
import android.app.*;
import android.os.*;
import android.widget.*;
import java.util.*;
import java.io.*;

public class history extends Activity
{
		@Override
		public void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
						String path = this.getCacheDir().toString() + "/picasso-cache/";
						ActionBar actionBar=getActionBar();
						if(actionBar != null){
										actionBar.hide();
								}
						if(!new File(path).exists()){
										setContentView(R.layout.nopic);
										TextView t = findViewById(R.id.nopicText);
										t.setText("你还没有获取过图片哦\n快去获取图片吧！");
										return;
								}
				
				setContentView(R.layout.history);
						Titlelayout title=findViewById(R.id.lishititle);
						title.setIcon(R.drawable.lishi);
						title.hideImageButton(true);
				List<String> l = new ArrayList<String>();
				for(String p:PicList.getpiclist(path)){
						if(p.endsWith(".1")){l.add(p);
						//Toast.makeText(this, p, Toast.LENGTH_SHORT).show();
						}
				}
				PicList p = new PicList(this,(ListView)findViewById(R.id.historyListView),l);
				}
}
