package domain.stylecheck;

interface ClassStyleChecker {

    boolean checkClassName(String name);

    boolean checkVariableOrMethodName(String name);

    boolean checkStaticConstantName(String name);

}
