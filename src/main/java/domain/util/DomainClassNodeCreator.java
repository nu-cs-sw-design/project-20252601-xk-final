package domain.util;

public interface DomainClassNodeCreator<T> {

    public DomainClassNode createDomainClassNode(T input);

}
