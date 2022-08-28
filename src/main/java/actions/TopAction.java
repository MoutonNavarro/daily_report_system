package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;

/**
 * Action class that do process relation to top page
 *
 */
public class TopAction extends ActionBase {

    /**
     * Run index method
     */
    @Override
    public void process() throws ServletException, IOException {

        //Do method
        invoke();
    }

    /**
     * Show list screen
     */
    public void index() throws ServletException, IOException{
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
