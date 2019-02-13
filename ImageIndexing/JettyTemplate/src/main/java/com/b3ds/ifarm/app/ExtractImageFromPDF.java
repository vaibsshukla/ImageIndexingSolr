	package com.b3ds.ifarm.app;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class ExtractImageFromPDF {
	
	public static String extract(InputStream url, String filename) throws IOException {	
		BufferedInputStream bis = new BufferedInputStream(url,url.available());
	 PDDocument doc=new PDDocument().load(url);
	 System.out.println(bis.available());
	 Iterator<PDPage> itr=doc.getDocumentCatalog().getPages().iterator();

	 Path currentRelativePath = Paths.get(""); 
	 String s=currentRelativePath.toAbsolutePath().toString();
	 String destinationFolderLoc = s+filename; // dynamic it is temp folder
	 File destinationFolder = new File(destinationFolderLoc);
	 FileUtils.forceMkdir(destinationFolder);
	 String putingfile = destinationFolderLoc+"//"+filename;
	 FileOutputStream fout=new FileOutputStream(new File(putingfile));
	 
	 while(itr.hasNext())
	 {
		PDResources res=itr.next().getResources();
		Iterable<COSName> cName=res.getXObjectNames();	
		Iterator<COSName> citr=cName.iterator();
			while(citr.hasNext())
			{
					String imageName= citr.next().getName();
					COSName cosName=COSName.getPDFName(imageName);
					PDImageXObject im = (PDImageXObject) res.getXObject(cosName);
					File ff = new File(destinationFolder+"//"+imageName+".png");
					BufferedImage bi=im.getImage();
					ImageIO.write(bi,"png",ff);
			}
		
			
	 }
	 doc.saveIncremental(fout);
	 fout.flush();
	 	doc.close();
	 	return destinationFolderLoc;
	 }
	
	
}
