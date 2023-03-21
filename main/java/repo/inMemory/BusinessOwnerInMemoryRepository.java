package repo.inMemory;

import interfaces.BusinessOwnerRepositoryInterface;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class BusinessOwnerInMemoryRepository implements BusinessOwnerRepositoryInterface{

    private static BusinessOwnerInMemoryRepository single_instance = null;
    private ArrayList<BusinessOwner> allBusinessOwner = new ArrayList<>();

    public static BusinessOwnerInMemoryRepository getInstance() {
        if (single_instance == null){
            single_instance = new BusinessOwnerInMemoryRepository();
            populateBusinessOwners();
        }
        return single_instance;
    }

    public ArrayList<BusinessOwner> getAllBusinessOwner() {
        return allBusinessOwner;
    }

    //static-poti apela metoda fara sa creezi un obj
    public static void populateBusinessOwners(){
        BusinessOwner businessOwner1 = new BusinessOwner("Raul", "Pop", "raulstefan002", "1234");
        BusinessOwner businessOwner2 = new BusinessOwner("Andreea", "Tamas", "andreeatam", "4321");

       //adaugam in memorie businessOwnerii
       BusinessOwnerInMemoryRepository.getInstance().add(businessOwner1);
       BusinessOwnerInMemoryRepository.getInstance().add(businessOwner2);

       Hall hall1 =new Hall("Sala 1","Evenimente exclusiviste","Cluj",150);

       DJ dj1 =new DJ("DjAndrei","atmosfera geniala",true,true);

        List<Sweet> sweets = new ArrayList<>();
        Sweet sweet1 = new Sweet("bomboane");
        Sweet sweet2 = new Sweet("prajituri");
        Sweet sweet3 = new Sweet("ciocolata");
        sweets.add(sweet1);
        sweets.add(sweet2);
        sweets.add(sweet3);
       CandyBar candybar1=new CandyBar("AllDelicious","very good",sweets);

       //adaugam anunturile "din memorie" la businessOwnerii "din memorie"
       businessOwner1.getProducts().add(hall1);
       businessOwner2.getProducts().add(dj1);
       businessOwner2.getProducts().add(candybar1);

       //adaugam in memorie anunturi
       ProductsInMemoryRepository.getInstance().add(hall1);
       ProductsInMemoryRepository.getInstance().add(dj1);
       ProductsInMemoryRepository.getInstance().add(candybar1);



//       ArrayList<Ad> adsInOffer = new ArrayList<>();
//       adsInOffer.add(ad1);
//
//       Offer offer = new Offer("12.12.2022", "24.12.2022", "das", adsInOffer);
//
//       businessOwner1.getReceivedOffers().add(offer);

    }

    @Override
    public void add(BusinessOwner newBusinessOwner){
        for(BusinessOwner businessOwner: this.allBusinessOwner){
            if(businessOwner.getUsername().equals(newBusinessOwner.getUsername())){
                return;
            }
        }
        this.allBusinessOwner.add(newBusinessOwner);
    }

    @Override
    public void remove(String username){
        this.allBusinessOwner.removeIf(businessOwner -> businessOwner.getUsername().equals(username));
    }

    @Override
    public void update(String username, BusinessOwner newBusinessOwner){
        for(BusinessOwner businessOwner: this.allBusinessOwner){
            if(businessOwner.getUsername().equals(username)){
              businessOwner.setFirstName(newBusinessOwner.getFirstName());
              businessOwner.setLastName(newBusinessOwner.getLastName());
              businessOwner.setUsername(newBusinessOwner.getUsername());
              businessOwner.setPassword(newBusinessOwner.getPassword());
              break;
            }
        }
    }

    @Override
    public BusinessOwner findById(String username) {
        for(BusinessOwner businessOwner: this.allBusinessOwner){
            if(businessOwner.getUsername().equals(username))
                return businessOwner;
        }
        return null;
    }


    @Override
    public BusinessOwner findByUsernameAndPassword(String username, String password) {
        BusinessOwner businessOwner = findById(username);

        if(businessOwner != null) {
            if(businessOwner.getPassword().equals(password)) {
                return businessOwner;
            }
        }
        return null;
    }

    //ret businessOwner-ul coresp id-ului unui Ad
    public BusinessOwner findBusinessOwnerByProductId(Integer idProduct) {
        for(BusinessOwner businessOwner : this.allBusinessOwner) {
            for(Product product: businessOwner.getProducts()) {
                if(product.getId().equals(idProduct)) {
                    return businessOwner;
                }
            }
        }
        return null;
    }

    public Integer getSize() {

        return this.allBusinessOwner.size();
    }


}

