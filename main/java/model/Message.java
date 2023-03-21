package model;

import javax.persistence.*;
import java.util.List;

//cls Message are un anunt, (idMessage,count), descr,startDate,endDate,nrInvitati, status
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMessage; //final -> poate fi initializ doar 1 data oriunde in clasa + valoarea variab nu poate fi schimbata
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idProduct")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "idOrganiser")
    private Organiser sender;
    @ManyToOne
    @JoinColumn(name = "idBusinessOwner")
    private BusinessOwner receiver;
    private String description;
    private String startingDate;
    private String endingDate;
    private Integer guests;
    @Enumerated(EnumType.STRING)
    private Status status;

    public Message(Product product, Organiser sender, BusinessOwner receiver, String startingDate, String endingDate, Integer guests, String description) {
        this.product = product;
        this.sender = sender;
        this.receiver = receiver;
        this.startingDate = startingDate;
        this.endingDate = endingDate;
        this.guests = guests;
        this.description = description;

        this.status = null;
    }

    public Message() {

    }

    public Organiser getSender() {
        return sender;
    }

    public BusinessOwner getReceiver() {
        return receiver;
    }

    public void setSender(Organiser sender) {
        this.sender = sender;
    }

    public void setReceiver(BusinessOwner receiver) {
        this.receiver = receiver;
    }

    public Integer getIdMessage() {
        return idMessage;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public String getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(String endingDate) {
        this.endingDate = endingDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }


}

