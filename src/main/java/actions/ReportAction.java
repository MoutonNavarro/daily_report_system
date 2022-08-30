package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

/**
 * Action class that processing relation to daily report
 *
 */
public class ReportAction extends ActionBase {

    private ReportService service;

    /**
     * Do method
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

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
        //Acquire report data for show list screen that pointed number of page
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //Acquire all number of report data
        long reportCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports); //Acquired report data
        putRequestScope(AttributeConst.REP_COUNT, reportCount); //Number of all report data
        putRequestScope(AttributeConst.PAGE, page); //Number of page
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //Number of record that showing at 1 page

        //In case set flush message at session then move to request scope and remove from session
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //show list screen
        forward(ForwardConst.FW_REP_INDEX);

    }

    /**
     * Show new registration screen
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException, IOException{

        putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF

        //Set date of report = date of today to empty instance of daily report information
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv); //Report instance that has set only date

        //Show new registration screen
        forward(ForwardConst.FW_REP_NEW);
    }
//
//    /**
//     * Do new registration
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void create() throws ServletException, IOException{
//
//        //Check administrator flag and check token for anti-CSRF
//        if(checkAdmin() && checkToken()) {
//
//            //Create instance of employee information from parameter value
//            EmployeeView ev = new EmployeeView(
//                    null,
//                    getRequestParam(AttributeConst.EMP_CODE),
//                    getRequestParam(AttributeConst.EMP_NAME),
//                    getRequestParam(AttributeConst.EMP_PASS),
//                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
//                    null,
//                    null,
//                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
//
//            //Acquire pepper character string from application scope
//            String pepper = getContextScope(PropertyConst.PEPPER);
//
//            //Employee information
//            List<String> errors = service.create(ev, pepper);
//
//            if (errors.size() > 0) {
//                //In case error happened
//
//                putRequestScope(AttributeConst.TOKEN, getTokenId()); //The token for anti-CSRF
//                putRequestScope(AttributeConst.EMPLOYEE, ev); //Inputed employee information
//                putRequestScope(AttributeConst.ERR, errors); //List of errors
//
//                //Re-show new registration screen
//                forward(ForwardConst.FW_EMP_NEW);
//
//            }else {
//                //In case no errors until registering
//
//                //Set flush message about register complete at the session
//                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERD.getMessage());
//
//                //Redirect to list screen
//                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
//            }
//        }
//    }
//
//    /**
//     * Show detail screen
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void show() throws ServletException, IOException{
//
//        //Check administrator flag
//        if(checkAdmin()) {
//            //Acquire employee data with ID as a condition
//            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));
//
//            if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {
//
//                //In case failure to acquire a data or it has logical deleted then show error screen
//                forward(ForwardConst.FW_ERR_UNKNOWN);
//                return;
//            }
//
//            putRequestScope(AttributeConst.EMPLOYEE, ev); //Acquired employee information
//
//            //Show detail screen
//            forward(ForwardConst.FW_EMP_SHOW);
//        }
//    }
//
//    /**
//     * Show edit screen
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void edit() throws ServletException, IOException{
//
//        //Check administrator flag
//        if(checkAdmin()) {
//           //Acquire employee data with ID as a condition
//            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));
//
//            if (ev ==null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {
//
//                //In case failure to acquire the data or that has been logical deleted then show error screen.
//                forward(ForwardConst.FW_ERR_UNKNOWN);
//                return;
//            }
//
//            putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
//            putRequestScope(AttributeConst.EMPLOYEE, ev); //Acquired employee information
//
//            //Show edit screen
//            forward(ForwardConst.FW_EMP_EDIT);
//        }
//    }
//    /**
//     * Do update
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void update() throws ServletException, IOException {
//
//        //Check administrator flag and check token for anti-CSRF
//        if(checkAdmin() && checkToken()) {
//            //Create instance of employee information from value of parameter
//            EmployeeView ev = new EmployeeView(
//                    toNumber(getRequestParam(AttributeConst.EMP_ID)),
//                    getRequestParam(AttributeConst.EMP_CODE),
//                    getRequestParam(AttributeConst.EMP_NAME),
//                    getRequestParam(AttributeConst.EMP_PASS),
//                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
//                    null,
//                    null,
//                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());
//
//            //Acquire pepper character string from application scope
//            String pepper = getContextScope(PropertyConst.PEPPER);
//
//            //Update employee information
//            List<String> errors = service.update(ev,  pepper);
//
//            if (errors.size() > 0) {
//                //Errors happened until updating
//
//                putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
//                putRequestScope(AttributeConst.EMPLOYEE, ev); //Inputed employee information
//                putRequestScope(AttributeConst.ERR, errors); //List of errors
//
//                //Re-show edit screen
//                forward(ForwardConst.FW_EMP_EDIT);
//            }else {
//                //In case no errors happened until updating
//
//                //Set flush message about update complete at session
//                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());
//
//                //Redirect to list screen
//                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
//            }
//        }
//    }
//
//    /**
//     * Do logical delete
//     * @throws ServletException
//     * @throws IOException
//     */
//    public void destroy() throws ServletException, IOException{
//        //Check administrator flag and check token for anti-CSRF
//        if(checkAdmin() && checkToken()) {
//            //Logical delete employee data with ID as a condition
//            service.destroy(toNumber(getRequestParam(AttributeConst.EMP_ID)));
//
//            //Set flush message about delete complete at session
//            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());
//
//            //Redirect to list screen
//            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
//        }
//    }
//    /**
//     * Check logged in employee is administrator and when it is not administrator show error screen
//     * true: Administrator, false: not administrator
//     * @throws ServletException
//     * @throws IOException
//     */
//    private boolean checkAdmin() throws ServletException, IOException{
//
//        //Acquire logged in employee information from session
//        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);
//
//        //If not administrator show error screen
//        if (ev.getAdminFlag() != AttributeConst.ROLE_ADMIN.getIntegerValue()) {
//
//            forward(ForwardConst.FW_ERR_UNKNOWN);
//            return false;
//
//        }else {
//            return true;
//        }
//    }

}
