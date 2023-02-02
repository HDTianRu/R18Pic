package com.TianRu.r18;

import android.app.*;
import android.content.*;
import android.text.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

public class Titlelayout extends LinearLayout{
				private Context con;
    //private TextView tv_title;
				private ImageView iv;
				private ImageButton ib;
    public Titlelayout(Context context){
        super(context,null);
						}
    public Titlelayout(final Context context, AttributeSet attrs){
        super(context,attrs);
								this.con = context;
        //引入布局
        LayoutInflater.from(context).inflate(R.layout.title_bar,this);
        /*Button btnBack=(Button)findViewById(R.id.btnBack);
									Button btnEdit=(Button)findViewById(R.id.btnEdit);
									btnBack.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
									((Activity)getContext()).finish();
									}
									});

									btnEdit.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View view) {
									Toast.makeText(context,"编辑",Toast.LENGTH_SHORT).show();
									}
									});

									tv_title=(TextView)findViewById(R.id.tv_title);*/
								this.iv = (ImageView) findViewById(R.id.icon);
								this.ib = (ImageButton) findViewById(R.id.setting);

						}

    //显示活的的标题
    /*public void setTitle(String title)
					{
					if(!TextUtils.isEmpty(title))
					{
					tv_title.setText(title);
					}
					}*/
				public void setIcon(int id){
								this.iv.setImageResource(id);
						}
				public void hideImageButton(boolean b){
								this.ib.setVisibility(b ? View.GONE : View.VISIBLE);
						}
				public void setOnclick(OnClickListener o){
								this.ib.setOnClickListener(o);
						}
				public void setbuttonimage(){

						}
		}
