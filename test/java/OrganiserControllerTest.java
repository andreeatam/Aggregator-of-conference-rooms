import Controller.OrganiserController;
import model.*;
import org.junit.Before;
import org.junit.Test;
import repo.jpa.MessageRepositoryJPA;
import repo.jpa.OfferRepositoryJPA;
import repo.jpa.OrganiserRepositoryJPA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OrganiserControllerTest {

    private OrganiserRepositoryJPA organiserRepositoryJPAMock;
    private MessageRepositoryJPA messageRepositoryJPAMock;
    private OrganiserController organiserController;

    @Before
    public void setUp() {
        organiserRepositoryJPAMock = mock(OrganiserRepositoryJPA.class);
        messageRepositoryJPAMock = mock(MessageRepositoryJPA.class);
        OfferRepositoryJPA offerRepositoryJPAMock = mock(OfferRepositoryJPA.class);
        organiserController = new OrganiserController(organiserRepositoryJPAMock, messageRepositoryJPAMock, offerRepositoryJPAMock);

        Organiser organiser = new Organiser("Raul", "Pop", "organiser", "1234");

        when(organiserRepositoryJPAMock.findById("organiser")).thenReturn(organiser);
        organiserController.setUsername("organiser");

    }


    @Test
    public void createUserSuccessfully() {
        System.out.println("Create an organiser");
        List<String> credentials = new ArrayList<>(Arrays.asList("1", "Raul", "Pop", "newUser", "1234"));
        assertEquals(Organiser.class, organiserController.createUser(credentials).getClass());
    }

    @Test
    public void getOrganiserTrue() {
        System.out.println("Get an organiser");

        Organiser organiser = new Organiser("Raul", "Pop", "organiser", "1234");
        when(organiserRepositoryJPAMock.findById("organiser")).thenReturn(organiser);

        assertEquals(organiser, organiserController.getOrganiser());
    }

    @Test
    public void getOrganiserFalse() {
        System.out.println("Get an organiser that doesnt exist");
        organiserController.setUsername("null");
        assertNull(organiserController.getOrganiser());
    }

    @Test
    public void noNewReceivedOffers() {
        System.out.println("check if a organiser has Received any new Offers with an empty list");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);

        when(offerMock1.getStatus()).thenReturn(Status.DECLINED);
        when(offerMock2.getStatus()).thenReturn(Status.DECLINED);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        assertTrue(organiserController.checkNewReceivedOffers());
    }

    @Test
    public void newReceivedOffers() {
        System.out.println("check if a organiser has Received any new Offers");
        Offer offerMock1 = mock(Offer.class);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        when(offerMock1.getStatus()).thenReturn(Status.SENT);


        assertFalse(organiserController.checkNewReceivedOffers());
    }

    @Test
    public void checkNewReceivedOffersWithEmptyList() {
        System.out.println("check if a organiser has Received any Offers with an empty list");
        assertTrue(organiserController.checkNewReceivedOffers());
    }


    @Test
    public void hasReceivedOffers() {
        System.out.println("check if a organiser has Received any Offers");
        Offer offerMock1 = mock(Offer.class);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        assertFalse(organiserController.checkReceivedOffers());
    }


    @Test
    public void checkReceivedOffersWithEmptyList() {
        System.out.println("check Received Offers With an Empty List");
        assertTrue(organiserController.checkReceivedOffers());
    }

    @Test
    public void hasSentMessages() {
        System.out.println("Check Sent Messages");
        Message messageMock = mock(Message.class);
        organiserController.getOrganiser().getSentMessages().add(messageMock);
        assertFalse(organiserController.checkSentMessages());
    }

    @Test
    public void checkSentMessagesWithEmptyList() {
        System.out.println("check Sent Messages With an Empty List");
        assertTrue(organiserController.checkSentMessages());
    }

    @Test
    public void sendMessageSuccessfully() {
        System.out.println("Check if a message is sent correctly");

        final Message MESSAGE_MOCK = mock(Message.class);

        organiserController.sendMessage(MESSAGE_MOCK);

        verify(messageRepositoryJPAMock).add(MESSAGE_MOCK);
        verify(messageRepositoryJPAMock).updateStatus(MESSAGE_MOCK, Status.SENT);

    }

    @Test
    public void filterByDeclinedMessagesWithEmptyList() {
        System.out.println("Filter by declined Messages in an empty list");
        List<Message> messages = new ArrayList<>();
        assertEquals(messages, organiserController.filterByDeclinedMessages());
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
        organiserController.getOrganiser().getSentMessages().add(messageMock1);
        organiserController.getOrganiser().getSentMessages().add(messageMock2);
        organiserController.getOrganiser().getSentMessages().add(messageMock3);
        assertEquals(messagesDeclined, organiserController.filterByDeclinedMessages());
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
        organiserController.getOrganiser().getSentMessages().add(messageMock1);
        organiserController.getOrganiser().getSentMessages().add(messageMock2);
        organiserController.getOrganiser().getSentMessages().add(messageMock3);
        assertEquals(messagesDeclined, organiserController.filterByDeclinedMessages());
    }

    @Test
    public void filterByAcceptedMessagesWithEmptyList() {
        System.out.println("Filter by accepted Messages in an empty list");
        List<Message> messages = new ArrayList<>();
        assertEquals(messages, organiserController.filterByAcceptedMessages());
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
        organiserController.getOrganiser().getSentMessages().add(messageMock1);
        organiserController.getOrganiser().getSentMessages().add(messageMock2);
        organiserController.getOrganiser().getSentMessages().add(messageMock3);
        assertEquals(messagesAccepted, organiserController.filterByAcceptedMessages());
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
        organiserController.getOrganiser().getSentMessages().add(messageMock1);
        organiserController.getOrganiser().getSentMessages().add(messageMock2);
        organiserController.getOrganiser().getSentMessages().add(messageMock3);
        assertEquals(messagesAccepted, organiserController.filterByAcceptedMessages());
    }

    @Test
    public void filterByDeclinedOffersWithEmptyList() {
        System.out.println("Filter by declined Offers in an empty list");
        List<Offer> offers = new ArrayList<>();
        assertEquals(offers, organiserController.filterByDeclinedOffers());
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
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersDeclined, organiserController.filterByDeclinedOffers());
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
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersDeclined, organiserController.filterByDeclinedOffers());
    }

    @Test
    public void filterByAcceptedOffersWithEmptyList() {
        System.out.println("Filter by accepted Offers in an empty list");
        List<Offer> offers = new ArrayList<>();
        assertEquals(offers, organiserController.filterByAcceptedOffers());
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
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersAccepted, organiserController.filterByAcceptedOffers());
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
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersAccepted, organiserController.filterByAcceptedOffers());
    }

    @Test
    public void filterOffersBySenderUsernameWithWrongUsername() {
        System.out.println("Filter Offers by sender username with a wrong input");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        BusinessOwner businessOwnerMock1 = mock(BusinessOwner.class);
        BusinessOwner businessOwnerMock2 = mock(BusinessOwner.class);

        when(offerMock1.getSender()).thenReturn(businessOwnerMock1);
        when(offerMock2.getSender()).thenReturn(businessOwnerMock1);
        when(offerMock3.getSender()).thenReturn(businessOwnerMock2);
        when(businessOwnerMock1.getUsername()).thenReturn("sender1");
        when(businessOwnerMock2.getUsername()).thenReturn("sender2");
        List<Offer> offersBySender0 = new ArrayList<>();
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersBySender0, organiserController.filterOffersBySenderUsername("sender0"));
    }

    @Test
    public void filterOffersBySenderUsername() {
        System.out.println("Filter Offers by sender username");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        BusinessOwner businessOwnerMock1 = mock(BusinessOwner.class);
        BusinessOwner businessOwnerMock2 = mock(BusinessOwner.class);

        when(offerMock1.getSender()).thenReturn(businessOwnerMock1);
        when(offerMock2.getSender()).thenReturn(businessOwnerMock1);
        when(offerMock3.getSender()).thenReturn(businessOwnerMock2);
        when(businessOwnerMock1.getUsername()).thenReturn("sender1");
        when(businessOwnerMock2.getUsername()).thenReturn("sender2");
        List<Offer> offersBySender1 = new ArrayList<>(Arrays.asList(offerMock1, offerMock2));
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        assertEquals(offersBySender1, organiserController.filterOffersBySenderUsername("sender1"));
    }

    @Test
    public void sortOffersByPriceAscending() {
        System.out.println("Sort Offers By Price ascending");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getPrice()).thenReturn(100);
        when(offerMock2.getPrice()).thenReturn(200);
        when(offerMock3.getPrice()).thenReturn(300);
        List<Offer> offers = new ArrayList<>(Arrays.asList(offerMock1, offerMock2, offerMock3));
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);


        Iterator<Offer> outputIterator = offers.iterator();
        Iterator<Offer> inputIterator = organiserController.sortOffersByPriceAscending().iterator();
        while(outputIterator.hasNext() && inputIterator.hasNext()) {
            assertEquals(outputIterator.next(), inputIterator.next());
        }
    }

    @Test
    public void sortOffersByPriceDescending() {
        System.out.println("Sort Offers By Price descending");
        Offer offerMock1 = mock(Offer.class);
        Offer offerMock2 = mock(Offer.class);
        Offer offerMock3 = mock(Offer.class);
        when(offerMock1.getPrice()).thenReturn(100);
        when(offerMock2.getPrice()).thenReturn(200);
        when(offerMock3.getPrice()).thenReturn(300);
        List<Offer> offers = new ArrayList<>(Arrays.asList(offerMock3, offerMock2, offerMock1));
        organiserController.getOrganiser().getReceivedOffers().add(offerMock1);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock2);
        organiserController.getOrganiser().getReceivedOffers().add(offerMock3);


        Iterator<Offer> outputIterator = offers.iterator();
        Iterator<Offer> inputIterator = organiserController.sortOffersByPriceDescending().iterator();
        while(outputIterator.hasNext() && inputIterator.hasNext()) {
            assertEquals(outputIterator.next(), inputIterator.next());
        }
    }








}
