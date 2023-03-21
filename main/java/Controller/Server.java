package Controller;
import model.BusinessOwner;
import model.Organiser;
import repo.jpa.*;


import javax.persistence.EntityManager;

import java.util.List;

public class Server {

    private OrganiserController organiserController;
    private BusinessOwnerController businessOwnerController;

    private ProductController productController;
    private  OrganiserRepositoryJPA organisersRepositoryJPA;
    private  BusinessOwnerRepositoryJPA businessOwnersRepositoryJPA;


    public Server(EntityManager manager) {
        this.organisersRepositoryJPA = new OrganiserRepositoryJPA(manager);
        this.businessOwnersRepositoryJPA = new BusinessOwnerRepositoryJPA(manager);
        ProductRepositoryJPA productRepositoryJPA = new ProductRepositoryJPA(manager);
        MessageRepositoryJPA messageRepositoryJPA = new MessageRepositoryJPA(manager);
        OfferRepositoryJPA offerRepositoryJPA = new OfferRepositoryJPA(manager);

        this.businessOwnerController = new BusinessOwnerController(this.businessOwnersRepositoryJPA, messageRepositoryJPA, offerRepositoryJPA);
        this.organiserController = new OrganiserController(this.organisersRepositoryJPA, messageRepositoryJPA, offerRepositoryJPA);
        this.productController = new ProductController(productRepositoryJPA);
    }


    public OrganiserController getOrganiserController() {
        return organiserController;
    }

    public BusinessOwnerController getBusinessOwnerController() {
        return businessOwnerController;
    }

    public ProductController getProductController() {
        return productController;
    }

    public Server() {
    }

    public boolean login(List<String> credentials) {

        if (credentials.get(0).equals("1") && organisersRepositoryJPA.findByUsernameAndPassword(credentials.get(1), credentials.get(2)) != null) { //daca e org si s-a gasit in bd (are cont)
            return true; //se dechide meniul pt organiser
        }
        if (credentials.get(0).equals("2") && businessOwnersRepositoryJPA.findByUsernameAndPassword(credentials.get(1), credentials.get(2)) != null) { //daca e b.o. si are deja cont
            return true;
        }
       return false;
    }
    public boolean signUp(List<String> credentials) {
        if(credentials.get(0).equals("1")) { //daca in string ul de credetiale, e organiser
            organisersRepositoryJPA.add(getOrganiserController().createUser(credentials)); //se creeaza un user cu acele credentiale => se ret un org si apoi se adauga la acea instanta unica
            return true;
        }
        if (credentials.get(0).equals("2")) { //daca in string ul de credetiale, e b.o
            businessOwnersRepositoryJPA.add(getBusinessOwnerController().createUser(credentials)); //se creeaza un user cu acele credentiale => se ret un b.o. si apoi se adauga la acea instanta unica
            return true;
        }
        return false;
    }

    public void setBusinessOwnerInController(String username) {
        businessOwnerController.setUsername(username);
    } public void setOrganiserInController(String username) {
        organiserController.setUsername(username);
    }

    public BusinessOwner getBusinessOwnerByProductId(Integer idProduct) {
        return businessOwnersRepositoryJPA.findBusinessOwnerByProductId(idProduct);
    }

    public Organiser getOrganiserByMessageId(Integer idMessage) {
        return organisersRepositoryJPA.findOrganiserByMessageId(idMessage);
    }
    public BusinessOwner getBusinessOwner(String username) {
        return businessOwnersRepositoryJPA.findById(username);
    }

    public Organiser getOrganiser(String username) {
        return organisersRepositoryJPA.findById(username);
    }



}
