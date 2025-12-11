package domain.util;

public enum PrimitiveType {

    BOOLEAN("boolean"),
    CHAR("char"),
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    FLOAT("float"),
    LONG("long"),
    DOUBLE("double"),
    VOID("void");

    private PrimitiveType(String name) {

    }

    public static PrimitiveType tryGetValue(String input) {
        try {
            return PrimitiveType.valueOf(input.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static boolean hasValue(String input) {
        try {
            PrimitiveType.valueOf(input.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
