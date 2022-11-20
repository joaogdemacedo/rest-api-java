package mvpMatch.Backend1.UseCaseTest;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Coin;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.CoinsRepository;
import mvpMatch.Backend1.Repository.UserRepository;
import mvpMatch.Backend1.UseCases.UseCaseAuthentication;
import mvpMatch.Backend1.UseCases.UseCaseCRUDProducts;
import mvpMatch.Backend1.UseCases.UseCaseCRUDUser;
import mvpMatch.Backend1.UseCases.UseCaseCoins;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UseCaseTest {

    @Mock // Add this
    UserRepository repository;

    @Mock
    UseCaseCRUDProducts useCaseCRUDProducts;

    @Mock
    UseCaseCoins useCaseCoins;

    @InjectMocks // Add this
    UseCaseCRUDUser service;

    @Mock
    UseCaseAuthentication useCaseAuthentication;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sellerUserReturnForbidden(){
        User user = new User();
        user.setUsername("username");
        user.setRole("role");

        when(useCaseAuthentication.getLoggedUsername()).thenReturn("user");
        when(repository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<BuyOutput> outPut = service.buyByProduct(0,0);
        Assertions.assertNull(outPut.getBody());
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(),outPut.getStatusCode().value());

    }

    @Test
    public void buyByProductSuccess(){
        User user = new User();
        user.setUsername("username");
        user.setRole("buyer");
        user.setCredit(100);

        Product product = new Product();
        product.setName("MockProduct");
        product.setId(1);
        product.setAmount(2);
        product.setOwner("admin");
        product.setCost(15);


        when(useCaseAuthentication.getLoggedUsername()).thenReturn("user");
        when(repository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity<Product> rproduct = new ResponseEntity<>(product,HttpStatus.OK);
        when(useCaseCRUDProducts.getProduct(Mockito.anyInt())).thenReturn(rproduct);

        List<Integer> mockChange = new ArrayList<Integer>(Arrays.asList(1,1,1,1,0));

        when(useCaseCRUDProducts.removeProduct(Mockito.any(Product.class),Mockito.anyInt())).thenReturn(true);

        Coin mockCoin = new Coin();
        mockCoin.setQuantity(5);
        when(useCaseCoins.calculateChange(Mockito.anyLong(),Mockito.anyLong())).thenReturn(mockChange);
        BuyOutput MockBuyOutput = new BuyOutput(product.getCost(),product.getName(),mockChange);

        ResponseEntity<BuyOutput> outPut = service.buyByProduct(1,1);

        Assertions.assertNotNull(outPut);
        Assertions.assertNotNull(outPut.getBody());
        Assertions.assertEquals(MockBuyOutput.getChange(),outPut.getBody().getChange());
        Assertions.assertEquals(MockBuyOutput.getProductName(),outPut.getBody().getProductName());

    }

    @Test
    public void updateAmountSuccess(){
        User user = new User();
        user.setUsername("username");
        user.setRole("buyer");
        user.setCredit(0);

        when(useCaseAuthentication.getLoggedUsername()).thenReturn("user");
        when(repository.findById(any())).thenReturn(Optional.of(user));
        when(repository.saveAndFlush(Mockito.any(User.class))).thenReturn(user);
        doNothing().when(useCaseCoins).addCoin(Mockito.anyInt());

        ResponseEntity<User> outPut = service.updateAmount(100);
        Assertions.assertNotNull(outPut);
        Assertions.assertNotNull(outPut.getBody());
        Assertions.assertEquals(100,outPut.getBody().getCredit());
    }
}
