package com.xtool.iot.orderid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xtool.enterprise.RespState;

@RestController
@RefreshScope
@EnableConfigurationProperties({DemoConfig.class})
public class MainController {

	@Autowired
	private OrderIdGenerator idGenerator;
	
	@RequestMapping(value="/nextid")
	public RespState<String> nextOrderId() {
		RespState<String> resp=new RespState<>();
		resp.setCode(0);
		resp.setMsg("");
		resp.setData(idGenerator.NextId());
		return resp;
	}
	@Autowired
	private DemoConfig config;
	
	@RequestMapping(value="/name")
	public RespState<String> name(){
		RespState<String> resp=new RespState<>();
		resp.setCode(0);
		resp.setMsg("");
		resp.setData(config.getName());
		
		Path path= Paths.get("logs");
		if(Files.notExists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Path file=path.resolve("aaa.txt");
		try {
			Files.write(file, (resp.getData()+"\n").getBytes(),StandardOpenOption.APPEND,StandardOpenOption.CREATE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resp;
	}
}
