package mvpMatch.Backend1.Controller;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.CoinsRepository;
import mvpMatch.Backend1.UseCases.UseCaseCRUDProducts;
import mvpMatch.Backend1.UseCases.UseCaseCRUDUser;
import mvpMatch.Backend1.UseCases.UseCaseAuthentication;
import mvpMatch.Backend1.UseCases.UseCaseCoins;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UseCaseCRUDUser useCaseCRUDUser;

    @ResponseBody
    @PostMapping(consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<User> addUser(HttpServletRequest request, @RequestBody User user) {
        return useCaseCRUDUser.addUser(user);
    }
    @ResponseBody
    @GetMapping(consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<List<User>> findAllUsers(HttpServletRequest request) {
       return useCaseCRUDUser.getallUsers();
    }
    @ResponseBody
    @GetMapping(value ="/{username}",consumes ="application/json" ,produces = "application/json")
    public ResponseEntity<User> getUser(HttpServletRequest request, @PathVariable String username) {

        return useCaseCRUDUser.getUserById(username);
    }

    @ResponseBody
    @PatchMapping(value = "/{username}", consumes =" application/json" ,produces = "application/json")
    public ResponseEntity<Void> updateUser(HttpServletRequest request, @PathVariable String username,@RequestBody User user) {

        return useCaseCRUDUser.updateUser(username,user);
    }

    @PutMapping(value = "/deposit/{amount}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> updateAmount(HttpServletRequest request, @PathVariable int amount){

       return useCaseCRUDUser.updateAmount(amount);
    }

    @PutMapping(value = "/reset", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> resetAmount(HttpServletRequest request){

        return useCaseCRUDUser.resetAmount();
    }

    @PostMapping(value = "/buy/{productId}/{amount}")
    public ResponseEntity<BuyOutput> buy(HttpServletRequest request, @PathVariable Integer productId ,@PathVariable Integer amount ){

            return useCaseCRUDUser.buyByProduct(productId,amount);
    }

}
