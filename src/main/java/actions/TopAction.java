package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import services.ReportService;

/**
 * Action class that do process relation to top page
 *
 */
public class TopAction extends ActionBase {

    private ReportService service;

    /**
     * Run index method
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //Do method
        invoke();

        service.close();
    }

    /**
     * Show list screen
     */
    public void index() throws ServletException, IOException{

        //Acquire logging in employee information from session
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //Acquire report data created by logged-in employee for show at list screen that pointed number of page
        int page = getPage();
        List<ReportView> reports = service.getMinePerPage(loginEmployee, page);

        //Acquire number of report that created by logged-in employee
        long myReportsCount = service.countAllMine(loginEmployee);

        putRequestScope(AttributeConst.REPORTS, reports); //Acquired report data
        putRequestScope(AttributeConst.REP_COUNT, myReportsCount); //Number of report created by logged-in employee
        putRequestScope(AttributeConst.PAGE, page); //Number of page
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //Number of record for show at one page

        //In case set flush message at session then move to request scope it and remove from session
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //Show list screen
        forward(ForwardConst.FW_TOP_INDEX);
    }

}
