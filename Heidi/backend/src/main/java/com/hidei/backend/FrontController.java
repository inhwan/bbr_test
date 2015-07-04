package com.hidei.backend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by laewoongJang on 2015-04-21.
 */
public class FrontController extends javax.servlet.http.HttpServlet {

    private static final String HELLO = "/hello";
    private static final String BYE = "/bye";
    private static final String CHARACTER_ENCODING = "UTF-8";

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding(CHARACTER_ENCODING);
        response.setCharacterEncoding(CHARACTER_ENCODING);

        final String action = request.getRequestURI();
        if(action == null) {
            return;
        }

        if(action.equals(HELLO))
        {
            new SecondController().helloHidei(request, response);
        }
        else if(action.equals(BYE))
        {
            new SecondController().byeHidei(request, response);
        }
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        PrintWriter out = response.getWriter(  );
        response.setContentType("text/html");
        out.println("<H1>Welcome Hidei Service</h2>");
        out.println("<P>You visited at ");
        out.println(new Date().toString());
        out.println("<P>Book Borrow Revolution ");
    }
}
