package com.TianRu.r18;

import android.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.view.*;
import android.widget.*;
import com.squareup.picasso.*;
import org.json.*;
import android.view.View.*;

public class MainActivity extends Activity{
				private static EditText t;
				private static StringBuilder isR18 = new StringBuilder(2);
				private static StringBuilder url = new StringBuilder(128);
				private static String savepath;
				private static Bitmap lastbitmap;
				Handler h;
				@Override
				protected void onCreate(Bundle savedInstanceState){
								super.onCreate(savedInstanceState);
								spUtil.setActivity(this);
								/*if(Build.VERSION.SDK_INT >= 19){
												//修改标题栏颜色
												ActionBar ab=getActionBar();
												ab.setBackgroundDrawable(new ColorDrawable(0x1C58EF));
												Window window = getWindow();     
												window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
										}*/
								h = new Handler();
								if(!spUtil.getBoolean("isclosecheckupdate")) checkupdate(this,h);
								if(spUtil.getBoolean("ishide")){
												Intent i = new Intent(this,jisuanqi.class);
												startActivity(i);}
								setContentView(R.layout.main);
								Titlelayout title=findViewById(R.id.maintitle);
								title.setIcon(R.drawable.setu);
								title.hideImageButton(false);
								title.setOnclick(new OnClickListener(){
														@Override
														public void onClick(View v){
																		Intent i = new Intent(MainActivity.this,setting.class);
																		startActivity(i);
																}
												});
								if(spUtil.getBoolean("isprivate")) savepath = this.getFilesDir().toString() + "/private/";
								else{
												savepath = spUtil.getString("savepath");
												if(savepath.equals("")) savepath = "/storage/emulated/0/Download";
										}
								isR18.append("0");
								if(spUtil.getBoolean("openr18")){
												isR18.deleteCharAt(0).append("1");
												Button btn = findViewById(R.id.check);
												btn.setText("关闭r18");
												toast("已自动开启r18");
										}
								ActionBar actionBar=getActionBar();
								if(actionBar != null){
												actionBar.hide();
										}
								t = (EditText) findViewById(R.id.tag);
								TextView info = (TextView) findViewById(R.id.info);
								if(!spUtil.getBoolean("isclose")) info.setText("使用方法:点击“获取”，即可获取涩图\n默认不获取18禁图片\n可点击“开启r18”即可获取\n若觉得麻烦可在设置开启“启动时自动开启r18”\n默认保存图片位置为/storage/emulated/0/Download/\n可在设置(右上角齿轮图标)内更改\n不翻墙也能看，但是卡\n标签用法：\n输入想看的涩图标签，最多3个，空格隔开，如“萝莉 全裸”，输入完成获取即可");
								Button get = (Button) findViewById(R.id.get);
								/*Button check = (Button) findViewById(R.id.check);
									check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
									@Override
									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
									{
									if (isChecked)
									{
									isR18.deleteCharAt(0).append("1");
									}
									else
									{
									isR18.deleteCharAt(0).append("0");
									}
									}
									});
									get.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v)
									{
									getr18();
									toast("2");
									}
									});*/
								get.setOnLongClickListener(new View.OnLongClickListener() {
														@Override
														public boolean onLongClick(View v){

																		return true;
																}
												});
						}

				private long exitTime = 0;

