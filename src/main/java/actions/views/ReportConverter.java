package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Report;

/**
 * Class that mutual convert report data's DTO model and View model
 *
 */
public class ReportConverter {

    /**
     * Create instance of DTO model from instance of the View model
     * @param ev Instance of the ReportView
     * @return Instance of Report
     */
    public static Report toModel(ReportView rv) {
        return new Report(
                rv.getId(),
                EmployeeConverter.toModel(rv.getEmployee()),
                rv.getReportDate(),
                rv.getTitle(),
                rv.getContent(),
                rv.getCreatedAt(),
                rv.getUpdatedAt());
    }

    /**
     * Create instance of View model from instance of DTO model
     * @param e Instance of Report
     * @return instance of ReportView
     */
    public static ReportView toView(Report r) {
        if (r == null) {
            return null;
        }

        return new ReportView(
                r.getId(),
                EmployeeConverter.toView(r.getEmployee()),
                r.getReportDate(),
                r.getTitle(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt());
    }

    /**
     * Create list of View models from list of DTO models
     * @param list List of DTO models
     * @return List of View models
     */
    public static List<ReportView> toViewList(List<Report> list){
        List<ReportView> rvs = new ArrayList<>();

        for (Report r : list) {
            rvs.add(toView(r));
        }

        return rvs;
    }

    /**
     * Copy from content of all field of View model to field of DTO model
     * @param e DTO model(Copy to)
     * @param ev View model (Copy from)
     */
    public static void copyViewToModel(Report r, ReportView rv) {
        r.setId(rv.getId());
        r.setEmployee(EmployeeConverter.toModel(rv.getEmployee()));
        r.setReportDate(rv.getReportDate());
        r.setTitle(rv.getTitle());
        r.setContent(rv.getContent());
        r.setCreatedAt(rv.getCreatedAt());
        r.setUpdatedAt(rv.getUpdatedAt());

    }

}
