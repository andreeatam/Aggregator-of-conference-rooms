import Controller.BusinessOwnerController;
import model.*;
import org.junit.Before;
import org.junit.Test;
import repo.jpa.BusinessOwnerRepositoryJPA;
import repo.jpa.MessageRepositoryJPA;
import repo.jpa.OfferRepositoryJPA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class BusinessOwnerControllerTest {

    private MessageRepositoryJPA messageRepositoryJPAMock;
    private OfferRepositoryJPA offerRepositoryJPAMock;
    private BusinessOwnerRepositoryJPA businessOwnerRepositoryJPAMock;
    private BusinessOwnerController businessOwnerController;

    @Before
    public void setUp() {
        businessOwnerRepositoryJPAMock = mock(BusinessOwnerRepositoryJPA.class);
        messageRepositoryJPAMock = mock(MessageRepositoryJPA.class);
        offerRepositoryJPAMock = mock(OfferRepositoryJPA.class);
        businessOwnerController = new BusinessOwnerController(businessOwnerRepositoryJPAMock, messageRepositoryJPAMock, offerRepositoryJPAMock);

        BusinessOwner businessOwner = new BusinessOwner("Raul", "Pop", "businessOwner", "1234");
        Hall hallMock = mock(Hall.class);
        DJ djMock = mock(DJ.class);
        when(hallMock.getId()).thenReturn(1);
        when(djMock.getId()).thenReturn(2);
        businessOwner.getProducts().add(hallMock);
        businessOwner.getProducts().add(djMock);

        when(businessOwnerRepositoryJPAMock.findById("businessOwner")).thenReturn(businessOwner);
        businessOwnerController.setUsername("businessOwner");

    }


    @Test
    public void createUserSuccessfully() {
        System.out.println("Create an businessOwner");
        List<String> credentials = new ArrayList<>(Arrays.asList("1", "Raul", "Pop", "newUser", "1234"));
        assertEquals(BusinessOwner.class, businessOwnerController.createUser(credentials).getClass());
    }

    @Test
    public void getBusinessOwnerTrue() {
        System.out.println("Get a businessOwner");

        BusinessOwner businessOwner = new BusinessOwner("Raul", "Pop", "businessOwner", "1234");
        when(businessOwnerRepositoryJPAMock.findById("businessOwner")).thenReturn(businessOwner);

        assertEquals(businessOwner, businessOwnerController.getBusinessOwner());
    }

    @Test
    public void getBusinessOwnerFalse() {
        System.out.println("Get an businessOwner that doesnt exist");
        businessOwnerController.setUsername("null");
        assertNull(businessOwnerController.getBusinessOwner());
    }


    @Test
    public void checkSentOffersWithEmptyList() {
        System.out.println("check if a businessOwner has sent any Offers when he haven't ");
        assertTrue(businessOwnerController.checkSentOffers());
    }


    @Test
    public void hasSentOffers() {
        System.out.println("check if a businessOwner has Received any Offers");
        Offer offerMock1 = mock(Offer.class);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock1);
        assertFalse(businessOwnerController.checkSentOffers());
    }


    @Test
    public void hasReceivedMessages() {
        System.out.println("Check Received Messages");
        Message messageMock = mock(Message.class);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock);
        assertFalse(businessOwnerController.checkReceivedMessages());
    }

    @Test
    public void checkReceivedMessagesWithEmptyList() {
        System.out.println("check Receiver Messages With an Empty List");
        assertTrue(businessOwnerController.checkReceivedMessages());
    }

    @Test
    public void newReceivedMessages() {
        System.out.println("check if a businessOwner has Received any new Message");
        Message messageMock1 = mock(Message.class);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        when(messageMock1.getStatus()).thenReturn(Status.SENT);


        assertFalse(businessOwnerController.checkNewMessages());
    }

    @Test
    public void newReceivedMessagesWithEmptyList() {
        System.out.println("check if a businessOwner has Received any new Message with an empty list");
        assertTrue(businessOwnerController.checkSentOffers());
    }

    @Test
    public void noNewReceivedMessages() {
        System.out.println("check if a businessOwner has Received any new Message with a list that hasn't new messages");
        Message messageMock1 = mock(Message.class);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        when(messageMock1.getStatus()).thenReturn(Status.DECLINED);


        assertTrue(businessOwnerController.checkSentOffers());
    }

    @Test
    public void checkOfferSentSuccessfully() {
        System.out.println("Make an offer successfully");

        final Offer OFFER_MOCK = mock(Offer.class);
        final Message MESSAGE_MOCK = mock(Message.class);

        businessOwnerController.makeOffer(OFFER_MOCK, MESSAGE_MOCK);
        verify(offerRepositoryJPAMock).add(OFFER_MOCK);
        verify(offerRepositoryJPAMock).updateStatus(OFFER_MOCK, Status.SENT);
        verify(messageRepositoryJPAMock).updateStatus(MESSAGE_MOCK, Status.ACCEPTED);
    }

    @Test
    public void filterByDeclinedMessagesWithEmptyList() {
        System.out.println("Filter by declined Messages in an empty list");
        List<Message> messages = new ArrayList<>();
        assertEquals(messages, businessOwnerController.filterByDeclinedMessages());
    }

    @Test
    public void filterByDeclinedMessagesWithNoDeclinedMessages() {
        System.out.println("Filter by declined Messages in a list that doesn't have declined messages");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        when(messageMock1.getStatus()).thenReturn(Status.ACCEPTED);
        when(messageMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(messageMock3.getStatus()).thenReturn(Status.SENT);
        List<Message> messagesDeclined = new ArrayList<>();
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesDeclined, businessOwnerController.filterByDeclinedMessages());
    }

    @Test
    public void filterByDeclinedMessagesWithDeclinedMessages() {
        System.out.println("Filter by declined Messages in a list that has declined messages");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        when(messageMock1.getStatus()).thenReturn(Status.DECLINED);
        when(messageMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(messageMock3.getStatus()).thenReturn(Status.SENT);
        List<Message> messagesDeclined = new ArrayList<>();
        messagesDeclined.add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesDeclined, businessOwnerController.filterByDeclinedMessages());
    }

    @Test
    public void filterByAcceptedMessagesWithEmptyList() {
        System.out.println("Filter by accepted Messages in an empty list");
        List<Message> messages = new ArrayList<>();
        assertEquals(messages, businessOwnerController.filterByAcceptedMessages());
    }

    @Test
    public void filterByAcceptedMessagesWithNoAcceptedMessages() {
        System.out.println("Filter by accepted Messages in a list that doesn't have accepted messages");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        when(messageMock1.getStatus()).thenReturn(Status.DECLINED);
        when(messageMock2.getStatus()).thenReturn(Status.DECLINED);
        when(messageMock3.getStatus()).thenReturn(Status.SENT);
        List<Message> messagesAccepted = new ArrayList<>();
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesAccepted, businessOwnerController.filterByAcceptedMessages());
    }

    @Test
    public void filterByAcceptedMessagesWithAcceptedMessages() {
        System.out.println("Filter by accepted Messages in a list that has accepted messages");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        when(messageMock1.getStatus()).thenReturn(Status.DECLINED);
        when(messageMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(messageMock3.getStatus()).thenReturn(Status.SENT);
        List<Message> messagesAccepted = new ArrayList<>();
        messagesAccepted.add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesAccepted, businessOwnerController.filterByAcceptedMessages());
    }

    @Test
    public void filterByDeclinedOffersWithEmptyList() {
        System.out.println("Filter by declined Offers in an empty list");
        List<Offer> offers = new ArrayList<>();
        assertEquals(offers, businessOwnerController.filterByDeclinedOffers());
    }

    @Test
    public void filterByDeclinedOffersWithNoDeclinedOffers() {
        System.out.println("Filter by declined Offers in a list that doesn't have declined Offers");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getStatus()).thenReturn(Status.ACCEPTED);
        when(offerMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(offerMock3.getStatus()).thenReturn(Status.SENT);
        List<Offer> offersDeclined = new ArrayList<>();
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock1);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock2);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock3);
        assertEquals(offersDeclined, businessOwnerController.filterByDeclinedOffers());
    }

    @Test
    public void filterByDeclinedOffersWithDeclinedOffers() {
        System.out.println("Filter by declined Offers in a list that has declined Offers");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getStatus()).thenReturn(Status.DECLINED);
        when(offerMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(offerMock3.getStatus()).thenReturn(Status.SENT);
        List<Offer> offersDeclined = new ArrayList<>();
        offersDeclined.add(offerMock1);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock1);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock2);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock3);
        assertEquals(offersDeclined, businessOwnerController.filterByDeclinedOffers());
    }

    @Test
    public void filterByAcceptedOffersWithEmptyList() {
        System.out.println("Filter by accepted Offers in an empty list");
        List<Offer> offers = new ArrayList<>();
        assertEquals(offers, businessOwnerController.filterByAcceptedOffers());
    }

    @Test
    public void filterByAcceptedOffersWithNoAcceptedOffers() {
        System.out.println("Filter by accepted Offers in a list that doesn't have accepted Offers");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getStatus()).thenReturn(Status.DECLINED);
        when(offerMock2.getStatus()).thenReturn(Status.DECLINED);
        when(offerMock3.getStatus()).thenReturn(Status.SENT);
        List<Offer> offersAccepted = new ArrayList<>();
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock1);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock2);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock3);
        assertEquals(offersAccepted, businessOwnerController.filterByAcceptedOffers());
    }

    @Test
    public void filterByAcceptedOffersWithAcceptedOffers() {
        System.out.println("Filter by accepted Offers in a list that has accepted Offers");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getStatus()).thenReturn(Status.DECLINED);
        when(offerMock2.getStatus()).thenReturn(Status.ACCEPTED);
        when(offerMock3.getStatus()).thenReturn(Status.SENT);
        List<Offer> offersAccepted = new ArrayList<>();
        offersAccepted.add(offerMock2);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock1);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock2);
        businessOwnerController.getBusinessOwner().getSentOffers().add(offerMock3);
        assertEquals(offersAccepted, businessOwnerController.filterByAcceptedOffers());
    }

    @Test
    public void filterMessagesBySenderUsernameWithWrongUsername() {
        System.out.println("Filter Messages by sender username with a wrong input");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        Organiser organiserMock1 = mock(Organiser.class);
        Organiser organiserMock2 = mock(Organiser.class);

        when(messageMock1.getSender()).thenReturn(organiserMock1);
        when(messageMock2.getSender()).thenReturn(organiserMock1);
        when(messageMock3.getSender()).thenReturn(organiserMock2);
        when(organiserMock1.getUsername()).thenReturn("sender1");
        when(organiserMock2.getUsername()).thenReturn("sender2");
        List<Message> messagesBySender0 = new ArrayList<>();
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesBySender0, businessOwnerController.filterMessagesBySenderUsername("sender0"));
    }

    @Test
    public void filterMessagesBySenderUsername() {
        System.out.println("Filter Messages by sender username");
        Message messageMock1 = mock(Message.class);
        Message messageMock2 = mock(Message.class);
        Message messageMock3 = mock(Message.class);
        Organiser organiserMock1 = mock(Organiser.class);
        Organiser organiserMock2 = mock(Organiser.class);

        when(messageMock1.getSender()).thenReturn(organiserMock1);
        when(messageMock2.getSender()).thenReturn(organiserMock1);
        when(messageMock3.getSender()).thenReturn(organiserMock2);
        when(organiserMock1.getUsername()).thenReturn("sender1");
        when(organiserMock2.getUsername()).thenReturn("sender2");
        List<Message> messagesBySender1 = new ArrayList<>(Arrays.asList(messageMock1, messageMock2));
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock1);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock2);
        businessOwnerController.getBusinessOwner().getReceivedMessages().add(messageMock3);
        assertEquals(messagesBySender1, businessOwnerController.filterMessagesBySenderUsername("sender1"));
    }

    @Test
    public void isBusinessOwnerProductTrue() {
        System.out.println("Check if a product id belongs to a business owner's product: True");
        assertTrue(businessOwnerController.isBusinessOwnerProduct(1));
    }
    @Test
    public void isBusinessOwnerProductFalse() {
        System.out.println("Check if a product id belongs to a business owner's product: False");
        assertFalse(businessOwnerController.isBusinessOwnerProduct(4));
    }

}