/**
 * Copyright (c) 2014 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * http://java.net/projects/javaeetutorial/pages/BerkeleyLicense
 */
package com.example.compute.forecastUI;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.example.compute.forecastUI.DFUHistData;
import com.example.compute.forecastUI.DBOpers;

/**
 * File upload servlet example
 */
@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload"})
@MultipartConfig
public class FileUploadServlet extends HttpServlet {

    private final static Logger LOGGER =
            Logger.getLogger(FileUploadServlet.class.getCanonicalName());
    private static final long serialVersionUID = 7908187011456392847L;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
    HttpServletResponse response)
            throws ServletException, IOException {

        String fileStr = request.getParameter("file");
        if(fileStr != null && fileStr.equals("download")) {
            downloadFile(request, response, fileStr);
        }
        else if(fileStr != null && fileStr.equals("forecast")) {
            downloadFile(request, response, fileStr);
        } else {


            response.setContentType("text/html;charset=UTF-8");

            DBOpers dbOpers = new DBOpers();
            Connection con = dbOpers.getConnection();

            String checkData = request.getParameter("result");
            if (checkData != null && checkData.equals("test")) {

                List<DFUHistData> dfusForecast = dbOpers.retrieveRecords(con);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(dfusForecast.toString());

            } else {
                // Create path components to save the file
                final String path = "C:/Users/j1008526/Desktop/template/";//"/home/buranpur_laxman/temp/";//"C:/Users/j1008526/Desktop/template/";
                final Part filePart = request.getPart("file");
                final String fileName = getFileName(filePart);

                BufferedReader reader = null;
                FileOutputStream out = null;
                InputStream filecontent = null;
                String newFileName = path + "Copy" + fileName;

                final PrintWriter writer = response.getWriter();

                try {
                    out = new FileOutputStream(new File(newFileName));
                    filecontent = filePart.getInputStream();

                    int read;
                    final byte[] bytes = new byte[1024];
                    List<DFUHistData> dfuHistDataList = new ArrayList();

                    while ((read = filecontent.read(bytes)) != -1) {
                        out.write(bytes, 0, read);
                    }

                    reader = new BufferedReader(new FileReader(newFileName));

                    String sCurrentLine = "";
                    SimpleDateFormat smf = new SimpleDateFormat("MM/dd/yyyy");
                    //first line is a marker and for reference leave the line and start processing from the next line.
                    reader.readLine();
                    String productMarker = "";

                    while ((sCurrentLine = reader.readLine()) != null) {
                        StringTokenizer tokenizer = new StringTokenizer(sCurrentLine, ",");
                        int i = 0;
                        DFUHistData dfuHistData = new DFUHistData();
                        double[] history = new double[104];
                        while (tokenizer.hasMoreElements()) {
                            String token = (String) tokenizer.nextToken();
                            if (i < 5) {
                                switch (i) {
                                    case 0:
                                        dfuHistData.setDmdUnit(token);
                                        productMarker = token;
                                        break;
                                    case 1:
                                        dfuHistData.setLoc(token);
                                        break;
                                    case 2:
                                        dfuHistData.setDmdGroup(token);
                                        break;
                                    case 3:
                                        dfuHistData.setModel(token);
                                        break;
                                    case 4:
                                        dfuHistData.setDmdPostDate(smf.parse(token).getTime());
                                        break;

                                }
                            } else if(i < 108) {
                                    history[i - 5] = Double.parseDouble(token);
                                }

                            i++;
                        }
                        dfuHistData.setBaseHistory(history);
                        dfuHistDataList.add(dfuHistData);
                    }

                    if(!productMarker.equals("") && productMarker.contains("-")) {
                        StringTokenizer markToken = new StringTokenizer(productMarker, "-");
                        productMarker = markToken.nextToken();
                    }

                    LOGGER.log(Level.INFO, "File {0} being uploaded to {1}",  new Object[]{fileName, path});
                    dbOpers.cleanDFUDetails(con, productMarker);
                    dbOpers.insertRecords(con, dfuHistDataList);

                    writer.println("New file " + fileName + " created at " + path + ", please go back and generate the forecast");
                    LOGGER.log(Level.INFO, "File {0} being uploaded to {1}",  new Object[]{fileName, path});


                } catch (FileNotFoundException fne) {
                    writer.println("You either did not specify a file to upload or are "
                            + "trying to upload a file to a protected or nonexistent "
                            + "location.");
                    writer.println("<br/> ERROR: " + fne.getMessage());

                    LOGGER.log(Level.SEVERE, "Problems during file upload. Error: {0}",
                            new Object[]{fne.getMessage()});
                } catch (ParseException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                    if (filecontent != null) {
                        filecontent.close();
                    }
                    if (writer != null) {
                        writer.close();
                    }
                }
            }
        }
    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response, String fileStr)  {

            String filePath = "C:/Users/j1008526/Desktop/template/";//"/home/buranpur_laxman/temp/";//"C:/Users/j1008526/Desktop/template/";
            filePath = (fileStr.equals("forecast")) ? filePath + "forecast.csv" : filePath + "sample.csv";

            // reads input file from an absolute path
            File downloadFile = new File(filePath);

        try(FileInputStream inStream = new FileInputStream(downloadFile)) {

            // if you want to use a relative path to context root:
            String relativePath = getServletContext().getRealPath("");
            LOGGER.log(Level.SEVERE, "relativePath = " + relativePath);

            // obtains ServletContext
            ServletContext context = getServletContext();

            // gets MIME type of the file
            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
            System.out.println("MIME type: " + mimeType);

            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());

            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);

            // obtains response's output stream
            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inStream.close();
            outStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet that uploads files to a user-defined destination";
    }
}
