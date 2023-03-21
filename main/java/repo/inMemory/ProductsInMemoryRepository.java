package repo.inMemory;

import interfaces.ProductRepositoryInterface;
import model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsInMemoryRepository  implements ProductRepositoryInterface {
    private static ProductsInMemoryRepository single_instance = null;
    private List<Product> allProducts = new ArrayList<>();

    public static ProductsInMemoryRepository getInstance() {
        if(single_instance == null) {
            single_instance = new ProductsInMemoryRepository();
        }
        return single_instance;
    }

    public List<Product> getAllProducts() {
        return allProducts;
    }

    @Override
    public void add(Product entity){
        for(Product product: this.allProducts){
            if(product.getId().equals(entity.getId())){
                return;
            }
        }
        this.allProducts.add(entity);
    }

    //sterge un anunt dupa id din lista de toate anunturile
    @Override
    public void remove(Integer id){

        this.allProducts.removeIf(product -> product.getId().equals(id));
    }

    @Override
    public void update(Integer id, Product newProduct) {
        for( Product product : this.allProducts) {
            if(product.getId().equals(id)) {
                product.setName(newProduct.getName());
                product.setDescription(newProduct.getDescription());
                break;
            }
        }
    }

    @Override
    public Product findById(Integer id){
        for(Product product: this.allProducts) {
            if(product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

}
