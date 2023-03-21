package repo.jpa;

import interfaces.OrganiserRepositoryInterface;
import model.Organiser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class OrganiserRepositoryJPA implements OrganiserRepositoryInterface {

    private EntityManager manager;

    public OrganiserRepositoryJPA(EntityManager manager) {
        this.manager = manager;
    }

    public OrganiserRepositoryJPA() {

    }

    @Override
    public void add(Organiser newOrganiser){

        Organiser organiser = findById(newOrganiser.getUsername());
        if(organiser == null) {
            manager.getTransaction().begin();
            manager.persist(newOrganiser);
            manager.getTransaction().commit();
        }

    }

    @Override
    public void remove(String username){
        Organiser organiser = findById(username);
        if(organiser != null) {
            manager.getTransaction().begin();
            manager.remove(organiser);
            manager.getTransaction().commit();
        }

    }

    @Override
    public void update(String username, Organiser newOrganiser){

        Organiser organiser = findById(username);
        if(organiser != null) {
            manager.getTransaction().begin();
            organiser.setFirstName(newOrganiser.getFirstName());
            organiser.setLastName(newOrganiser.getLastName());
            organiser.setPassword(newOrganiser.getPassword());
            manager.getTransaction().commit();
        }
    }

    @Override
    public Organiser findById(String username) {

        Organiser organiser = manager.find(Organiser.class, username);
        if(organiser != null) {
            manager.refresh(organiser);
        }
        return organiser;
    }

    @Override
    public Organiser findByUsernameAndPassword(String username, String password) {
        Organiser organiser = findById(username);

        if(organiser != null) {
            if(organiser.getPassword().equals(password)) {
                return organiser;
            }
        }
        return null;
    }

//

    public Organiser findOrganiserByMessageId(Integer idMessage) {
        Query query = manager.createNativeQuery("SELECT idOrganiser FROM messages WHERE idMessage = " + idMessage);
        return (Organiser) manager.find(Organiser.class, (String)query.getSingleResult());
    }
}
