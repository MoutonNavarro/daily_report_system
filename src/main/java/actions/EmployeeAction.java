package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;

/**
 * Action class that processing about employee
 *
 */
public class EmployeeAction extends ActionBase {

    private EmployeeService service;

    /**
     * Do method
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        //Run method
        invoke();

        service.close();
    }

    /**
     * Show list screen
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException{

        //Acquire data for show list screen that pointed number of page
        int page = getPage();
        List<EmployeeView> employees = service.getPerPage(page);

        //Acquire all number of employee data
        long employeeCount = service.countAll();

        putRequestScope(AttributeConst.EMPLOYEES, employees); //Acquired employee data
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount); //Number of all employee data
        putRequestScope(AttributeConst.PAGE, page); //Number of page
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //Number of record that showing at 1 page

        //In case set flush message at session then move to request scope and remove from session
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //show list screen
        forward(ForwardConst.FW_EMP_INDEX);
    }

    /**
     * Show new registration screen
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException{

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
        putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView()); //Empty employee instance

        //Show new registration screen
        forward(ForwardConst.FW_EMP_NEW);
    }

    /**
     * Do new registration
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //Anti-CSRF token check
        if (checkToken()) {

            //Create instance of employee information from parameter value
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            //Acquire pepper character string from application scope
            String pepper = getContextScope(PropertyConst.PEPPER);

            //Employee information
            List<String> errors = service.create(ev, pepper);

            if (errors.size() > 0) {
                //In case error happened

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //The token for anti-CSRF
                putRequestScope(AttributeConst.EMPLOYEE, ev); //Inputed employee information
                putRequestScope(AttributeConst.ERR, errors); //List of errors

                //Re-show new registration screen
                forward(ForwardConst.FW_EMP_NEW);

            }else {
                //In case no errors until registering

                //Set flush message about register complete at the session
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERD.getMessage());

                //Redirect to list screen
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * Show detail screen
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException{

        //Acquire employee data with ID as a condition
        EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            //In case failure to acquire a data or it has logical deleted then show error screen
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }

        putRequestScope(AttributeConst.EMPLOYEE, ev); //Acquired employee information

        //Show detail screen
        forward(ForwardConst.FW_EMP_SHOW);
    }

    /**
     * Show edit screen
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException{

        //Acquire employee data with ID as a condition
        EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if (ev ==null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            //In case failure to acquire the data or that has been logical deleted then show error screen.
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
        putRequestScope(AttributeConst.EMPLOYEE, ev); //Acquired employee information

        //Show edit screen
        forward(ForwardConst.FW_EMP_EDIT);
    }

}
