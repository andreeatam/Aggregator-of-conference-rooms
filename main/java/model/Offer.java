package model;

import javax.persistence.*;

@Entity
@Table(name = "offers")
public class Offer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idBusinessOwner")
    private BusinessOwner sender;

    @ManyToOne
    @JoinColumn(name = "idOrganiser")
    private Organiser receiver;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idProduct")
    private Product product;
    private Integer price;

    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;



    public Offer(BusinessOwner sender, Organiser receiver, Product product, Integer price, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.price = price;
        this.description = description;
        this.product = product;
    }

    public Offer() {

    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BusinessOwner getSender() {
        return sender;
    }

    public void setSender(BusinessOwner sender) {
        this.sender = sender;
    }

    public void setReceiver(Organiser receiver) {
        this.receiver = receiver;
    }

    public Organiser getReceiver() {
        return receiver;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
