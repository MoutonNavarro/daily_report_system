package constants;

/**
 * Enum class that defined each output message
 *
 */
public enum MessageConst {

    //Authentication
    I_LOGINED("You logged in"),
    E_LOGINED("Failure to login"),
    I_LOGOUT("You logged out"),

    //DB update
    I_REGISTERD("Registered complete"),
    I_UPDATED("Update complete"),
    I_DELETED("Delete complete"),

    //Validation
    E_NONAME("Please input name."),
    E_NOPASSWORD("Please input password."),
    E_NOEMP_CODE("Please input employee number."),
    E_EMP_CODE_EXIST("Your input employee number is already existed."),
    E_NOTITLE("Please input title."),
    E_NOCONTENT("Please input content.");

    /**
     * Character string
     */
    private final String text;

    /**
     * Constructor
     */
    private MessageConst(final String text) {
        this.text = text;
    }

    /**
     * Get value(Character string)
     */
    public String getMessage() {
        return this.text;
    }
}
