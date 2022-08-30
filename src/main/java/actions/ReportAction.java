package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
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

    /**
     * Do new registration
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //Anti-CSRF check token
        if(checkToken()) {

            //In case it hasn't input date then set today's date
            LocalDate day = null;
            if (getRequestParam(AttributeConst.REP_DATE) == null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")) {
                day = LocalDate.now();
            }else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }
            //Acquire logging in employee information from session
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //Create instance of report information from value of the parameter
            ReportView rv = new ReportView(
                    null,
                    ev,//Register logging in employee as who create the report
                    day,
                    getRequestParam(AttributeConst.REP_TITLE),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null);

            //Register daily report information
            List<String> errors = service.create(rv);

            if (errors.size() > 0) {
                //In case error happened

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //The token for anti-CSRF
                putRequestScope(AttributeConst.REPORT, rv); //Inputed daily report information
                putRequestScope(AttributeConst.ERR, errors); //List of errors

                //Re-show new registration screen
                forward(ForwardConst.FW_EMP_NEW);

            }else {
                //In case no errors until registering

                //Set flush message about register complete at the session
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERD.getMessage());

                //Redirect to list screen
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    /**
     * Show detail screen
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException{

        //Acquire report data with ID as a condition
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if (rv == null) {

            //In case pointed report data is not exist then show error screen
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }else {
            putRequestScope(AttributeConst.REPORT, rv); //Acquired daily report data

            //Show detail screen
            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    /**
     * Show edit screen
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException{

       //Acquire report data with ID as a condition
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //Acquire logging in employee information from session
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv ==null || ev.getId() != rv.getEmployee().getId()) {

            //In case pointed report hasn't exist or
            //logging in employee is not match to who is created the report then show error screen
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }else {
            putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
            putRequestScope(AttributeConst.REPORT, rv); //Acquired report information

            //Show edit screen
            forward(ForwardConst.FW_REP_EDIT);
        }

    }
    /**
     * Do update
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //Anti-CSRF, check the token
        if(checkToken()) {

            //Acquire daily report data with ID as a condition
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //Set input the report content
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            //Update the report data
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //Errors happened until updating

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //Token for anti-CSRF
                putRequestScope(AttributeConst.REPORT, rv); //Input the report information
                putRequestScope(AttributeConst.ERR, errors); //List of errors

                //Re-show edit screen
                forward(ForwardConst.FW_REP_EDIT);
            }else {
                //In case no errors happened until updating

                //Set flush message about update complete at session
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //Redirect to list screen
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

}
