package org.trahim.bean.validation.servlet;


import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Constraint;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Set;

@WebServlet(urlPatterns = "/ex3")
public class ValidationEx3 extends HttpServlet {
    @Inject
    private Validator validator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        PrintWriter writer = resp.getWriter();


        Set<ConstraintViolation<MyEmail>> email = validator.validateValue(MyEmail.class, "email", "same@gmail.com");
        if (email.size() > 0) {
            for (ConstraintViolation violation :
                    email) {
                writer.write("<p>"+violation.getMessage()+"</p>");
                writer.write("<p>"+violation.getInvalidValue()+"</p>");

            }
        }

        Set<ConstraintViolation<MyEmail>> email2 = validator.validateValue(MyEmail.class, "email", "same@gmail.ru");
        if (email2.size() > 0) {
            for (ConstraintViolation violation :
                    email2) {
                writer.write("<p>"+violation.getMessage()+"</p>");
                writer.write("<p>"+violation.getInvalidValue()+"</p>");

            }
        }


    }
}

@NotNull
@Pattern(regexp = "[A-Za-z0-9]*@[A-Za-z0-9]*\\.com")
@Constraint(validatedBy = {})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD, ElementType.TYPE})
@interface Email {
    String message() default "Email address doesn't look good.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


class MyEmail {
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
