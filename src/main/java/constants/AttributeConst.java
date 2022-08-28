package constants;

public enum AttributeConst {
    //Flush message
    FLUSH("flush"),

    //Common show screen
    MAX_ROW("maxRow"),
    PAGE("page"),

    //Common input form
    TOKEN("_token"),
    ERR("errors"),

    //Employee that logged in
    LOGIN_EMP("login_employee"),

    //Login screen
    LOGIN_ERR("loginError"),

    //Employees management
    EMPLOYEE("employee"),
    EMPLOYEES("employees"),
    EMP_COUNT("employees_count"),
    EMP_ID("id"),
    EMP_CODE("code"),
    EMP_PASS("password"),
    EMP_NAME("name"),
    EMP_ADMIN_FLG("admin_flag"),

    //Administrator flag
    ROLE_ADMIN(1),
    ROLE_GENERAL(0),

    //Delete flag
    DEL_FLAG_TRUE(1),
    DEL_FLAG_FALSE(0),

    //Daily reports management
    REPORT("report"),
    REPORTS("reports"),
    REP_COUNT("reports_count"),
    REP_ID("id"),
    RREP_DATE("report_date"),
    REP_TITLE("title"),
    REP_CONTENT("content_msg");

    private final String text;
    private final Integer i;

    private AttributeConst(final String text) {
        this.text = text;
        this.i = null;
    }

    private AttributeConst(final Integer i) {
        this.text = null;
        this.i = i;
    }

    public String getValue() {
        return this.text;
    }
    public Integer getIntegerValue() {
        return this.i;
    }
}
