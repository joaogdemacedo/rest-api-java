package mvpMatch.Backend1.UseCases;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.Repository.ProductRepository;
import mvpMatch.Backend1.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UseCaseCRUDProducts {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    UseCaseAuthentication useCaseAuthentication;

    private final String buyerRole ="buyer";
    private final String sellerRole ="seller";

    public ResponseEntity<List<Product>> getProducts(){

        List<Product> products = productRepository.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    public ResponseEntity<Product> getProduct(int id){
        try {
            Product product = productRepository.findById(id).get();

            return new ResponseEntity<>(product, HttpStatus.OK);
        }catch (Exception e){

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<Product> addProduct(Product product,String role){

        String loggedUser = useCaseAuthentication.getLoggedUsername();

        if(role.equals(sellerRole)){

                if(isValidProductCost(product.getCost())) {
                    int id = getIdFromProduct(product.getName());
                    return addProductToRepository(id, product,loggedUser);

                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    private ResponseEntity<Product> addProductToRepository(int id, Product product, String owner){

        if(id == -1){
            Product newProduct = new Product();
            newProduct.setAmount(product.getAmount());
            newProduct.setCost(product.getCost());
            newProduct.setName(product.getName());
            newProduct.setOwner(owner);
            newProduct.setId(getNewId());
            productRepository.saveAndFlush(newProduct);
            return new ResponseEntity<>(newProduct,HttpStatus.CREATED);
        }
        else {
            Product refProduct = productRepository.findById(id).get();
            refProduct.setAmount(refProduct.getAmount()+product.getAmount());
            productRepository.saveAndFlush(refProduct);
            return new ResponseEntity<>(refProduct,HttpStatus.OK);

        }
    }

    public ResponseEntity<Void> deleteProduct(int id){
        try {
            Product product = getProduct(id).getBody();

            if(product!= null && product.getOwner().equals(useCaseAuthentication.getLoggedUsername())){
                productRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private int getIdFromProduct(String name){
        List<Product> products = (List<Product>) productRepository.findAll();

        for(Product p : products){
            if(p.getName().equals(name)) return p.getId();
        }
        return -1;
    }

    private int getNewId(){
        return (int) (productRepository.count() +1);
    }

    public ResponseEntity<List<Product>> findProductsByOwner(String owner){
        List<Product> products = productRepository.findProductsByOwner(owner);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }
    public ResponseEntity<List<Product>> findProductByMaxCost(int cost){
        List<Product> products =productRepository.findProductsByMaxCost(cost);
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    public Boolean isValidProductCost(long cost){
        return cost % 5 ==0;
    }

    public boolean removeProduct(Product product,int amount){
        if(product.getAmount() < amount || product.getAmount() == 0){
            return false;
        }
        product.setAmount(product.getAmount()-amount);
        productRepository.saveAndFlush(product);
        return true;
    }
}
