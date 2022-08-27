package services;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.NoResultException;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import constants.JpaConst;
import models.Employee;
import models.validators.EmployeeValidator;
import utils.EncryptUtil;

/**
 * Class that process that relation to control of employee table
 */
public class EmployeeService extends ServiceBase {

    /**
     * Get data to show at list screen that pointed number of page and return list of EmployeeView
     * @param page Number of page
     * @return List that showing data
     */
    public List<EmployeeView> getPerPage(int page){
        List<Employee> employees = em.createNamedQuery(JpaConst.Q_EMP_GET_ALL, Employee.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return EmployeeConverter.toViewList(employees);
    }

    /**
     * Get number of data from employee table and return
     * @return Number of data at employee table
     */
    public long countAll() {
        long empCount = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT, Long.class)
                .getSingleResult();
        return empCount;
    }

    /**
     * Return the data acquired with employee number and password as an instance of EmployeeView
     * @param code employee number
     * @param plainPass Password string
     * @param pepper pepper character string
     * @return Acquired instance. In case failure to acquired then null
     */
    public EmployeeView fineOne(String code, String plainPass, String pepper) {
        Employee e = null;
        try {
            //Hash password
            String pass = EnctyptUtil.getPasswordEncrypt(plainPass, pepper);

            //Get 1 undeleted employee based on employee number and hashed password
            e = em.createNamedQuery(JpaConst.Q_EMP_GET_BY_CODE_AND_PASS, Employee.class)
                    .setParameter(JpaConst.JPQL_PARM_CODE, code)
                    .setParameter(JpaConst.JPQL_PARM_PASSWORD, pass)
                    .getSingleResult();
        }catch (NoResultException ex) {

        }

        return EmployeeConverter.toView(e);
    }
    /**
     * Acquire the number of data corresponding to the employee number and return it
     * @param code employee number
     * @return Number of data corresponded
     */
    public long countByCode(String code) {

        //Acquire number of item of employee that has pointed employee number
        long employees_count = (long) em.createNamedQuery(JpaConst.Q_EMP_COUNT_REGISTERED_BY_CODE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_CODE, code)
                .getSingleResult();
        return employees_count;
    }

    /**
     * Create one data based on the employee registration details entered from the screen and register it in the employee table
     * @param ev Registered content of employee that pointed on input at screen
     * @param pepper pepper character string
     * @return List of errors happened at validation or registration
     */
    public List <String> create(EmployeeView ev, String pepper){

        //Set password with hash
        String pass = EncryptUtil.getPasswordEncrypt(ev.getPassword(), pepper);
        ev.setPassword(pass);

        //Set current time at date registered and date updated
        LocalDateTime now = LocalDateTime.now();
        ev.setCreatedAt(now);
        ev.setUpdatedAt(now);

        //Validate registration content
        List <String> errors = EmployeeValidator.validate(this, ev, true, true);

        //Register the data if not validation errors
        if (errors.size() == 0) {
            create(ev);
        }

        //Return errors (In case no errors then empty list)
        return errors;
    }

    /**
     * Create one data from updated content of employee that pointed on input from screen, and update employee table
     * @param ev Registered content of pointed employee that input from the screen
     * @param pepper pepper character string
     * @return List of errors happened at validation or update processing
     */
    public List<String> update(EmployeeView ev, String pepper){

        //Acquire employee information that registered with ID as a condition
        EmployeeView savedEmp = findOne(ev.getId());

        boolean validateCode = false;
        if (!savedEmp.getCode().equals(ev.getCode())) {

            //In case update employee number

            //validate about employee number
            validateCode = true;
            //Set changed employee number
            savedEmp.setCode(ev.getCode());
        }

        boolean validatePass = false;
        if (ev.getPassword() != null && !ev.getPassword().equals("")) {
            //In case input on password

            //Validate about password
            validatePass = true;

            //Hash changed password and set it
            savedEmp.setPassword(
                    EncryptUtil.getPasswordEncrypt(ev.getPassword(), pepper));
        }

        savedEmp.setName(ev.getName()); //Set changed name
        savedEmp.setAdminFlag(ev.getAdminFlag()); //Set changed administrator flag

        //Set current time at date updated
        LocalDateTime today = LocalDateTime.now();
        savedEmp.setUpdatedAt(today);

        //Validate about update content
        List<String> errors = EmployeeValidator.validate(this, savedEmp, validateCode, validatePass);

        //In case no validation errors then update data
        if (errors.size() == 0) {
            update(savedEmp);
        }

        //Return error (No error then empty list)
        return errors;
    }

    /**
     * Logical delete employee data with ID as a condition
     * @param id
     */
    public void destroy(Integer id) {

        //Acquire registered employee information with ID as a condition
        EmployeeView savedEmp = findOne(id);

        //Set current time as updated
        LocalDateTime today = LocalDateTime.now();
        savedEmp.setUpdatedAt(today);

        //Enable logical delete flag
        savedEmp.setDeleteFlag(JpaConst.EMP_DEL_TRUE);

        //Updating
        update(savedEmp);

    }

    /**
     * Search with employee number and password as a condition then return result if acquired data
     * @param code Employee number
     * @param plainPass Password
     * @param pepper pepper character string
     * @return Return authentication result(Success: true Failure: false)
     */
    public Boolean validateLogin(String code, String plainPass, String pepper) {
        boolean isValidEmployee = false;
        if (code != null && !code.equals("") && plainPass != null && !plainPass.equals("")) {
            EmployeeView ev = findOne(code, plainPass, pepper);

            if (ev != null && ev.getId() != null) {
                //In case acquired data then authentication successfully
                isValidEmployee = true;
            }
        }
        return isValidEmployee;
    }

    /**
     * Acquire one data with ID as a condition and return instance of Employee
     * @param id
     * @return Instance of acquired data
     */
    private Employee findOneInternal(int id) {
        Employee e = em.find(Employee.class, id);

        return e;
    }

    /**
     * Register one employee data
     * @param ev employee data
     * @return Registration result
     */
    private void create(EmployeeView ev) {

        em.getTransaction().begin();
        em.persist(EmployeeConverter.toModel(ev));
        em.getTransaction().commit();
    }

    /**
     * Update employee data
     * @param ev Registration content of employee on input from the screen
     */
    private void update(EmployeeView ev) {
        em.getTransaction().begin();
        Employee e = findOneInternal(ev.getId());
        EmployeeConverter.copyViewToModel(e,  ev);
        em.getTransaction().commit();
    }

}
