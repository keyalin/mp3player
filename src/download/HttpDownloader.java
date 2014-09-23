package download;

import java.io.*;
import java.net.*;

import android.os.Environment;

public class HttpDownloader {
	public  String sdpath = null;
	public HttpDownloader(){
		sdpath = GetSDDir();
	}
	
	
	public File[] GetFileList(String path){
		File dir = new File(sdpath + path);
		return dir.listFiles();
	}
	public String DownloadString(String urlStr){
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
	
	public  String GetSDDir(){
		File file = Environment.getExternalStorageDirectory();
		String dir = file.getAbsolutePath() + File.separator;
		System.out.println("sdpath-->" + dir);
		return dir;
	}
	
	public  File CreateDir(String dir){
		System.out.println("create dir: " + this.sdpath + dir);
		File directory = new File(this.sdpath + dir);
		directory.mkdir();
		return directory;
	}
	
	public  File CreateSDFile(String fileName) throws IOException{
		System.out.println("create file: " + this.sdpath + fileName);
		File file = new File(this.sdpath + fileName);
		file.createNewFile();
		return file;
	}
	
	public  boolean isFileExist(String fileName){
		File  file = new File(sdpath + fileName);
		return file.exists();
	}
	
	public  int DownloadFile(String urlStr, String path, String filename){
		if(isFileExist(path+filename)){
			return 1;
		}
		System.out.println("Path: " + path);
		System.out.println("filename: " + filename);
		InputStream input = null;
		FileOutputStream output = null;
		try{
			input = new URL(urlStr).openConnection().getInputStream();
			CreateDir(path);
			File file = CreateSDFile(path + filename);
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
	
	public  void main(String[] args){
//		String url = "http://localhost:8080/music/test.txt";
//		System.out.println(url);
//		String html = HttpDownloader.DownloadString(url);
//		System.out.println(html);
	}
}
