package mvpMatch.Backend1.Repository;

import mvpMatch.Backend1.DataProvider.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query(value="Select * From product WHERE owner=?1 ", nativeQuery = true)
    List<Product> findProductsByOwner(String owner);

    @Query(value="Select * From product WHERE cost<=?1 ", nativeQuery = true)
    List<Product> findProductsByMaxCost(int cost);

}
