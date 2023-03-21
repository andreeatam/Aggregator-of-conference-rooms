package repo.jpa;

import interfaces.ChatRepositoryInterface;
import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class OfferRepositoryJPA implements ChatRepositoryInterface<Offer, Integer> {
    private final EntityManager manager;

    public OfferRepositoryJPA(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void add(Offer newOffer) {
        manager.getTransaction().begin();
        Product product = manager.merge(newOffer.getProduct());
        BusinessOwner sender = manager.merge(newOffer.getSender());
        Organiser receiver = manager.merge(newOffer.getReceiver());
        newOffer.setSender(sender);
        newOffer.setReceiver(receiver);
        newOffer.setProduct(product);
        manager.persist(newOffer);
        manager.getTransaction().commit();
    }

    @Override
    public void remove(Integer id) {
        Offer offer = findById(id);
        if(offer != null) {
            manager.getTransaction().begin();
            manager.remove(offer);
            manager.getTransaction().commit();
        }

    }

    @Override
    public void updateStatus(Offer offer, Status newStatus) {
        if(offer != null) {
            manager.getTransaction().begin();
            manager.merge(offer).setStatus(newStatus);
            BusinessOwner sender = manager.merge(offer.getSender());
            Organiser receiver = manager.merge(offer.getReceiver());
            manager.merge(offer).setSender(sender);
            manager.merge(offer).setReceiver(receiver);
            manager.getTransaction().commit();

        }

    }

    @Override
    public Offer findById(Integer id) {
        return manager.find(Offer.class, id);
    }


}
