package org.trahim.bean.validation.servlet;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Constraint(validatedBy = {CheckSiteLogic.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@interface Url {
    String message() default "{example.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int port() default -1;

    String host() default "";

    String protocol() default "";

}

@WebServlet(urlPatterns = "/ex4")
public class ValidationEx4 extends HttpServlet {
    @Inject
    Validator validator;
    @Inject
    private MySite mySite;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        PrintWriter writer = resp.getWriter();

        Set<ConstraintViolation<MySite>> site = validator.validateValue(MySite.class, "site", "myfirstsite.com");
        if (site.size() > 0) {
            for (ConstraintViolation violation :
                    site) {

                writer.write("<p>"+"site"+"<p/>");
                writer.write("<p>"+violation.getMessage()+"<p/>");
                writer.write("<p>"+violation.getInvalidValue()+"<p/>");

            }
        }

        Set<ConstraintViolation<MySite>> site2 = validator.validateValue(MySite.class, "site2", "https://myfirstsite.com");
        if (site2.size() > 0) {
            for (ConstraintViolation violation :
                    site2) {
                writer.write("<p>"+"site2"+"<p/>");
                writer.write("<p>"+violation.getMessage()+"<p/>");
                writer.write("<p>"+violation.getInvalidValue()+"<p/>");

            }
        }

        Set<ConstraintViolation<MySite>> site3 = validator.validateValue(MySite.class, "site3", "https://myfirstsite.com");
        if (site3.size() > 0) {
            for (ConstraintViolation violation :
                    site3) {
                writer.write("<p>"+"site3"+"<p/>");
                writer.write("<p>"+violation.getMessage()+"<p/>");
                writer.write("<p>"+violation.getInvalidValue()+"<p/>");

            }
        }


        Set<ConstraintViolation<MySite>> site4 = validator.validateValue(MySite.class, "site4", "ftp://site.com:22");
        if (site4.size() > 0) {
            for (ConstraintViolation violation :
                    site4) {

                writer.write("<p>"+"site4"+"<p/>");
                writer.write("<p>"+violation.getMessage()+"<p/>");
                writer.write("<p>"+violation.getInvalidValue()+"<p/>");
            }
        }


    }
}

class MySite {

    @Url
    private String site;
    @Url(protocol = "http")
    private String site2;
    @Url(host = "mysite.com")
    private String site3;
    @Url(protocol = "ftp", port = 21)
    private String site4;
}

class CheckSiteLogic implements ConstraintValidator<Url, String> {
    private String protocol;
    private String host;
    private int port;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null && value.equals("")) {
            return true;
        }

        URL url;

        try {
            url = new URL(value);
        } catch (MalformedURLException e) {
            return false;
        }

        if (protocol != null && protocol.length() > 0 && !protocol.equals(url.getProtocol())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("invalid protocol").addConstraintViolation();
            return false;
        }

        if (host != null && host.length() > 0 && !host.equals(url.getHost())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("invalid host").addConstraintViolation();
            return false;
        }

        if (port != -1 && port != url.getPort()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("invalid port").addConstraintViolation();
            return false;
        }


        return true;


    }

    @Override
    public void initialize(Url constraintAnnotation) {
        protocol = constraintAnnotation.protocol();
        host = constraintAnnotation.host();
        port = constraintAnnotation.port();
    }
}