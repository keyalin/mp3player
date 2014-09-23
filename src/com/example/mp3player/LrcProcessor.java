package com.example.mp3player;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LrcProcessor {
	Queue<Long> timMills = new LinkedList<Long>();
	Queue<String> messages = new LinkedList<String>();
	
	
	
	public Queue<Long> getTimMills() {
		return timMills;
	}

	public void setTimMills(Queue<Long> timMills) {
		this.timMills = timMills;
	}

	public Queue<String> getMessages() {
		return messages;
	}

	public void setMessages(Queue<String> messages) {
		this.messages = messages;
	}

	public void Process(InputStream inputStream){
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			String temp = null;
			Pattern p = Pattern.compile("\\[([^\\]]+)\\]");
			String tempmsg = null;
			while((temp = br.readLine()) != null){
				temp = temp.trim();
				Matcher match = p.matcher(temp);
				if(match.find()){
					String time = match.group();
					time = time.substring(1, time.length() - 1);
					Long timepoint = TimeToLong(time);
					if(tempmsg != null){
						messages.add(tempmsg);	
					}
					timMills.add(timepoint);
					System.out.println(timepoint);
					tempmsg = temp.substring(time.length() + 1, temp.length());	
					if(tempmsg.length() == 1){
						tempmsg = "";
					}else{
						tempmsg = tempmsg.substring(1) + "\n";
					}
				}else{
					if(tempmsg != null)
						tempmsg = tempmsg + temp + "\n";
				}
			}
			messages.add(tempmsg);
		}catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	private long TimeToLong(String time){
		String s[] = time.split(":");
		int min = Integer.parseInt(s[0]);
		String ss[] = s[1].split("\\.");
		int sec = Integer.parseInt(ss[0]);
		int mil = Integer.parseInt(ss[1]);
		return min * 60 * 1000 + sec * 1000 + mil * 10L;
	}
}
