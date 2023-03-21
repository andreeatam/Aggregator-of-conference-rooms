package interfaces;

public interface ICrudRepositoryInterface<E, ID> {

    void add(E entity);

    void remove(ID id);

    void update(ID id, E newEntity);

    E findById(ID id);

}
