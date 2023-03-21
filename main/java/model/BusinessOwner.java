package model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "businessOwners")
public class BusinessOwner extends User {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "idBusinessOwner")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Offer> sentOffers = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<Message> receivedMessages = new ArrayList<>();

    public BusinessOwner(String firstName, String lastName, String username, String password) {
        super(firstName, lastName, username, password);
    }

    public BusinessOwner() {

    }

    @Override
    public String toString() {
        return "BusinessOwner{ " +
                "username=" + getUsername() +
                ", products=" + products +
                ", sentOffers=" + sentOffers +
                ", receivedMessages=" + receivedMessages +
                '}';
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Offer> getSentOffers() {
        return sentOffers;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }


}
