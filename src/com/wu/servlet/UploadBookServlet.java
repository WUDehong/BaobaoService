/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.wu.entity.Book;
import com.wu.service.BookDBService;

/**
 * @author WDH
 * @Date 2016-1-4
 * 
 */
public class UploadBookServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter printWriter = response.getWriter();
		try {
			String filePath = getServletContext().getRealPath("/upload");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			factory.setRepository(new File(filePath));
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setFileSizeMax(100 * 1024 * 1024);
			fileUpload.setHeaderEncoding("UTF-8");
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			Book book = new Book();
			for (FileItem fi : fileItems) {
				if (fi.isFormField()) {
					String name = fi.getFieldName();
					String value = new String(fi.getString().getBytes("ISO-8859-1"),"UTF-8");
					if (name.equals("name")){
						System.out.println("name:"+value);
						book.setName(value);
					} else if(name.equals("id")){
						book.setId(Integer.parseInt(value));
					} else if(name.equals("price")){
						book.setPrice(Double.parseDouble(value));
					} else if(name.equals("description")){
						book.setDescription(value);
					} else if(name.equals("author")){
						book.setAuthor(value);
					}
					
				} else {
					String name = fi.getFieldName();
					String tempPath = null;
					if(name.equals("imageFile")){
						tempPath =filePath+"/"+ fi.getName();
						book.setImageUrl(fi.getName());
						System.out.println("image url"+tempPath);
					} else{
						tempPath =filePath+ "/" + fi.getName();
						book.setFileUrl(fi.getName());
						System.out.println("file url"+tempPath);
					}
					File file = new File(tempPath);
					fi.write(file);
				}
			}
			BookDBService bds = new BookDBService();
			bds.insertBookToDB(book);
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter.write("success!");
		printWriter.flush();
		printWriter.close();
	}

}
