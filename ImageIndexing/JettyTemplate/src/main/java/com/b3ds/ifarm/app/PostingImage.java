package com.b3ds.ifarm.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class PostingImage {

	private LoadProperties propt = LoadProperties.loadProperties();
	
	public int postfile(String loc) throws InterruptedException
	{						
		ExtractImageFromPDF ex=new ExtractImageFromPDF();
		File folder=new File(loc);
		int id=0;
		File[] listOfFiles=folder.listFiles();
		int status = 0;

		for(File file:listOfFiles)
		{ 
//			System.out.println(file);
			if(file.isFile())
			{
			String host=propt.getProperty("solr.url");
			String collection=propt.getProperty("solr.core");
			MultipartBodyBuilder mp = new MultipartBodyBuilder();
			Resource res = new FileSystemResource(file);
			mp.part("file", res).header("Content-Type", "multipart/form-data");
			MultiValueMap<String, HttpEntity<?>> mm = mp.build();
			String uid=UUID.randomUUID().toString();
			HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(mm);	
			String serverUrl = "http://"+host+":8983/solr/"+collection+"/update/extract?literal.id="+uid+"&commit=true";
			RestTemplate restTemplate = new RestTemplate();			
			ResponseEntity<String> response = restTemplate
			  .postForEntity(serverUrl, requestEntity, String.class);
			status = response.getStatusCodeValue();
			id++;
			}
			
			
		}
		
		return status;
	}
	
	public int postDataHdfs(String loc) throws IOException
	{
		File folder=new File(loc);
		File[] listOfFiles=folder.listFiles();
		ResponseEntity<String> res = null;

		for(File file:listOfFiles)
		{
			String host=propt.getProperty("hdfs.url");
			String username=propt.getProperty("hdfs.username");
			String namenode=propt.getProperty("hdfs.namenoderpcaddress");
			FileInputStream fis = new FileInputStream(file);
			byte[] bytes = IOUtils.toByteArray(fis);
			String url="http://"+host+":50075/webhdfs/v1/solrdata/"+file.getName()+"?op=CREATE&user.name="+username+"&namenoderpcaddress="+namenode+"&overwrite=true";			//dynamic webhdfs host port and username and namenoderpc
			RestTemplate restTemplate = new RestTemplate();		
			HttpEntity<Object> entity = new HttpEntity<Object>(bytes);
			res = restTemplate.exchange(url, HttpMethod.PUT,entity,String.class);		
		}
		try{
			return res.getStatusCodeValue();
		}catch(NullPointerException npe)
		{
			return 500;
		}
	}
	
	public static void main(String[] args) {
		
		
	}
	
}
