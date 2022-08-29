package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

/**
 * Class that validate value that has set at report's instance
 *
 */
public class ReportValidator {

    /**
     * Do validation about each items of the report's instance
     * @param rv Instance of ReportView
     * @return List of errors
     */
    public static List<String> validate(ReportView rv){
        List<String> errors = new ArrayList<String>();

        //Check title
        String titleError = validateTitle(rv.getTitle());
        if (!titleError.equals("")) {
            errors.add(titleError);
        }

        //Check content
        String contentError = validateContent(rv.getContent());
        if (!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;
    }

    /**
     * Check title input and return error message
     * @param title Title
     * @return Error message
     */
    private static String validateTitle(String title) {
        if (title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();
        }

        //No errors then return empty string
        return "";
    }

    /**
     * Check input value in content and if no value then return error message
     * @param content Content
     * @return Error message
     */
    private static String validateContent(String content) {
        if (content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();
        }

        //In case valid input value then return empty string
        return "";
    }

}
