package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.ActionBase;
import actions.UnknownAction;
import constants.ForwardConst;

/**
 * Front controller
 *
 */
@WebServlet("/")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FrontController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServlerResponse response)
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Action class instance that equal to parameter
        ActionBase action = getAction(request, response);

        //Set servlet context, request and response at field of Action instance
        action.init(getServletContext(), request, response);

        //Call Action class process
        action.process();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletRequest request)
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Create Action class's instance that equal to request parameter and return it
     * (Example: In case parameter that action=Employee, create actions.EmployeeAction object)
     * @param request Request
     * @param response Response
     * @return
     */

    @SuppressWarnings({"rawtypes","unchecked"})
    private ActionBase getAction(HttpServletRequest request, HttpServletResponse response) {
        Class type = null;
        ActionBase action = null;
        try {

            //Get value of "action" from request (Example:"Employee", "Report")
            String actionString = request.getParameter(ForwardConst.ACT.getValue());

            //Create Action object that applicable(Example: In case parameter is action=Employee from request, then create actions.EmployeeAction object)
            type = Class.forName(String.format("actions.%sAction", actionString));

            //Cast to object of ActionBase (Example: actions.EmployeeAction object to actions.ActionBase object)
            action = (ActionBase)(type.asSubclass(ActionBase.class)
                    .getDeclaredConstructor()
                    .newInstance());

        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {

            //In case value of "action" that has set request parameter is illegal (Example: Not exist that applicate Action class example action=xxxxx and else)
            //Create Action object that error processing
            action = new UnknownAction();
        }
        return action;
    }

}
