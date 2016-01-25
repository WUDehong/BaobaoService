/**
 * @ c2015-2025 All right reserve by WUDeHong
 */
package com.wu.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author WDH
 * @Date 2016-1-4
 * 
 */
public class UploadAPKServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter printWriter = response.getWriter();
		try {
			String filePath = getServletContext().getRealPath("/upload");
			String versionPath = getServletContext().getRealPath(
					"WEB-INF/classes/version.properties");
			System.out.println("filePath" + filePath);
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(1024 * 1024);
			factory.setRepository(new File(filePath));
			ServletFileUpload fileUpload = new ServletFileUpload(factory);
			fileUpload.setFileSizeMax(100 * 1024 * 1024);
			fileUpload.setHeaderEncoding("utf-8");
			List<FileItem> fileItems = fileUpload.parseRequest(request);
			Properties p = new Properties();
			for (FileItem fi : fileItems) {
				if (fi.isFormField()) {
					String name = fi.getFieldName();
					String value = fi.getString("utf-8");
					if (name.equals("versionName")
							|| name.equals("versionCode")
							|| name.equals("versionDescription")) {
						p.setProperty(name, value);
						System.out.println(value);
					}
					
				} else {
					filePath = filePath + "/" + fi.getName();
					File file = new File(filePath);
					fi.write(file);
				}
			}
			FileOutputStream fos = new FileOutputStream(versionPath);
			p.store(fos, null);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		printWriter.write("success!");
		printWriter.flush();
		printWriter.close();
	}

}
