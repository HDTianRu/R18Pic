package com.TianRu.r18;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;
import java.util.*;

public class setting extends Activity{
		public static EditText t1,t2;
		private static TextView p1,p2;
		Handler h;
		@Override
		protected void onCreate(Bundle savedInstanceState){
				super.onCreate(savedInstanceState);
				setContentView(R.layout.setting);
						ActionBar actionBar=getActionBar();
						if(actionBar != null){
										actionBar.hide();
								}
				spUtil.setActivity(this);
						Titlelayout title=findViewById(R.id.settingtitle);
						title.setIcon(R.drawable.shezhi);
						title.hideImageButton(true);
				h=new Handler();
				final LinearLayout hidesetting = findViewById(R.id.hidesetting);
				final LinearLayout savesetting = findViewById(R.id.savesetting);
				hidesetting.setVisibility(spUtil.getBoolean("ishide") ? View.VISIBLE : View.GONE);
				savesetting.setVisibility(spUtil.getBoolean("isprivate") ? View.GONE : View.VISIBLE);
				t1=findViewById(R.id.inputpath);
				p1=findViewById(R.id.path);
				String path = spUtil.getString("savepath");
				p1.setText("当前保存路径:" + ("".equals(path) ? "/storage/emulated/0/Download" : path));
				t2=findViewById(R.id.inputpassword);
				p2=findViewById(R.id.password);
				p2.setText("当前密码:" + spUtil.getString("password"));
				Switch openr18 = findViewById(R.id.openr18);
				Switch savetag = findViewById(R.id.savetag);
				Switch isclose = findViewById(R.id.isclose);
				Switch hide = findViewById(R.id.ishide);
				Switch isprivate = findViewById(R.id.isprivate);
				Switch isclosecheckupdate = findViewById(R.id.isclosecheckupdate);
				openr18.setChecked(spUtil.getBoolean("openr18"));
				savetag.setChecked(spUtil.getBoolean("savetag"));
				isclose.setChecked(spUtil.getBoolean("isclose"));
				hide.setChecked(spUtil.getBoolean("ishide"));
				isprivate.setChecked(spUtil.getBoolean("isprivate"));
				isclosecheckupdate.setChecked(spUtil.getBoolean("isclosecheckupdate"));
				Spinner spinner = findViewById(R.id.picsetting);
				List<String> Spinnerlist = new ArrayList<String>();
				String a = spUtil.getString("picsetting");
				Spinnerlist.add(a.equals("") ? "original(原图)" : a);
				Spinnerlist.add("original(原图)");
				Spinnerlist.add("regular(长宽最大1200)");
				Spinnerlist.add("small(长宽最大540)");
				Spinnerlist.add("thumb(长宽最大250");
				Spinnerlist.add("mini(长宽最大48)");
				ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,Spinnerlist);
				spinner.setAdapter(adapter);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener(){
							public void onItemSelected(AdapterView<?> parent,View view,int position,long id){
									String[] s = new String[] {"original","regular","small","thumb","mini"};
									if("0".equals(position + "")) return;
									spUtil.putString("picsetting",s[position - 1]);
								}
							public void onNothingSelected(AdapterView<?> parent){}
						});
				t1.setOnKeyListener(new View.OnKeyListener() {
							@Override
							public boolean onKey(View v,int keyCode,KeyEvent event){
									if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
											setSavePath();
											return true;
										}
									return false;
								}
						});
				t2.setOnKeyListener(new View.OnKeyListener() {
							@Override
							public boolean onKey(View v,int keyCode,KeyEvent event){
									if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
											setPassword();
											return true;
										}
									return false;
								}
						});
				openr18.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("openr18",isChecked);
								}
						});
				savetag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("savetag",isChecked);
								}
						});
				isclose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("isclose",isChecked);
								}
						});
				hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("ishide",isChecked);
									hidesetting.setVisibility(isChecked ? View.VISIBLE : View.GONE);
								}
						});
				isprivate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("isprivate",isChecked);
									savesetting.setVisibility(isChecked ? View.GONE : View.VISIBLE);
								}
						});
				isclosecheckupdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
							@Override
							public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
									spUtil.putBoolean("isclosecheckupdate",isChecked);
								}
						});
				/*get.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
					getr18();
					toast("2");
					}
					});
					get.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v)
					{
					getr18();
					return true;
					}
					});*/
			}
		public void toast(final String s){
				setting.this.runOnUiThread(new Runnable() {
							@Override
							public void run(){
									Toast.makeText(setting.this,s,Toast.LENGTH_SHORT).show();
								}
						});
			}
		public void setSavePath(){
				String path = t1.getText().toString();
				if(path.equals("")){
						toast("请先在上方输入路径");return;
					}
				if(path.endsWith("/")) path=path.substring(0,path.length() - 1);
				spUtil.putString("savepath",path);
				toast("设置成功，当前保存路径:\n" + path);
				p1.setText("当前保存路径:" + path);
			}
		public void setSavePath(View v){
				setSavePath();
			}
		public void setPassword(){
				String password = t2.getText().toString();
				if(password.equals("")){
						toast("请先在上方输入密码");return;
					}
				if(!password.matches("[0-9]+")){toast("请输入至少一位的纯数字");return;}
				spUtil.putString("password",password);
				toast("设置成功，当前密码:\n" + password);
				p2.setText("当前密码:" + password);
			}
		public void setPassword(View v){
				setPassword();
			}
		public void gotoprivate(View v){
				Intent i = new Intent(this,privateroom.class);
				startActivity(i);
			}
		public void gotohistory(View v){
				startActivity(new Intent(this,history.class));
			}
		public void checkupdate(View v){
				MainActivity.checkupdate(setting.this,h);
			}
	}
