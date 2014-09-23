package utils;

import java.io.*;
import java.net.*;

import android.os.Environment;

public class HttpDownloader {
	
	public static String DownloadString(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader br = null;
		try{
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			return sb.toString();
		}catch(Exception e){
			System.out.println(e);
			return null;
		}
		finally{
			try{
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static String GetSDDir(){
		File file = Environment.getExternalStorageDirectory();
		return file.getAbsolutePath() + "/";
	}
	
	public static File CreateDir(String dir){
		File directory = new File(dir);
		directory.mkdir();
		return directory;
	}
	
	public static int DownloadFile(String urlStr, String path, String filename){
		InputStream input = null;
		FileOutputStream output = null;
		try{
			input = new URL(urlStr).openConnection().getInputStream();
			CreateDir(path);
			File file = new File(path + filename);
			file.createNewFile();
			output = new FileOutputStream(file);
			byte[] bytes = new byte[128];
			int len;
			while((len = input.read(bytes)) != -1){
				output.write(bytes, 0, len);
			}
			output.flush();
			return 0;
		}catch(Exception e){
			System.out.println(e);
			return -1;
		}finally{
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args){
//		String url = "http://localhost:8080/music/test.txt";
//		System.out.println(url);
//		String html = HttpDownloader.DownloadString(url);
//		System.out.println(html);
	}
}