				@Override
				public boolean onKeyDown(int keyCode, KeyEvent event){
								if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
												if((System.currentTimeMillis() - exitTime) > 2000){  
																Toast.makeText(getApplicationContext(),"再按一次返回退出程序",Toast.LENGTH_SHORT).show();                                
																exitTime = System.currentTimeMillis();   
														}
												else{
																finish();
																System.exit(0);
														}
												return true;   
										}
								return super.onKeyDown(keyCode,event);
						}

				@Override
				public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
								super.onRequestPermissionsResult(requestCode,permissions,grantResults);
								switch(requestCode){
												case 1:
														if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//执行代码,这里是已经申请权限成功了,可以不用做处理
																		toast("权限申请成功");
																}
														else{
																		toast("权限申请失败");
																}
														break;
										}
						}

				public void getr18(){
								new Thread(new Runnable() {
														public void run(){
																		final StringBuilder sb=new StringBuilder();
																		try{
																						String taglist = "";
																						String uidlist = "";
																						final String input = "" + t.getText().toString();
																						for(String tag:input.split(" ")){
																										if(tag.startsWith("uid")) uidlist += "&uid=" + tag.substring(3);
																										else taglist += "&tag=" + tag;
																								}
																						String type = spUtil.getString("picsetting");
																						if(type.equals("")){
																								type="original";
																						}
																						final String str=Http.httpget("https://api.lolicon.app/setu/v2?r18=" + isR18.toString() + "&num=1" + taglist + uidlist + "&size=" + type,"");
																						sb.append(str);
																						if(str.equals("")){
																										toast("API好像寄了？？");return;
																								}
																						if(str.equals("{\"error\":\"\",\"data\":[]}")){
																										toast("没有找到该标签的涩图");return;
																								}
																						toast("获取成功，正在加载中");
																						JSONObject data = new JSONObject(str);
																						String err = data.getString("error");
																						if(!err.equals("")){toast(err);return;}
																						JSONArray arr1 = new JSONArray(data.getString("data"));
																						JSONObject json = new JSONObject(arr1.getString(0));
																						JSONObject urls = new JSONObject(json.getString("urls"));
																						final String s=urls.getString(type);
																						url.delete(0,127).append(s);
																						final String r18="pid:" + json.get("pid") + "\n"
																								+ "uid:" + json.get("uid") + "\n" 
																								+ "标题:" + json.get("title") + "\n" 
																								+ "作者:" + json.get("author") + "\n" 
																								+ "标签:" + json.getString("tags").replaceAll("[\\[\\]\"]","").replace(",","、") + "\n" 
																								+ "原图规格:" + json.get("width") + " * " + json.get("height") + "\n" 
																								+ "图片格式:" + json.get("ext") + "\n" 
																								+ "链接:" + s;
																						MainActivity.this.runOnUiThread(new Runnable() {
																												@Override
																												public void run(){
																																ImageView pic = findViewById(R.id.pic);
																																if(spUtil.getBoolean("savetag")) t.setText(input);
																																Picasso.with(getBaseContext())
																																		.load(s)
																																		.noFade()
																																		.error(R.drawable.file_error)
																																		.placeholder(R.drawable.loading)
																																		.into(pic);
																																TextView info = (TextView) findViewById(R.id.info);
																																info.setText(r18);
																														}
																										});
																				}
																		catch(JSONException e){
																						toast(e.toString());
																						MainActivity.this.runOnUiThread(new Runnable(){
																												@Override
																												public void run(){
																														  TextView t=findViewById(R.id.info);
																																t.setText(sb.toString());
																														}
																										});
																				}
																		catch(Exception er){
																						toast(er.toString());
																				}
																}
												}).start();
						}
				public void toast(final String s){
								MainActivity.this.runOnUiThread(new Runnable() {
														@Override
														public void run(){
																		Toast.makeText(MainActivity.this,s,Toast.LENGTH_SHORT).show();
																}
												});
						}
				public boolean verifyStoragePermissions(){
								if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
												if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
//没有权限则申请权限
																ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
																return false;
														}
												else{
//有权限直接执行,不用做处理
														}
												return true;
										}
								else{
//小于6.0，不用申请权限，直接执行
												return true;
										}}
				public static String getAppVersionName(Context context){
								String versionName ="";
								try{
												PackageManager pm = context.getPackageManager();
												PackageInfo p1 = pm.getPackageInfo(context.getPackageName(),0);
												versionName = p1.versionName;
												if(versionName.isEmpty() || versionName.length() <= 0){
																return "";
														}
										}
								catch(PackageManager.NameNotFoundException e){
												e.printStackTrace();
										}
								return versionName;
						}
				public static void checkupdate(final Context act, final Handler handler){
								new Thread(new Runnable(){
														@Override
														public void run(){
																		try{
																						String get = Http.httpget("https://checkupdate.tianru114514.repl.co","");
																						if(get.equals("")) return;
																						final JSONObject data = new JSONObject(get);
																						float nowversion = Float.parseFloat(getAppVersionName(act));
																						float newversion = Float.parseFloat(data.optString("version"));
																						if(newversion > nowversion){
																										handler.postDelayed(new Runnable() {
																																@Override
																																public void run(){
																																				AlertDialog.Builder dialog=new AlertDialog.Builder(act);
																																				dialog.setTitle("有新版本更新！").setMessage("版本:" + data.optString("version") + "\n大小:" + data.optString("size") + "\n更新内容:" + data.optString("msg").replace("\\n","\n"));
																																				dialog.setPositiveButton("更新",new DialogInterface.OnClickListener() {
																																										@Override
																																										public void onClick(DialogInterface dia, int which){
																																														Uri uri = Uri.parse("https://R18Pic.tianru114514.repl.co");
																																														Intent intent = new Intent(Intent.ACTION_VIEW,uri);
																																														act.startActivity(intent);
																																												}
																																								});
																																				dialog.setNeutralButton("取消",null);
																																				dialog.show();
																																		}
																														},0);
																								}
																						else handler.postDelayed(new Runnable() {
																														@Override
																														public void run(){Toast.makeText(act,"当前已经是最新版了",Toast.LENGTH_SHORT).show();}},0);
																				}
																		catch(JSONException e){}
																		catch(NumberFormatException e){}
																}
												}).start();
						}
				public void get(View v){
								getr18();
						}
				public void check(View v){
								if(isR18.toString().equals("0")){
												isR18.deleteCharAt(0).append("1");
												MainActivity.this.runOnUiThread(new Runnable() {
																		@Override
																		public void run(){
																						Button btn = findViewById(R.id.check);
																						btn.setText("关闭r18");
																				}
																});
												toast("已开启r18");
										}
								else{
												isR18.deleteCharAt(0).append("0");
												MainActivity.this.runOnUiThread(new Runnable() {
																		@Override
																		public void run(){
																						Button btn = findViewById(R.id.check);
																						btn.setText("开启r18");
																				}
																});
												toast("已关闭r18");
										}
						}
				public void save(View v){
								if(!verifyStoragePermissions()){
												toast("请先同意存储权限");
												return;
										}
								toast("保存中");
								new Thread(new Runnable() {
														public void run(){
																		if(url.toString().equals("")){
																						toast("请先获取");return;
																				}
																		toast(Http.downloadFile(url.toString(),savepath));
																}
												}).start();
						}
		}
