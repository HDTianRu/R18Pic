package com.TianRu.r18;

import android.app.*;
import android.content.*;

public class spUtil{
		public static final String FILE_NAME = "config";
		private static Activity activity;
		public static SharedPreferences sp;
		public static void setActivity(Activity act){
				activity=act;
				sp=activity.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
			}
		public static Activity getActivity(){
				return activity;
			}
		public static void remove(String key){
				sp.edit().remove(key).commit();
			}
		public static void putString(String key, String value){
				sp.edit().putString(key,value).commit();
			}
		public static String getString(String key){
				return sp.getString(key,"");
			}
		public static void putBoolean(String key, boolean value){
				sp.edit().putBoolean(key,value).commit();
			}
		public static boolean getBoolean(String key){
				return sp.getBoolean(key,false);
			}
		public static void putInt(String key, int value){
				sp.edit().putInt(key,value).commit();
			}
		public static int getInt(String key){
				return sp.getInt(key,0);
			}
	}
