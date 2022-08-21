package actions.views;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * View model that handlings input / output at screen about employee's information
 *
 */
@Getter //Auto create getter about all class's field(Lombok)
@Setter //Auto create setter about all class's field(Lombok)
@NoArgsConstructor //Auto create no argument constructor(Lombok)
@AllArgsConstructor //Auto create argument constructors that have all class's field(Lombok)
public class EmployeeView {

    /**
     * id
     */
    private Integer id;

    /**
     * Employee's number
     */
    private String code;

    /**
     * Name
     */
    private String name;

    /**
     * Password
     */
    private String password;

    /**
     * Has a admin right (General: 0, Administrator: 1)
     */
    private Integer adminFlag;

    /**
     * Date registered
     */
    private LocalDateTime createdAt;

    /**
     * Date updated
     */
    private LocalDateTime updatedAt;

    /**
     * Is deleted(Active: 0, Deleted: 1)
     */
    private Integer deleteFlag;
}
