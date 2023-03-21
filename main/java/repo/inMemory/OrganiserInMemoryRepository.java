package repo.inMemory;

import interfaces.OrganiserRepositoryInterface;
import model.Message;
import model.Organiser;

import java.util.ArrayList;


public class OrganiserInMemoryRepository implements OrganiserRepositoryInterface {

    private final ArrayList<Organiser> allOrganisers = new ArrayList<>();

    private static OrganiserInMemoryRepository single_instance = null;

    public static OrganiserInMemoryRepository getInstance() {

        if(single_instance == null) {
            single_instance = new OrganiserInMemoryRepository();
            populateOrganisers();
        }
        return single_instance;

    }

    public ArrayList<Organiser> getAllOrganisers() {
        return allOrganisers;
    }

    public static void populateOrganisers(){
        Organiser organiser1 = new Organiser("Paul", "Bop", "paulstefan002", "9082");
        Organiser organiser2 = new Organiser("Andrei", "Malinas", "andreim", "1321");

       OrganiserInMemoryRepository.getInstance().add(organiser1);
       OrganiserInMemoryRepository.getInstance().add(organiser2);

    }

    @Override
    public void add(Organiser newOrganiser) {
        for( Organiser organiser: this.allOrganisers){
            if(organiser.getUsername().equals(newOrganiser.getUsername())) {
                return;
            }
        }
        this.allOrganisers.add(newOrganiser);
    }

    @Override
    public void remove(String username) {
        this.allOrganisers.removeIf(organiser -> organiser.getUsername().equals(username));
    }

    @Override
    public void update(String username, Organiser newOrganiser) {

        for( Organiser organiser : this.allOrganisers) {
            if(organiser.getUsername().equals(username)) {
                organiser.setFirstName(newOrganiser.getFirstName());
                organiser.setLastName(newOrganiser.getLastName());
                organiser.setPassword(newOrganiser.getPassword());
                organiser.setUsername(newOrganiser.getUsername());
                break;
            }
        }
    }

    //metoda folosita la login si la signUp ptr a vedea ca nu mai exista user ul
    @Override
    public Organiser findById(String username) {
        for( Organiser organiser: this.allOrganisers) {
            if (organiser.getUsername().equals(username)) {
                return organiser;
            }
        }
        return null;
    }

    @Override
    public Organiser findByUsernameAndPassword(String username, String password) {
        Organiser organiser = findById(username);

        if (organiser != null ) {
            if (organiser.getPassword().equals(password)) {
                return organiser;
            }
        }
        return null;
    }

    //ret un organiser dupa id-ul unei oferte
    //metoda ce ne ajuta sa aflam din mesaj cine e org
    public Organiser findOrganiserByMessageId(Integer idMessage) {
        for(Organiser organiser : this.allOrganisers) {
            for(Message message: organiser.getSentMessages()) {
                if(message.getIdMessage().equals(idMessage)) {
                    return organiser;
                }
            }
        }
        return null;
    }

    public void printOrganisers(){
        for(Organiser o : this.allOrganisers) {
            System.out.println("First "+o.getFirstName());
            System.out.println("Lastname "+o.getLastName());
            System.out.println("Username "+o.getUsername());
        }
    }
    public Integer getSize() {

        return this.allOrganisers.size();
    }
}
