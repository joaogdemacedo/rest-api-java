package mvpMatch.Backend1.Controller;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.UseCases.UseCaseAuthentication;
import mvpMatch.Backend1.UseCases.UseCaseCRUDProducts;
import mvpMatch.Backend1.UseCases.UseCaseCRUDUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    UseCaseCRUDProducts useCaseCRUDProducts;

    @Autowired
    UseCaseAuthentication useCaseAuthentication;
    @Autowired
    UseCaseCRUDUser useCaseCRUDUser;

    @ResponseBody
    @GetMapping(consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<List<Product>> findAllUsers(HttpServletRequest request) {
        return useCaseCRUDProducts.getProducts();
    }

    @GetMapping(value ="/{id}",consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<Product> getProduct(HttpServletRequest request, @PathVariable int id) {
        return useCaseCRUDProducts.getProduct(id);
    }

    @GetMapping(value = "/owner/{owner}", consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<List<Product>> findProductsByOwner(HttpServletRequest request, @PathVariable String owner) {

        return useCaseCRUDProducts.findProductsByOwner(owner);
    }

    @GetMapping(value = "/cost/{cost}", consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<List<Product>> findProductsByMaxCost(HttpServletRequest request, @PathVariable int cost) {
        return useCaseCRUDProducts.findProductByMaxCost(cost);
    }

    @ResponseBody
    @PutMapping(consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<Product> putProduct(HttpServletRequest request,@RequestBody Product product) {

        String role = useCaseCRUDUser.getUserRole(useCaseAuthentication.getLoggedUsername());
        return useCaseCRUDProducts.addProduct(product,role);
    }

    @DeleteMapping(value ="/{id}",consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<Void> deleteProduct(HttpServletRequest request, @PathVariable int id) {
        return useCaseCRUDProducts.deleteProduct(id);
    }
}
