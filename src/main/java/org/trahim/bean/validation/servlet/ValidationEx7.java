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

interface MyGroup {
}

interface MyGroup2 {
}


interface MyGroup3 {
}

interface MyGroup4 {
}

@WebServlet(urlPatterns = "/ex7")
public class ValidationEx7 extends HttpServlet {
    @Inject
    private Validator validator;

    @Inject
    private SameGroup sameGroup;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        Set<ConstraintViolation<SameGroup>> validate = validator.validate(sameGroup, MyGroup.class);
        if (validate.size() > 0) {
            PrintWriter writer = resp.getWriter();
            for (ConstraintViolation violation :
                    validate) {
                writer.write("<p>"+violation.getInvalidValue()+"</p>");
                writer.write("<p>"+violation.getMessage()+"</p>");
            }
        }
    }
}

class SameGroup {
    @NotNull(groups = MyGroup.class)
    private String s;
    @NotNull(groups = {MyGroup.class, MyGroup2.class})
    private String s2;
    @NotNull(groups = MyGroup3.class)
    private String s3;
    @NotNull(groups = MyGroup4.class)
    private String s4;

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }
}