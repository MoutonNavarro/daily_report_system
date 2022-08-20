package actions;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constants.AttributeConst;
import constants.ForwardConst;
import constants.PropertyConst;

/**
 * Parent class that each Action class. it process general.
 *
 */

public abstract class ActionBase {
    protected ServletContext context;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    /**
     * Initializing
     * Set servlet context, request, response at class field
     * @param servletContext
     * @param servletRequest
     * @param servletResponse
     */
    public void init(
            ServletContext servletContext,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse) {
        this.context = servletContext;
        this.request = servletRequest;
        this.response = servletResponse;
    }

    /**
     * Method that has will called by front controller
     * @throws ServletException
     * @throws IOException
     */
    public abstract void process() throws ServletException, IOException;

    /**
     * Run method that equal to value of command of parameter
     * @throws ServletException
     * @throws IOException
     */
    protected void invoke() throws ServletException, IOException{
        Method commandMethod;
        try {
            //Get command from parameter
            String command = request.getParameter(ForwardConst.CMD.getValue());

            //Run method that equal to command
            //(Example: In case action=Employee command=show, then run show() method at Employee class)
            commandMethod = this.getClass().getDeclaredMethod(command, new Class[0]);
            commandMethod.invoke(this, new Object[0]); //The argument to method is none
        }catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NullPointerException e) {
            //Show happened exceptions to console
            e.printStackTrace();
            //In case impossible to run because command value is illegal, then call error screen
            forward(ForwardConst.FW_ERR_UNKNOWN);
        }
    }

    /**
     * Call pointed jsp
     * @param target File name that screen of destination jsp(not include extension)
     * @throws ServletException
     * @throws IOException
     */
    protected void forward(ForwardConst target) throws ServletException, IOException {
        //Create relative path of the jsp file
        String forward = String.format("/WEB-INF/views/%s.jsp", target.getValue());
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);

        //Call the jsp file
        dispatcher.forward(request, response);
    }

    /**
     * Setup the URL and do redirect
     * @param action Value that set parameter
     * @param command Value that set parameter
     * @throws ServletException
     * @throws IOException
     */
    protected void redirect(ForwardConst action, ForwardConst command)
            throws ServletException, IOException {
        //Setup the URL
        String redirectUrl = request.getContextPath() + "/?action=" + action.getValue();
        if (command != null) {
            redirectUrl = redirectUrl + "&command=" + command.getValue();
        }

        //redirect to the URL
        response.sendRedirect(redirectUrl);
    }

    /**
     * Anti CSRF: In case illegal token, then show error screen
     * @return true: token valid / false: token illegal
     * @throws ServletException
     * @throws IOException
     */
    protected boolean checkToken() throws ServletException, IOException{
        //Get token value from parameter
        String _token = getRequestParam(AttributeConst.TOKEN);

        if (_token == null || !(_token.equals(getTokenId()))) {
            //In case not set a token or token isn't equals to the sesion ID, then show error screen
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;
        }else {
            return true;
        }
    }
    /**
     * Get session ID
     * @return The session ID
     */
    protected String getTokenId() {
        return request.getSession().getId();
    }

    /**
     * Get number of page that requested to show from request and return it
     * @return Number of requested page (In no request: 1)
     */
    protected int getPage() {
        int page;
        page = toNumber(request.getParameter(AttributeConst.PAGE.getValue()));
        if (page == Integer.MIN_VALUE) {
            page = 1;
        }
        return page;
    }

    /**
     * Convert character string to number
     * @param strNumber Character string that before convert
     * @return Character string that after convert
     */
    protected int toNumber(String strNumber) {
        int number = 0;
        try {
            number = Integer.parseInt(strNumber);
        }catch (Exception e) {
            number = Integer.MIN_VALUE;
        }
        return number;
    }

    /**
     * Convert character string to LocalDate type
     * @param strDate Character string that before convert
     * @return LocalDate instance that after convert
     */
    protected LocalDate toLocalDate(String strDate) {
        if (strDate == null | strDate.equals("")) {
            return LocalDate.now();
        }
        return LocalDate.parse(strDate);
    }

    /**
     * Return the value of the pointed parameter name with argument from request parameter
     * @param key Parameter name
     * @return Value of parameter
     */
    protected String getRequestParam(AttributeConst key) {
        return request.getParameter(key.getValue());
    }

    /**
     * Set parameter to the request scope
     * @param key Parameter name
     * @param value Parameter value
     */
    protected <V> void putRequestScope(AttributeConst key, V value) {
        request.setAttribute(key.getValue(), value);
    }

    /**
     * Get value of the pointed parameter value from the session scope and return it
     * @param key Parameter name
     * @return Value of the parameter
     */
    @SuppressWarnings("unchecked")
    protected <R> R getSessionScope(AttributeConst key){
        return (R) request.getSession().getAttribute(key.getValue());
    }

    /**
     * Set parameter to the session scope
     * @param key Parameter name
     * @param value Value of the parameter
     */
    protected <V> void putSessionScope(AttributeConst key, V value) {
        request.getSession().setAttribute(key.getValue(), value);
    }

    /**
     * Remove pointed name's parameter from the session scope
     * @param key Parameter name
     */
    protected void removeSessionScope(AttributeConst key) {
        request.getSession().removeAttribute(key.getValue());
    }

    /**
     * Get value of the pointed parameter from the application scope and return it
     * @param key Parameter name
     * @return Value of parameter
     */
    @SuppressWarnings("unchecked")
    protected <R> R getContextScope(PropertyConst key) {
        return (R) context.getAttribute(key.getValue());
    }
}
