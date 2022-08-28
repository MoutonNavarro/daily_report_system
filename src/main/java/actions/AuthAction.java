package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.MessageConst;
import constants.PropertyConst;
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

    /**
     * Do process login
     * @throws ServletException
     * @throws IOException
     */
    public void login() throws ServletException, IOException{

        String code = getRequestParam(AttributeConst.EMP_CODE);
        String plainPass = getRequestParam(AttributeConst.EMP_PASS);
        String pepper = getContextScope(PropertyConst.PEPPER);

        //Authenticate valid employee
        Boolean isValidEmployee = service.validateLogin(code, plainPass, pepper);

        if (isValidEmployee) {
            //In case authentication successful

            //Anti-CSRF check token
            if (checkToken()) {

                //Acquire DB data of the logged in employee
                EmployeeView ev = service.findOne(code, plainPass, pepper);
                //Set logged in employee at session
                putSessionScope(AttributeConst.LOGIN_EMP, ev);
                //Set flush message about logged in completely at session
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_LOGINED.getMessage());
                //Redirect to top page
                redirect(ForwardConst.ACT_TOP, ForwardConst.CMD_INDEX);
            }
        }else {
            //In case authentication failure

            //Set token for anti-CSRF
            putRequestScope(AttributeConst.TOKEN, getTokenId());
            //Set show authentication failure error message
            putRequestScope(AttributeConst.LOGIN_ERR, true);
            //Set inputed employee code
            putRequestScope(AttributeConst.EMP_CODE, code);

            //Show login screen
            forward(ForwardConst.FW_LOGIN);
        }
    }

}
