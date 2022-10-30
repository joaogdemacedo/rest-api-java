package mvpMatch.Backend1.UseCaseTest;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.UserRepository;
import mvpMatch.Backend1.UseCases.UseCaseAuthentication;
import mvpMatch.Backend1.UseCases.UseCaseCRUDUser;
import mvpMatch.Backend1.UseCases.UseCaseCoins;
import org.hibernate.service.spi.InjectService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UseCaseCRUDUserTest {


    @InjectMocks
    UseCaseCRUDUser useCaseCRUDUser;

    @InjectMocks
    UseCaseAuthentication useCaseAuthentication;

    @Autowired
    UserRepository userRepository;

    @Test
    public void givenValidRequest_WhenBuy_thenSuccess(){

        when(useCaseCRUDUser.getUserRole(Mockito.any())).thenReturn("buyer");

        useCaseCRUDUser.buyByProduct(null,null);

    }

}
