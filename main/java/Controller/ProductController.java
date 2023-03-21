package Controller;

import model.CandyBar;
import model.DJ;
import model.Hall;
import model.Product;
import repo.jpa.ProductRepositoryJPA;

import java.util.List;

public class ProductController {

    ProductRepositoryJPA productRepositoryJPA;

    ProductController(ProductRepositoryJPA productRepositoryJPA) {
        this.productRepositoryJPA = productRepositoryJPA;
    }

    public Product getProduct(Integer idProduct) {
        return productRepositoryJPA.findById(idProduct);
    }

    public List<Product> getProducts() {
        return productRepositoryJPA.getProducts();
    }

    public void deleteProduct(Integer idProduct){
        productRepositoryJPA.remove(idProduct);

    }

    public void modifyHall(Hall oldHall, Hall newHall) {
        productRepositoryJPA.updateHall(oldHall, newHall);
    }

    public void modifyDj(DJ oldDj, DJ newDj) {
        productRepositoryJPA.updateDj(oldDj, newDj);
    }

    public void modifyCandyBar(CandyBar oldCandyBar, CandyBar newCandyBar) {
        productRepositoryJPA.updateCandybar(oldCandyBar, newCandyBar);
    }


}
