package org.trahim.bean.validation.servlet;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@WebServlet(urlPatterns = "/ex1")
public class ValidationEx1 extends HttpServlet {
    @Inject
    MyBean myBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}

@Dependent
class MyBean implements Serializable {

    @Pattern.List({
            @Pattern(regexp = "[A-Z][a-z]*"),
            @Pattern(regexp = "")
    })
    private String str;
    @Size(min = 10, max = 200)
    private int value;

    @NotNull
    @Past
    private Date date;

    @Future
    private Date date2;

    @NotNull
    @Max(value = 1000)
    private String descr;

}
