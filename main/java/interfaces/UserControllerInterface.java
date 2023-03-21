package interfaces;

import model.Message;
import model.Offer;

import java.util.List;

public interface UserControllerInterface <E, ARR>{
    public E createUser(ARR array);
    public List<Message> filterByDeclinedMessages();
    public List<Message> filterByAcceptedMessages();
    public List<Offer> filterByDeclinedOffers();
    public List<Offer> filterByAcceptedOffers();
}
