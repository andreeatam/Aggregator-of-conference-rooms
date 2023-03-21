package interfaces;

import model.Organiser;


public interface OrganiserRepositoryInterface extends ICrudRepositoryInterface<Organiser, String > {


    Organiser findByUsernameAndPassword(String username, String password);
}
