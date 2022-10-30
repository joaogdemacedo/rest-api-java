package mvpMatch.Backend1.UseCaseTest;

import mvpMatch.Backend1.Repository.UserRepository;
import mvpMatch.Backend1.UseCases.UseCaseCRUDUser;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.any;

public class UseCaseTest {

    @Resource
    UserRepository repository;

    @Mock
    UseCaseCRUDUser service;

    @Test
    public void sellerUserReturnForbidden(){

        String s = repository.findById(any()).get().getRole();

    }

}
