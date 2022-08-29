package services;

import java.time.LocalDateTime;
import java.util.List;

import actions.views.EmployeeConverter;
import actions.views.EmployeeView;
import actions.views.ReportConverter;
import actions.views.ReportView;
import constants.JpaConst;
import models.Report;
import models.validators.ReportValidator;

/**
 * Class that process that relation to control of daily report table
 */
public class ReportService extends ServiceBase {

    /**
     * Get data to show at list screen that pointed number of page and return list of ReportView
     * @param employee Employee
     * @param page Number of page
     * @return List that showing data
     */
    public List<ReportView> getMinePerPage(EmployeeView employee, int page){
        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL_MINE, Report.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * Get number of report data that pointed employee created and return it
     * @param employee
     * @return Number of report data
     */
    public long countAllMine(EmployeeView employee) {
        long Count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT_ALL_MINE, Long.class)
                .setParameter(JpaConst.JPQL_PARM_EMPLOYEE, EmployeeConverter.toModel(employee))
                .getSingleResult();
        return Count;
    }

    /**
     * Acquire report data for show at list screen of pointed number of page and return it with list of ReportView
     * @param page Number of page
     * @return List of data for list screen
     */
    public List<ReportView> getAllPerPage(int page) {

        List<Report> reports = em.createNamedQuery(JpaConst.Q_REP_GET_ALL, Report.class)
                .setFirstResult(JpaConst.ROW_PER_PAGE * (page - 1))
                .setMaxResults(JpaConst.ROW_PER_PAGE)
                .getResultList();
        return ReportConverter.toViewList(reports);
    }

    /**
     * Get number of data from report table and return it
     * @return Number of data
     */
    public long countAll() {
        long reports_count = (long) em.createNamedQuery(JpaConst.Q_REP_COUNT, Long.class)
                .getSingleResult();
        return reports_count;
    }

    /**
     * Return acquired data with ID as a condition with ReportView instance
     * @param id
     * @return Instance of acquired data
     */
    public ReportView findOne(int id) {
        return ReportConverter.toView(findOneInternal(id));
    }

    /**
     * Create one data based on the report registration details entered from the screen and register it on the report table
     * @param rv Registered content of the report
     * @return List of errors happened at validation
     */
    public List <String> create(ReportView rv){
        List <String> errors = ReportValidator.validate(rv);
        if (errors.size() == 0) {
            LocalDateTime ldt = LocalDateTime.now();
            rv.setCreatedAt(ldt);
            rv.setUpdatedAt(ldt);
            createInternal(rv);
        }

        //Return errors happened on validation (In case no errors then empty list)
        return errors;
    }

    /**
     * Update report data from inputed registration content from screen
     * @param rv Update content of report
     * @return List of errors happened at validation or update processing
     */
    public List<String> update(ReportView rv){

        //Do validation
        List<String> errors = ReportValidator.validate(rv);


        if (errors.size() == 0) {
            //Set current date at updated
            LocalDateTime ldt = LocalDateTime.now();
            rv.setUpdatedAt(ldt);

            updateInternal(rv);
        }

        //Return errors happened on validation (In case no errors then empty list)
        return errors;
    }

    /**
     * Acquire one data with ID as a condition
     * @param id
     * @return Instance of acquired data
     */
    private Report findOneInternal(int id) {
        return em.find(Report.class, id);
    }

    /**
     * Register one report data
     * @param rv report data
     */
    private void createInternal(ReportView rv) {

        em.getTransaction().begin();
        em.persist(ReportConverter.toModel(rv));
        em.getTransaction().commit();
    }

    /**
     * Update report data
     * @param rv Report data
     */
    private void updateInternal(ReportView rv) {
        em.getTransaction().begin();
        Report r = findOneInternal(rv.getId());
        ReportConverter.copyViewToModel(r, rv);
        em.getTransaction().commit();
    }

}
