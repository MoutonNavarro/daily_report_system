package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
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

}
