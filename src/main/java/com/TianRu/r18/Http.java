package com.TianRu.r18;

import java.net.*;
import java.nio.*;
import java.io.*;
import android.graphics.*;
import android.widget.*;
import com.TianRu.r18.*;

public class Http{
		public static String httpget(String url, String cookie){
				StringBuffer buffer = new StringBuffer();
				InputStreamReader isr = null;
				try{
						URL urlObj = new URL(url);
						URLConnection uc = urlObj.openConnection();
						uc.setRequestProperty("Cookie",cookie);
						uc.setConnectTimeout(10000);
						uc.setReadTimeout(10000);
						isr=new InputStreamReader(uc.getInputStream(),"utf-8");
						BufferedReader reader = new BufferedReader(isr); //缓冲
						String line;
						while((line=reader.readLine())!=null){
								buffer.append(line+"\n");
							}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						try{
								if(null!=isr){
										isr.close();
									}
							}catch(IOException e){
								e.printStackTrace();
							}
					}
				if(buffer.length()==0)return buffer.toString();
				buffer.delete(buffer.length()-1,buffer.length());
				return buffer.toString();
			}
		public static String httppost(String urlPath, String cookie, String data){
				StringBuffer buffer = new StringBuffer();
				InputStreamReader isr = null;
				try{
						URL url = new URL(urlPath);
						HttpURLConnection uc = (HttpURLConnection) url.openConnection();
						uc.setDoInput(true);
						uc.setDoOutput(true);
						uc.setConnectTimeout(2000000);// 设置连接主机超时（单位：毫秒）
						uc.setReadTimeout(2000000);// 设置从主机读取数据超时（单位：毫秒）
						uc.setRequestMethod("POST");
						uc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
						uc.setRequestProperty("Cookie",cookie);
						uc.getOutputStream().write(data.getBytes("UTF-8"));
						uc.getOutputStream().flush();
						uc.getOutputStream().close();
						isr=new InputStreamReader(uc.getInputStream(),"utf-8");
						BufferedReader reader = new BufferedReader(isr); //缓冲
						String line;
						while((line=reader.readLine())!=null){
								buffer.append(line+"\n");
							}
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						try{
								if(null!=isr){
										isr.close();
									}
							}catch(IOException e){
								e.printStackTrace();
							}
					}
				if(buffer.length()==0)return buffer.toString();
				buffer.delete(buffer.length()-1,buffer.length());
				return buffer.toString();
			}
		public static String downloadFile(String Url, String savepath){
				HttpURLConnection httpUrl = null;
				byte[] buf = new byte[1024];
				int size = 0;
				String path;
				try{
						//下载的地址
						URL url = new URL(Url);
						//支持http特定功能
						httpUrl=(HttpURLConnection) url.openConnection();
						httpUrl.connect();
						//缓存输入流,提供了一个缓存数组,每次调用read的时候会先尝试从缓存区读取数据
						BufferedInputStream bis = new BufferedInputStream(httpUrl.getInputStream());
						File file = new File(savepath);
						//判断文件夹是否存在
						if(!file.exists()){
								file.mkdir();//如果不存在就创建一个文件夹
							}
						//讲http上面的地址拆分成数组,
						String arrUrl[] = Url.split("/");
						//输出流,输出到新的地址上面
						path=savepath+"/"+arrUrl[arrUrl.length-1];
						FileOutputStream fos = new FileOutputStream(path);
						while((size=bis.read(buf))!=-1){
								fos.write(buf,0,size);
							}
						//记得及时释放资源
						fos.close();
						bis.close();
					}catch(IOException e){
						return e.toString();
					}
				httpUrl.disconnect();
				return "保存成功，已保存至:"+path;
			}
	}

