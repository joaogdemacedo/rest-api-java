package mvpMatch.Backend1.Configurations;

import mvpMatch.Backend1.UseCases.UseCaseAuthentication;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;


@Component
public class Authentication implements HandlerInterceptor {
    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    UseCaseAuthentication useCaseAuthentication;

    private static String getMethod = "GET";
    private static String postMethod = "POST";

    private static String patchMethod = "PATCH";
    private static String authorizationHeaderToken = "authorization";

    private static String productURI = "/products";
    private static String userURI = "/users";

    private static String errorURI = "/error";

    private static String productIDURIPattern = "\\/products\\/[0-9]*";

    private static String productByMaxCostPattern = "\\/products\\/cost\\/[0-9]*";

    private static String productByOwnerPattern = "\\/products\\/owner\\/[aA-zZ]*";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {

            //Get products should not require authentication
            if(getMethod.equals(request.getMethod()) && (productURI.equals(request.getRequestURI()) || request.getRequestURI().matches(productIDURIPattern) || request.getRequestURI().matches(productByMaxCostPattern) || request.getRequestURI().matches(productByOwnerPattern))){
                return true;
            }

            //Create new user should not require authentication
            if(postMethod.equals(request.getMethod()) && userURI.equals(request.getRequestURI())){
                return true;
            }

            if(errorURI.equals(request.getRequestURI())){
                return true;
            }

            String basicAuthHeaderValue = request.getHeader(authorizationHeaderToken);
            String username = useCaseAuthentication.getUsernameFromToken(basicAuthHeaderValue);
            useCaseAuthentication.setLoggedUsername(username);

            Boolean isValidBasicAuthRequest = useCaseAuthentication.validateBasicAuthentication(basicAuthHeaderValue);

            if(isValidBasicAuthRequest){
                return true;
            }

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;

        }catch (Exception e){
            log.error("Fail to authenticate"+e.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
        return false;

    }

}
