package domain.util;

public interface DomainFieldNodeCreator<T> {

    public DomainFieldNode createDomainFieldNode(T input);

}
