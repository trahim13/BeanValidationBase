package org.trahim.bean.validation.servlet;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.executable.ExecutableValidator;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Set;

@WebServlet(urlPatterns = "/ex2")
public class ValidationEx2 extends HttpServlet {
    @Inject
    private Person person;

    //    @Inject
    private Validation validation;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        person.setName("Trahim");
        person.setAge(12);

//        ValidatorFactory validatorFactory = validation.buildDefaultValidatorFactory();


        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Person>> validate = validator.validate(person);


        PrintWriter writer = resp.getWriter();
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");

        if (validate.size() > 0) {
            writer.write("Bean has same errors.");

            for (ConstraintViolation<Person> violation : validate) {
                writer.write("Message: " + violation.getMessage());
                writer.write("  ");
                writer.write("InvalidValue: " + violation.getInvalidValue().toString());
            }
        }


        Set<ConstraintViolation<Person>> name = validator.validateProperty(person, "name");
        if (name.size() > 0) {
            writer.write(" ");
            writer.write("The name is wrong.");
        }

        Set<ConstraintViolation<Person>> constraintViolations = validator.validateValue(Person.class, "name", "Mask");
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<Person> val :
                    constraintViolations) {
                writer.write(val.getMessage());
                writer.write(val.getInvalidValue().toString());
            }
        }

        try {
            Method setName = Person.class.getMethod("setName", String.class);
            ExecutableValidator executableValidator = validator.forExecutables();
            Set<ConstraintViolation<Class<Person>>> constraint = executableValidator.validateParameters(Person.class, setName, new Object[]{null});
            if (constraint.size() > 0) {
                writer.write("<p>return value is wrong</p>");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        validatorFactory.close();
    }


}

@Dependent
class Person {

    @Pattern(regexp = "[A-Z]")
    private String name;
    @Min(value = 18)
    private int age;

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
