package constants;

/**
 * Enum class that defined application scope's parameter name
 *
 */

public enum PropertyConst {
    //Pepper character string
    PEPPER("pepper");

    private final String text;
    private PropertyConst(final String text) {
        this.text= text;
    }

    public String getValue() {
        return this.text;
    }
}
