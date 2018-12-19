package cmd;

public class TypedStr {

    private int type;
    private String substr;

    public TypedStr(int type, String substr) {
        this.type = type;
        this.substr = substr;
    }

    public int getType() {
        return type;
    }

    public String getSubstr() {
        return substr;
    }
}
