package actions;
import java.io.IOException;

import javax.servlet.ServletException;

import constants.ForwardConst;

/**
 * Action class that process for error happen
 *
 */
public class UnknownAction extends ActionBase{
    /**
     * Show common error screen "The page is not found"
     */
    @Override
    public void process() throws ServletException, IOException{
        //Show error screen
        forward(ForwardConst.FW_ERR_UNKNOWN);
    }
}
