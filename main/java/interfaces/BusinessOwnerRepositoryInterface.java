package interfaces;

import model.BusinessOwner;

public interface BusinessOwnerRepositoryInterface extends ICrudRepositoryInterface<BusinessOwner, String> {

    BusinessOwner findByUsernameAndPassword(String username, String password);

}


