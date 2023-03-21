package Controller;

import interfaces.UserControllerInterface;
import model.*;
import repo.jpa.BusinessOwnerRepositoryJPA;
import repo.jpa.MessageRepositoryJPA;
import repo.jpa.OfferRepositoryJPA;

import java.util.List;


public class BusinessOwnerController implements UserControllerInterface<BusinessOwner, List<String>> {
    private String username;

    private BusinessOwnerRepositoryJPA businessOwnerRepositoryJPA;
    private MessageRepositoryJPA messageRepositoryJPA;
    private OfferRepositoryJPA offerRepositoryJPA;



    public BusinessOwnerController(BusinessOwnerRepositoryJPA businessOwnerRepositoryJPA, MessageRepositoryJPA messageRepositoryJPA, OfferRepositoryJPA offerRepositoryJPA) {
        this.businessOwnerRepositoryJPA = businessOwnerRepositoryJPA;
        this.messageRepositoryJPA = messageRepositoryJPA;
        this.offerRepositoryJPA = offerRepositoryJPA;
    }

    public BusinessOwnerController() {
    }

    public BusinessOwner getBusinessOwner() {
        return businessOwnerRepositoryJPA.findById(username);
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public List<Product> getBusinessOwnerProducts() {
        List<Product> products = businessOwnerRepositoryJPA.findById(this.username).getProducts();
        return products;
    }

    @Override
    public BusinessOwner createUser(List<String> credentials) {
        return new BusinessOwner(credentials.get(1), credentials.get(2), credentials.get(3), credentials.get(4));

    }

    public boolean checkNewMessages() {
        List<Message> newMessages = getBusinessOwner().getReceivedMessages().stream().filter(message -> message.getStatus().equals(Status.SENT)).toList(); ;
        return newMessages.isEmpty();
    }

    public boolean checkReceivedMessages() {
        return getBusinessOwner().getReceivedMessages().isEmpty();
    }
    public boolean checkSentOffers() {
        return getBusinessOwner().getSentOffers().isEmpty();
    }

    public void createProduct(Product newProduct) {
        businessOwnerRepositoryJPA.updateProductsList(getBusinessOwner(), newProduct);
    }

    public void declineMessage(Message message) {

        messageRepositoryJPA.updateStatus(message, Status.DECLINED);
    }


    public void makeOffer(Offer offer, Message message) {
        offerRepositoryJPA.add(offer);
        offerRepositoryJPA.updateStatus(offer, Status.SENT);
        messageRepositoryJPA.updateStatus(message, Status.ACCEPTED);
    }

    public void removeProduct(Integer idProduct) {
        for(Product product : getBusinessOwner().getProducts()) {
            if(product.getId() == idProduct) {
                businessOwnerRepositoryJPA.removeProduct(username, product);
                break;
            }
        }
    }

    @Override
    public List<Message> filterByDeclinedMessages(){

        List<Message> messages = businessOwnerRepositoryJPA.findById(this.username).getReceivedMessages();

        return messages.stream().filter(message -> message.getStatus().equals(Status.DECLINED)).toList();
    }

    @Override
    public List<Message> filterByAcceptedMessages(){

        List<Message> messages = businessOwnerRepositoryJPA.findById(this.username).getReceivedMessages();

        return messages.stream().filter(message -> message.getStatus().equals(Status.ACCEPTED)).toList();
    }
    @Override
    public List<Offer> filterByDeclinedOffers(){

        List<Offer> offers = businessOwnerRepositoryJPA.findById(this.username).getSentOffers();

        return offers.stream().filter(offer -> offer.getStatus().equals(Status.DECLINED)).toList();
    }
    @Override
    public List<Offer> filterByAcceptedOffers(){

        List<Offer> offers = businessOwnerRepositoryJPA.findById(this.username).getSentOffers();

        return offers.stream().filter(offer -> offer.getStatus().equals(Status.ACCEPTED)).toList();
    }

    public List<Message> filterMessagesBySenderUsername(String senderUsername) {
        List<Message> messages = businessOwnerRepositoryJPA.findById(this.username).getReceivedMessages();

        return messages.stream().filter(message -> message.getSender().getUsername().equals(senderUsername)).toList();
    }

    public boolean isBusinessOwnerProduct(Integer idProduct) {
        for(Product product : getBusinessOwner().getProducts()) {
            if(product.getId() == idProduct) {
                return true;
            }
        }
        return false;
    }
}

