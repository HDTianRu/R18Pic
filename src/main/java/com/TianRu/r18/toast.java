package com.TianRu.r18;
import android.content.*;
import android.os.*;
import android.widget.*;

public class toast{
				private static Toast mToast;
				public static void showtoast(Context context, String text){
								if(mToast == null){
												mToast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
										}
								else if(Build.VERSION.SDK_INT >= 26){
												mToast.cancel();
												mToast = Toast.makeText(context,text,Toast.LENGTH_SHORT);
										}
								else{
												mToast.cancel();
												mToast.setText(text);
												mToast.setDuration(Toast.LENGTH_SHORT);
												mToast.show();
										}
						}
		}

