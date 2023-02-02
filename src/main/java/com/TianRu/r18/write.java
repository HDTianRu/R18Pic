package com.TianRu.r18;

import android.app.*;
import android.content.*;
import java.io.*;

public class write{
		public static String write(String path,String WriteData){
				try{
						File file = new File(path);
						if(!file.getParentFile().exists()){    
								file.getParentFile().mkdirs(); 
								file.createNewFile();
							}
						else if(!file.exists()){
								file.createNewFile();
							}
						FileOutputStream fos = new FileOutputStream(file);
						byte[] bytesArray = WriteData.getBytes();
						fos.write(bytesArray);
						fos.flush();
					}
				catch(IOException ioe){
						return "写入时发生错误:" + ioe;
					}
				return "写入成功";
			}
		public static void copy(String path1,String path2){
				copy(new File(path1),new File(path2));
			}
		public static void copy(File f1,File f2){
				InputStream is = null;
				OutputStream os = null;

				try{
						is=new FileInputStream(f1);
						os=new FileOutputStream(f2);
						byte[] buffer = new byte[1024];
						int length;
						while((length = is.read(buffer)) > 0){
								os.write(buffer,0,length);
							}
					}
				catch(IOException e){}
				finally{
						try{
								is.close();
								os.close();
							}
						catch(IOException e){}
					}
			}
	}
