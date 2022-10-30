package mvpMatch.Backend1.UseCases;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.Immutable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;

import static java.util.HashSet.newHashSet;
import static java.util.Map.entry;

@Service
public class UseCaseCRUDUser {

    @Autowired
    UseCaseAuthentication useCaseAuthentication;

    @Autowired
    UseCaseCRUDProducts useCaseCRUDProducts;

    @Autowired
    UseCaseCoins useCaseCoins;
    private final Logger log = LogManager.getLogger(getClass());
    Set<Integer> validCoins = new HashSet<>(Arrays.asList(5, 10, 20, 50, 100));
    private final String buyerRole ="buyer";
    private final String sellerRole ="seller";


    @Autowired
    UserRepository userRepository;

    public ResponseEntity<User> addUser(User user){
        try {
            String hashPassword = useCaseAuthentication.getArgon2().hash(4, 2048, 8, user.getPassword().toCharArray());
            User newUser = new User();
            newUser.setUsername(user.getUsername());
            newUser.setPassword(hashPassword);
            newUser.setCredit(0);
            newUser.setRole(user.getRole());

            userRepository.saveAndFlush(newUser);

            return new ResponseEntity<>(newUser,HttpStatus.CREATED);

        }catch (Exception e){
            log.error("Error add user",e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    public ResponseEntity<List<User>> getallUsers(){

        if(useCaseAuthentication.isAdmin(useCaseAuthentication.getLoggedUsername())){
            List<User> users = userRepository.findAll();

            return new ResponseEntity<>(users,HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

    }

    public String getUserRole(String username){
        return userRepository.findById(username).get().getRole();
    }

    public ResponseEntity<User> getUserById(String username){

        if(useCaseAuthentication.isAdmin(useCaseAuthentication.getLoggedUsername()) || useCaseAuthentication.getLoggedUsername().equals(username)){
            User getUser = userRepository.findById(username).get();
            return new ResponseEntity<>(getUser,HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    public ResponseEntity<Void> updateUser(String username,User user){

        if(useCaseAuthentication.getLoggedUsername().equals(username)){

            if(user.getPassword() != null) {
                String password = useCaseAuthentication.getArgon2().hash(4, 2048, 8, user.getPassword().toCharArray());
                User dbUser = userRepository.findById(username).get();
                dbUser.setPassword(user.getPassword() != null ? password : null);
                userRepository.save(dbUser);
            }
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);


    }

    public ResponseEntity<User> updateAmount(int amount){

        String username = useCaseAuthentication.getLoggedUsername();
        String role = getUserRole(username);

        if (role.equals(buyerRole)){
            if (isValidAmount(amount)){
                User user = userRepository.findById(username).get();

                user.setCredit(user.getCredit()+amount);
                userRepository.saveAndFlush(user);
                useCaseCoins.addCoin(amount);
                return new ResponseEntity<>(user,HttpStatus.OK);
            }
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);
        }

    }

    public boolean isValidAmount(int amount){
        return validCoins.contains(amount);
    }

    public ResponseEntity<User> resetAmount(){

        String username = useCaseAuthentication.getLoggedUsername();
        String role =getUserRole(username);

        if (role.equals(buyerRole)){
            User updatedUser = userRepository.findById(username).get();
            updatedUser.setCredit(0);
            userRepository.saveAndFlush(updatedUser);
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);

    }

    public ResponseEntity<BuyOutput> buyByProduct(Integer productId,Integer amount ){

        if(getUserRole(useCaseAuthentication.getLoggedUsername()).equals(buyerRole)) {

            Product product = useCaseCRUDProducts.getProduct(productId).getBody();

            if(product == null){
                return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
            }

            long credit = getUserById(useCaseAuthentication.getLoggedUsername()).getBody().getCredit();
            long totalSpent = amount * product.getCost();

            if(credit >= totalSpent && useCaseCRUDProducts.removeProduct(product,amount)) {
                BuyOutput buyOutput = new BuyOutput(totalSpent, product.getName(), useCaseCoins.calculateChange(totalSpent, credit));

                resetAmount();
                return new ResponseEntity<>(buyOutput, HttpStatus.OK);
            }

            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null,HttpStatus.FORBIDDEN);

    }

}
