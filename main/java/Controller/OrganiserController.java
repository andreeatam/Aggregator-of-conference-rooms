package Controller;

import interfaces.UserControllerInterface;
import model.*;
import repo.jpa.*;

import java.util.Comparator;
import java.util.List;

public class OrganiserController implements UserControllerInterface<Organiser, List<String>> {

    private String username;

    private OrganiserRepositoryJPA organiserRepositoryJPA;

    private MessageRepositoryJPA messageRepositoryJPA;
    private OfferRepositoryJPA offerRepositoryJPA;



    public OrganiserController(OrganiserRepositoryJPA organiserRepositoryJPA, MessageRepositoryJPA messageRepositoryJPA, OfferRepositoryJPA offerRepositoryJPA) {
        this.organiserRepositoryJPA = organiserRepositoryJPA;
        this.messageRepositoryJPA = messageRepositoryJPA;
        this.offerRepositoryJPA = offerRepositoryJPA;
    }

    public OrganiserController() {
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Organiser getOrganiser() {
        return organiserRepositoryJPA.findById(username);
    }


    @Override
    public Organiser createUser(List<String> credentials) {
        return new Organiser(credentials.get(1), credentials.get(2), credentials.get(3), credentials.get(4));
    }


    //se seteaza statusul unei oferte la ACCEPTED
    public void acceptOffer(Offer offer) {
        offerRepositoryJPA.updateStatus(offer, Status.ACCEPTED);
    }

    //se seteaza statusul unei oferte la DECLINED
    public void declineOffer(Offer offer) {
        offerRepositoryJPA.updateStatus(offer, Status.DECLINED);
    }

    public boolean checkNewReceivedOffers() {
        List<Offer> newOffers = getOrganiser().getReceivedOffers().stream().filter(offer -> offer.getStatus().equals(Status.SENT)).toList(); ;
        return newOffers.isEmpty();
    }

    public boolean checkReceivedOffers() {
        return getOrganiser().getReceivedOffers().isEmpty();
    }

    public boolean checkSentMessages() {
        return getOrganiser().getSentMessages().isEmpty();
    }


    public void sendMessage(Message message) {
        messageRepositoryJPA.add(message);
        messageRepositoryJPA.updateStatus(message, Status.SENT); //setam statusul msj la SENT
    }

    @Override
    public List<Message> filterByDeclinedMessages(){

        List<Message> messages = getOrganiser().getSentMessages();

        return messages.stream().filter(message -> message.getStatus().equals(Status.DECLINED)).toList();
    }

    @Override
    public List<Message> filterByAcceptedMessages(){

        List<Message> messages = getOrganiser().getSentMessages();

        return messages.stream().filter(message -> message.getStatus().equals(Status.ACCEPTED)).toList();
    }
    @Override
    public List<Offer> filterByDeclinedOffers(){

        List<Offer> offers = getOrganiser().getReceivedOffers();

        return offers.stream().filter(offer -> offer.getStatus().equals(Status.DECLINED)).toList();
    }
    @Override
    public List<Offer> filterByAcceptedOffers(){

        List<Offer> offers = getOrganiser().getReceivedOffers();

        return offers.stream().filter(offer -> offer.getStatus().equals(Status.ACCEPTED)).toList();
    }

    public List<Offer> sortOffersByPriceAscending() {
        List<Offer> offers = getOrganiser().getReceivedOffers();

        return offers.stream().sorted(Comparator.comparingInt(Offer::getPrice)).toList();
    }

    public List<Offer> sortOffersByPriceDescending() {
        List<Offer> offers = getOrganiser().getReceivedOffers();

        return offers.stream().sorted(Comparator.comparingInt(Offer::getPrice).reversed()).toList();
    }

    public List<Offer> filterOffersBySenderUsername(String usernameSender) {
        List<Offer> offers = getOrganiser().getReceivedOffers();
        return offers.stream().filter(offer -> offer.getSender().getUsername().equals(usernameSender)).toList();
    }


}
