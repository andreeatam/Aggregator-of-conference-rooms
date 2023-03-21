package interfaces;


import model.Status;

public interface ChatRepositoryInterface<E, ID>{
    void add(E entity);

    void remove(ID id);

    void updateStatus(E entity, Status status);

    E findById(ID id);
}
