import model.CourseIdea;
import model.CourseIdeaDAO;
import model.SimpleCourseIdeaDAO;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        staticFileLocation("/public");
        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/sign-in", (request, response) -> {
            Map<String, String> model = new HashMap<>();
            String username = request.queryParams("username");
            response.cookie("username", username);
            model.put("username", username);
            return new ModelAndView(model, "sign-in.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("ideas", (request, response) -> {
            String title = request.queryParams("title");
            //TODO:bgs - This username is tied to the cookie implematation
            CourseIdea courseIdea = new CourseIdea(title,
                    request.cookie("username"));
            dao.add(courseIdea);
            response.redirect("/ideas");
            return null;
        });

    }
}