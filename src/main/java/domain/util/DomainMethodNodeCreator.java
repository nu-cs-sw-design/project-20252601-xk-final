package domain.util;

public interface DomainMethodNodeCreator<T> {

    public DomainMethodNode createDomainMethodNode(T input, String localClassName);

}
