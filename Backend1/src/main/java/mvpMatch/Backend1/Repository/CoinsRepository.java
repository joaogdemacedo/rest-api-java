package mvpMatch.Backend1.Repository;

import mvpMatch.Backend1.DataProvider.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinsRepository extends JpaRepository<Coin,Integer> {
}
