package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;

/**
 * Action class that process relation to authentication
 *
 */
public class AuthAction extends ActionBase {

    private EmployeeService service;

    /**
     * Run method
     */
    @Override
    public void process() throws ServletException, IOException {
        service = new EmployeeService();

        //Run method
        invoke();

        service.close();
    }

    /**
     * Show login screen
     * @throws ServletException
     * @throws IOException
     */
    public void showLogin() throws ServletException, IOException {

        //Set a token for anti-CSRF
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //In case registered flush message at session then set request scope it
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //Show login screen
        forward(ForwardConst.FW_LOGIN);
    }

}
