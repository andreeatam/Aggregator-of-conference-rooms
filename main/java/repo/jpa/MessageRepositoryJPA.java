package repo.jpa;

import interfaces.ChatRepositoryInterface;
import interfaces.ICrudRepositoryInterface;
import model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MessageRepositoryJPA implements ChatRepositoryInterface<Message, Integer> {
    private final EntityManager manager;

    public MessageRepositoryJPA(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public void add(Message newMessage) {

        manager.getTransaction().begin();
        Product product = manager.merge(newMessage.getProduct());
        Organiser sender = manager.merge(newMessage.getSender());
        BusinessOwner receiver = manager.merge(newMessage.getReceiver());
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setProduct(product);
        manager.persist(newMessage);
        manager.getTransaction().commit();
    }

    @Override
    public void remove(Integer id) {
        Message message = findById(id);
        if(message != null) {
            manager.getTransaction().begin();
            manager.remove(message);
            manager.getTransaction().commit();
        }

    }

    @Override
    public void updateStatus(Message message, Status newStatus) {
        if(message != null) {
            manager.getTransaction().begin();
            Organiser sender = manager.merge(message.getSender());
            BusinessOwner receiver = manager.merge(message.getReceiver());
            manager.merge(message).setSender(sender);
            manager.merge(message).setReceiver(receiver);
            manager.merge(message).setStatus(newStatus);

            manager.getTransaction().commit();
        }
    }

    @Override
    public Message findById(Integer id) {

        Message message =  manager.find(Message.class, id);
        return message;
    }


}
