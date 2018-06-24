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
import java.time.LocalDate;
import java.util.Set;

@Constraint(validatedBy = {ChronologicalDateValidator.class})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface ChronDate {
    String message() default "Invalid dates.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}

@WebServlet(urlPatterns = "/ex5")
public class ValidationEx5 extends HttpServlet {

    @Inject
    SameDate sameDate;
    @Inject
    private Validator validator;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");

        sameDate.setBirthDay(LocalDate.of(3900, 12, 1));
        sameDate.setDeadDay(LocalDate.of(2040, 3, 2));

        Set<ConstraintViolation<SameDate>> validate = validator.validate(sameDate);
        if (validate.size() > 0) {
            PrintWriter writer = resp.getWriter();

            for (ConstraintViolation validator :
                    validate) {
                writer.write("<p>" + validator.getInvalidValue() + "</p>");
                writer.write("<p>" + validator.getMessage() + "</p>");
            }
        }
    }
}

@ChronDate
class SameDate {
    private LocalDate birthDay;
    private LocalDate deadDay;

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public LocalDate getDeadDay() {
        return deadDay;
    }

    public void setDeadDay(LocalDate deadDay) {
        this.deadDay = deadDay;
    }

    @Override
    public String toString() {
        return "SameDate{" +
                "birthDay=" + birthDay +
                ", deadDay=" + deadDay +
                '}';
    }
}

class ChronologicalDateValidator implements ConstraintValidator<ChronDate, SameDate> {
    @Override
    public boolean isValid(SameDate value, ConstraintValidatorContext context) {
        if (value.getBirthDay().isBefore(value.getDeadDay())) {
            return true;
        }
        return false;
    }

    @Override
    public void initialize(ChronDate constraintAnnotation) {

    }
}
