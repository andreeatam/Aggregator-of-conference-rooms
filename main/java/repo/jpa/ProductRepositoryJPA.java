package repo.jpa;

import interfaces.ProductRepositoryInterface;
import model.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ProductRepositoryJPA {
    private final EntityManager manager;

    public ProductRepositoryJPA(EntityManager manager) {
        this.manager = manager;
    }

    public void remove(Integer id) {
        Product product = findById(id);
        if( product != null) {
            manager.getTransaction().begin();
            product.setStatusProduct(StatusProduct.INACTIVE);
            //manager.remove(product);
            manager.flush();
            manager.getTransaction().commit();
            //product.setStatusProduct(StatusProduct.INACTIVE);
        }

    }

    public void updateHall(Hall hall, Hall newHall) {
        manager.getTransaction().begin();
        hall.setName(newHall.getName());
        hall.setCapacity(newHall.getCapacity());
        hall.setLocation(newHall.getLocation());
        hall.setDescription(newHall.getDescription());
        manager.getTransaction().commit();
    }

    public void updateCandybar(CandyBar candyBar, CandyBar newCandyBar) {
        manager.getTransaction().begin();
        candyBar.setName(newCandyBar.getName());
        candyBar.setDescription(newCandyBar.getDescription());
        manager.getTransaction().commit();
    }

    public void updateDj(DJ dj, DJ newDj) {
        manager.getTransaction().begin();
        dj.setName(newDj.getName());
        dj.setDescription(newDj.getDescription());
        dj.setLights(newDj.getLights());
        dj.setStereo(newDj.getStereo());
        manager.getTransaction().commit();
    }

    public Product findById(Integer id) {
        System.out.println("ID: " + id);
        Product product= manager.find(Product.class, id);
        if(product != null) {
            manager.refresh(product);
        }
        return product;
    }

    public List<Product> getProducts() {
        TypedQuery<Product> query = manager.createQuery("SELECT p FROM Product p WHERE p.statusProduct = :status", Product.class);
        query.setParameter("status", StatusProduct.ACTIVE);
        List<Product> products = query.getResultList();
        return products;
    }


}
