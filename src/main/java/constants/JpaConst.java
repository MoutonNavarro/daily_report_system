package constants;

/**
 * Interface that defined item value that relation of DB
 * *If you put variable at interface then its mean "public static final" decorator automaticly.
 *
 */

public interface JpaConst {

    //Persistence-unit name
    String PERSISTENCE_UNIT_NAME = "daily_report_system";

    //Maximum number of get data
    int ROW_PER_PAGE = 15;

    //Employees table
    String TABLE_EMP = "employees"; //Table name
    //Employees table column
    String EMP_COL_ID = "id"; //ID
    String EMP_COL_CODE = "code"; //Employee's number
    String EMP_COL_NAME = "name"; //Name
    String EMP_COL_PASS = "password"; //Password
    String EMP_COL_ADMIN_FLAG = "admin_flag"; //Admin rights
    String EMP_COL_CREATED_AT = "created_at"; //Date of created
    String EMP_COL_UPDATED_AT = "updated_at"; //Date of updated
    String EMP_COL_DELETE_FLAG = "deleted_flag"; //Logical deleted flag

    int ROLE_ADMIN = 1; //Admin rights on (Administrator)
    int ROLE_GENERAL = 0; //Admin rights off (General user)
    int EMP_DEL_TRUE = 1; //Logical deleted flag on (deleted)
    int EMP_DEL_FALSE = 0; //Logical deleted flag off (active)

    //Daily report table
    String TABLE_REP = "reports"; //Table name
    //Daily report table column
    String REP_COL_ID = "id"; //ID
    String REP_COL_EMP = "employee_id"; //Employee's id that created the daily report
    String REP_COL_REP_DATE = "report_date"; //Date of reported
    String REP_COL_TITLE = "title"; //Title of the daily report
    String REP_COL_CONTENT = "content"; //Content of daily report
    String REP_COL_CREATED_AT = "created_at"; //Date of registered
    String REP_COL_UPDATED_AT = "updated_at"; //Date of updated

    //Entity name
    String ENTITY_EMP = "employee"; //Employees
    String ENTITY_REP = "report"; //Daily report

    //Parameter at the JPQL
    String JPQL_PARM_CODE = "code"; //Employee number
    String JPQL_PARM_PASSWORD = "password"; //Password
    String JPQL_PARM_EMPLOYEE = "employee"; //Employee

    //NamedQuery's name and query
    //Get all employees with reverse order with its ID
    String Q_EMP_GET_ALL = ENTITY_EMP + ".getAll"; //name
    String Q_EMP_GET_ALL_DEF = "SELECT e FROM Employee AS e ORDER BY e.id DESC"; //query
    //Get number of all employees
    String Q_EMP_COUNT = ENTITY_EMP + ".count";
    String Q_EMP_COUNT_DEF = "SELECT COUNT(e) FROM Employee AS e";
    //Get not deleted employees that condition to employee's ID and hashed password
    String Q_EMP_GET_BY_CODE_AND_PASS = ENTITY_EMP + ".getByCodeAndPass";
    String Q_EMP_GET_BY_CODE_AND_PASS_DEF = "SELECT e FROM Employee AS e WHERE e.deleteFlag = 0 AND e.code = :" + JPQL_PARM_CODE + " AND e.password = :" + JPQL_PARM_PASSWORD;
    //Get number of employees that pointed employee number
    String Q_EMP_COUNT_REGISTERED_BY_CODE = ENTITY_EMP + ".countRegisterByCode";
    String Q_EMP_COUNT_REGISTERED_BY_CODE_DEF = "SELECT COUNT(e) FROM Employee AS e WHERE e.code = :" + JPQL_PARM_CODE;
    //Get all reports with reverse order with its ID
    String Q_REP_GET_ALL = ENTITY_REP + ".getAll";
    String Q_REP_GET_ALL_DEF = "SELECT r FROM Report AS r ORDER BY r.id DESC";
    //Get report that pointed employee's created with reverse order with all ID
    String Q_REP_GET_ALL_MINE = ENTITY_REP + ".getAllMine";
    String Q_REP_GET_ALL_MINE_DEF = "SELECT r FROM Reoirt AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE + " ORDER BY r.id DESC";
    //Get number of reports that pointed employee's created
    String Q_REP_COUNT_ALL_MINE = ENTITY_REP + "countAllMine";
    String Q_REP_COUNT_ALL_MINE_DEF = "SELECT COUNT(r) FROM Report AS r WHERE r.employee = :" + JPQL_PARM_EMPLOYEE;
}
