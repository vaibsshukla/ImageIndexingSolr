package com.b3ds.ifarm.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.b3ds.ifarm.app.ExtractImageFromPDF;
import com.b3ds.ifarm.app.PostingImage;


@org.springframework.stereotype.Controller
public class Controller {
	
	private final Logger logger = LogManager.getLogger(Controller.class);
	
	
	@RequestMapping("/")
	public String index(HttpServletRequest req)
	{
		return "redirect:index.html";
	}

	
	@RequestMapping(value="/imageupload", method=RequestMethod.POST)
	@ResponseBody
	public String singleFileUpload(MultipartHttpServletRequest req,MultipartFile file) throws IOException, InterruptedException
	{
		ExtractImageFromPDF image=new ExtractImageFromPDF();
		PostingImage pi=new PostingImage();
		
		file = req.getFile("fileUpload");
		InputStream is = file.getInputStream();

		String loc=image.extract(is, file.getOriginalFilename());
		pi.postfile(loc);
		pi.postDataHdfs(loc);
		
		return loc;
	}
	
	@RequestMapping(value="/something", method=RequestMethod.GET)
	@ResponseBody
	public String some(HttpServletRequest req)
	{
		logger.debug("#############################################################################");
		return "indexes";
	}

	@RequestMapping("/llp")
	@ResponseBody
	public String somes(HttpServletRequest req)
	{
		logger.debug("#############################################################################");
		return "llp";
	}
	
}
