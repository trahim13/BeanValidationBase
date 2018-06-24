package org.trahim.bean.validation.servlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet(urlPatterns = "/ex6")
public class ValidationEx6 extends HttpServlet {
    @Inject
    private Validator validator;

    @Inject
    private L l;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        Set<ConstraintViolation<L>> validate = validator.validateValue(L.class, "value", null);
        if (validate.size() > 0) {
            PrintWriter writer = resp.getWriter();
            for (ConstraintViolation violation :
                    validate) {
                writer.write("<p>" + violation.getInvalidValue() + "</p>");
                writer.write("<p>" + violation.getMessage() + "</p>");

            }
        }
    }
}

class V {

    @NotNull
    String value;



}

class L extends V {

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}