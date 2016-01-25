package com.wu.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadImageServlet extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		String path = request.getSession().getServletContext().getRealPath("/upload");
		factory.setRepository(new File(path));
		factory.setSizeThreshold(1024*1024);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(10 * 1024 * 1024);
		upload.setHeaderEncoding("UTF-8");
		String email = request.getParameter("email");
		System.out.println(email);
		try{
			List<FileItem> list = (List<FileItem>)upload.parseRequest(request);
			for(FileItem item:list){
				if(!item.isFormField()){
					File f = new File(path,email+".jpeg");
					item.write(f);
					System.out.println("upload image success");
				} 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}

