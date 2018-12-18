package com.xtool.iot.orderid;

import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderIdGenerator {
	@Value("${spring.application.name}")
	public String ServerName;
	
	private String lastid;
	
	public synchronized String NextId() {
		String result=buildId();
		while(lastid!=null && result.equals(lastid)) {
			try {
				Thread.sleep(20);
			}catch (Exception e) {
				// TODO: handle exception
			}
			result=buildId();
		}
		lastid=result;
		return result;
	}
	
	private String buildId() {
		String server=Integer.toHexString( this.ServerName.hashCode()).toLowerCase();
		if(server.length()<8) {
			for(int i=0;i<8-server.length();i++)server="0"+server;
		}
		Date now=new Date();
		String mils=Long.toHexString(now.getTime()).toLowerCase();
		if(mils.length()<16) {
			for(int i=0;i<16-mils.length();i++)mils="0"+mils;
		}
		
		Random random=new Random(now.getTime());
		String rand=String.valueOf(random.nextInt(89999)+10000);
		
		return String.format("o%s%s%s", server,mils,rand);
	}
}
