package actions.views;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View model that handlings input / output at screen about report's information
 *
 */
@Getter //Auto create getter about all class's field(Lombok)
@Setter //Auto create setter about all class's field(Lombok)
@NoArgsConstructor //Auto create no argument constructor(Lombok)
@AllArgsConstructor //Auto create argument constructors that have all class's field(Lombok)
public class ReportView {

    /**
     * id
     */
    private Integer id;

    /**
     * Employee whose registered the report
     */
    private EmployeeView employee;

    /**
     * Date that indicates someday the report
     */
    private LocalDate reportDate;

    /**
     * Title of the report
     *
     */
    private String title;

    /**
     * Content of the report
     */
    private String content;

    /**
     * Date registered
     */
    private LocalDateTime createdAt;

    /**
     * Date updated
     */
    private LocalDateTime updatedAt;

}
