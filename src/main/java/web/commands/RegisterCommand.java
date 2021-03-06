package web.commands;

import business.entities.User;
import business.persistence.Database;
import business.services.UserFacade;
import business.exceptions.UserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RegisterCommand extends CommandUnprotectedPage
{
    private UserFacade userFacade;

    public RegisterCommand(String pageToShow)
    {
        super(pageToShow);
        userFacade = new UserFacade(database);
    }

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws UserException
    {
        String name = request.getParameter("name");
        String street = request.getParameter("street");
        String town = request.getParameter("town");
        int zipCode = Integer.parseInt(request.getParameter("zipCode"));
        String email = request.getParameter("email");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        int phone = Integer.parseInt(request.getParameter("phone"));

        if (password1.equals(password2))
        {
            User user = userFacade.createUser(name, street, town, zipCode, email, password1, phone);
            HttpSession session = request.getSession();

            session.setAttribute("user", user);
//            session.setAttribute("role", user.getRole());
            return user.getRole() + "page";
        }
        else
        {
            request.setAttribute("error", "the two passwords did not match");
            return "registerpage";
        }
    }

}
