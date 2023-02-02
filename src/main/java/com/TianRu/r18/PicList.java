package com.TianRu.r18;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import android.widget.SimpleAdapter.*;
import java.io.*;
import java.util.*;

import android.view.View.OnClickListener;

public class PicList{
	 private Context context;
		// ListView的Adapter
		private SimpleAdapter mSimpleAdapter;
		private ListView lv;
		private Button bt;
		private LinearLayout pg;
		private List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
		private List<String> all;
// ListView底部View
		private View moreView;
		private Handler handler;
// 设置一个最大的数据条数，超过即不再加载
		private int MaxDateNum;
		private Bitmap error;
		public PicList(Context activity,ListView v,String path){
				this(activity,v,getpiclist(path));
			}
		public PicList(Context activity,ListView v,List<String> l){
			 this.context=activity;
				this.lv=v;
				this.all=l;
				int temp=this.all.size();
				this.MaxDateNum="0".equals(temp % 3 + "") ? temp / 3 : (((temp - (temp % 3)) / 3) + 1);

				this.handler=new Handler();

				this.error=BitmapFactory.decodeResource(this.context.getResources(),R.drawable.file_error);

				/*DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					Width=dm.widthPixels;
					Height=dm.heightPixels;*/

				this.list.addAll(getlist(getbitmap(all.subList(0,all.size() < 12 ? all.size() : 12))));
				this.mSimpleAdapter=new SimpleAdapter(
					context,
					list,
					R.layout.pic,
					new String[] {"pic1","pic2","pic3"},
					new int[] {R.id.pic1,R.id.pic2,R.id.pic3}
				);
				this.mSimpleAdapter.setViewBinder(new ViewBinder() {
							public boolean setViewValue(
								View view,
								Object data,
								String textRepresentation){
//判断是否为bitmap
									if(view instanceof ImageView && data instanceof Bitmap){
											ImageView iv = (ImageView) view;
											iv.setImageBitmap((Bitmap) data);
											return true;
										}
									else
										return false;
								}
						});

				// 实例化底部布局
				this.moreView=LayoutInflater.from(context).inflate(R.layout.load,null);

				// 加上底部View，注意要放在setAdapter方法前
				this.lv.addFooterView(moreView);
				this.lv.setAdapter(mSimpleAdapter);
				// 绑定监听器
				//this.lv.setOnScrollListener(this);
				//fixListViewHeight(this.lv);
				this.lv.setOnItemClickListener(new OnItemClickListener(){
							public void onItemClick(final AdapterView<?> adapter,final View view,final int position,long id){
									String[] items = { "第一张","第二张","第三张"};
									AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
									listDialog.setTitle("你要看这一行的哪一个图片呢～"+id);
									listDialog.setItems(items,new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,final int whichpic){
														HashMap<String,Object> map = (HashMap<String, Object>) adapter.getItemAtPosition(position);
														final ImageView iv = new ImageView(context);
														iv.setImageBitmap(((Bitmap)map.get("pic" + (whichpic + 1))));
														AlertDialog.Builder picDialog = new AlertDialog.Builder(context);
														picDialog.setTitle("哎嘿～").setView(iv);
														picDialog.setPositiveButton("确定",null);
														picDialog.setNegativeButton("删除",new DialogInterface.OnClickListener(){
																	@Override
																	public void onClick(DialogInterface dialog,int which){
																			AlertDialog.Builder issure = new AlertDialog.Builder(context);
																			issure.setTitle("删除").setMessage("你确定要删除这张图片吗");
																			issure.setNegativeButton("取消",new DialogInterface.OnClickListener(){
																						@Override
																						public void onClick(DialogInterface dialog,int which){

																							}
																					});
																			issure.setPositiveButton("确定",new DialogInterface.OnClickListener(){
																						@Override
																						public void onClick(DialogInterface dialog,int which){
																								String path = all.get(position * 3 + whichpic);
																								/*AlertDialog.Builder a = new AlertDialog.Builder(context);
																									a.setMessage(path).show();*/
																								File f=new File(path);
																								f.delete();
																								Toast.makeText(context,"已删除",Toast.LENGTH_SHORT).show();

																							}
																					}).show();
																		}
																});
														picDialog.setNeutralButton("保存至",new DialogInterface.OnClickListener(){
																	@Override
																	public void onClick(DialogInterface dialog,int which){
																			final EditText editText = new EditText(context);
																			final String path = all.get(position * 3 + whichpic);
																			editText.setText(spUtil.getString("savepath") + "/" + new File(path).getName() + ".png");
																			AlertDialog.Builder inputDialog = new AlertDialog.Builder(context);
																			inputDialog.setTitle("请输入保存路径").setView(editText);
																			inputDialog.setPositiveButton("确定", 
																				new DialogInterface.OnClickListener() {
																						@Override
																						public void onClick(DialogInterface dialog,int which){
																								write.copy(path,editText.getText().toString());
																								Toast.makeText(context,
																																							"保存成功", 
																																							Toast.LENGTH_SHORT).show();
																							}
																					}).show();
																		}
																}).show();
													}
											}).show();
								}
						});

				this.bt=(Button) moreView.findViewById(R.id.bt_load);
				this.pg=(LinearLayout) moreView.findViewById(R.id.loading);
				this.pg.setVisibility(View.GONE);

				this.bt.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v){
									Toast.makeText(context,lv.getCount() + "m" + MaxDateNum + "m" + list.size() + "m" + all.size(),Toast.LENGTH_SHORT).show();

									if(lv.getCount() - 1 >= MaxDateNum){
											lv.removeFooterView(moreView);
											Toast.makeText(context,"图片已全部加载完毕，没有更多图片",Toast.LENGTH_LONG).show();

											return;
										}
									pg.setVisibility(View.VISIBLE);// 将进度条可见
									bt.setVisibility(View.GONE);// 按钮不可见
									handler.postDelayed(new Runnable() {
												@Override
												public void run(){
														loadMoreDate();// 加载更多数据
														bt.setVisibility(View.VISIBLE);
														pg.setVisibility(View.GONE);
														mSimpleAdapter.notifyDataSetChanged();// 通知listView刷新数据
													}
											},0);
								}
						});
			}


		public static List<String> getpiclist(String file){
				List<String> l= new ArrayList<String>();
				File f = new File(file);
				for(String filepath:f.list()){
						l.add(file + filepath);
					}
				return l;
			}
		public List<Bitmap> getbitmap(List<String> filelist){
				List<Bitmap> bit=new ArrayList<Bitmap>();
				Bitmap bitmap;
				for(String file:filelist){
						bitmap=BitmapFactory.decodeFile(file);
						if(bitmap == null){bit.add(error); continue;}
						/*int w = bitmap.getWidth();
							int h = bitmap.getHeight();*/
						bit.add(bitmap);
					}
				return bit;
			}
		public static List<HashMap<String,Object>> getlist(List<Bitmap> bitlist){
				List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
				int a=0;
				Bitmap[] temp = new Bitmap[2];
				for(Bitmap bit:bitlist){
					 if(++a < 3) temp[a - 1]=bit;
						else{list.add(getmap(new Object[] {temp[0],temp[1],bit}));a=0;}
					}
				if(a > 0){
						Object[] o1 = new Object[] {temp[0],null,null};
						Object[] o2 = new Object[] {temp[0],temp[1],null};
						list.add(getmap(a < 2 ? o1 : o2));
					}
				return list;
			}
		public static HashMap<String,Object> getmap(Object[] piclist){
				HashMap<String,Object> map = new HashMap<String,Object>();
				String[] s = new String[] {"pic1","pic2","pic3"};
				int i = 0;
				for(String pic:s){
						map.put(pic,piclist[i]);
						i++;
					}
				return map;
			}
		//如果listview在滑动视图中，高度会变得很怪，要用这个方法修复
		public static void fixListViewHeight(ListView listView){   
				// 如果没有设置数据适配器，则ListView没有子项，返回。  
				ListAdapter listAdapter = listView.getAdapter();  
				int totalHeight = 0;   
				if(listAdapter == null){   
						return;   
					}   
				for(int index = 0, len = listAdapter.getCount(); index < len; index++){     
						View listViewItem = listAdapter.getView(index ,null,listView);  
						// 计算子项View 的宽高   
						listViewItem.measure(0,0);    
						// 计算所有子项的高度和
						totalHeight+=listViewItem.getMeasuredHeight();    
					}   

				ViewGroup.LayoutParams params = listView.getLayoutParams();   
				// listView.getDividerHeight()获取子项间分隔符的高度   
				// params.height设置ListView完全显示需要的高度    
				params.height=totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
				listView.setLayoutParams(params);   
			}

		private void loadMoreDate(){
    int count = mSimpleAdapter.getCount() * 3;
				this.list.addAll(getlist(getbitmap(all.subList(0 + count,all.size() < 12 + count ? all.size() : 12 + count))));
				//fixListViewHeight(this.lv);
			}

		/*@Override
			public void onScroll(AbsListView view,int firstVisibleItem,
			int visibleItemCount,int totalItemCount){
			// 计算最后可见条目的索引
			lastVisibleIndex=firstVisibleItem + visibleItemCount - 1;

			// 所有的条目已经和最大条数相等
			if(totalItemCount == MaxDateNum + 1){
			Toast.makeText(context,"数据全部加载完成，没有更多数据！",200).show();
			}

			}

			@Override
			public void onScrollStateChanged(AbsListView view,int scrollState){
			// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
			/*if(scrollState == OnScrollListener.SCROLL_STATE_IDLE
			&& lastVisibleIndex == mSimpleAdapter.getCount()){
			// 当滑到底部时自动加载
			//						handler.postDelayed(new Runnable() {
			//									@Override
			//									public void run(){
			//											loadMoreDate();
			//											mSimpleAdapter.notifyDataSetChanged();
			//										}
			//								},0);
			}}*/
	}
