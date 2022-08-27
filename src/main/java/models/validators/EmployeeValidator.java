package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.EmployeeView;
import constants.MessageConst;
import services.EmployeeService;

/**
 * Class that validate value that has set at employee's instance
 *
 */
public class EmployeeValidator {

    /**
     * Do validation about each items of the employee's instance
     * @param service Original instance of Service class
     * @param ev Instance of employeeView
     * @param codeDuplicateCheckFlag Is enable to check duplicate about employee number (check: true no check: false)
     * @param passwordCheckFlag Is enable to check to password input (check: true no check: false)
     * @return List of errors
     */
    public static List<String> validate(
            EmployeeService service, EmployeeView ev, Boolean codeDuplicateCheckFlag, Boolean passwordCheckFlag){
        List<String> errors = new ArrayList<String>();

        //Check employee number
        String codeError = validateCode(service, ev.getCode(), codeDuplicateCheckFlag);
        if (!codeError.equals("")) {
            errors.add(codeError);
        }

        //Check name
        String nameError = validateName(ev.getName());
        if (!nameError.equals("")) {
            errors.add(nameError);
        }

        //Check password
        String passError = validatePassword(ev.getPassword(), passwordCheckFlag);
        if (!passError.equals("")) {
            errors.add(passError);
        }

        return errors;
    }
    /**
     * Check employee number input and return error message
     * @param service Instance of EmployeeService
     * @param code Employee number
     * @param codeDuplicateCheckFlag Is enable to check duplicate about employee number (check: true no check: false)
     * @return Error message
     */
    private static String validateCode(EmployeeService service, String code, Boolean codeDuplicateCheckFlag) {
        //Return error message if input value is null
        if (code == null || code.equals("")) {
            return MessageConst.E_NOEMP_CODE.getMessage();
        }
        if (codeDuplicateCheckFlag) {
            //Check duplicate employee number

            long employeesCount = isDuplicateEmployee(service, code);

            //In case of same as that employee number is already registered then return error message
            if (employeesCount > 0) {
                return MessageConst.E_EMP_CODE_EXIST.getMessage();
            }
        }

        //No errors then return empty string
        return "";
    }
    /**
     * @param service EmployeeService's instance
     * @param code Employee number
     * @return Number of data that same as pointed employee number registered at employee table
     */
    private static long isDuplicateEmployee(EmployeeService service, String code) {

        long employeesCount = service.countByCode(code);
        return employeesCount;
    }

    /**
     * Check input value in name and if no value then return error message
     * @param name Name
     * @return Error message
     */
    private static String validateName(String name) {
        if (name == null || name.equals("")) {
            return MessageConst.E_NONAME.getMessage();
        }

        //In case valid input value then return empty string
        return "";
    }

    /**
     * Check password input and return error message
     * @param password Password
     * @param passwordCheckFlag enable to check to input check of password (check: true no check: false)
     * @return Error message
     */
    private static String validatePassword (String password, Boolean passwordCheckFlag) {

        //Input check and if no input value then return error message
        if (passwordCheckFlag && (password == null || password.equals(""))) {
            return MessageConst.E_NOPASSWORD.getMessage();
        }

        //No error then return empty string
        return "";
    }
}
