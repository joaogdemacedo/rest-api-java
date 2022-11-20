package mvpMatch.Backend1.UseCases;

import mvpMatch.Backend1.DataProvider.BuyOutput;
import mvpMatch.Backend1.DataProvider.Coin;
import mvpMatch.Backend1.DataProvider.Product;
import mvpMatch.Backend1.DataProvider.User;
import mvpMatch.Backend1.Repository.CoinsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UseCaseCoins {

    @Autowired
    CoinsRepository coinsRepository;

    ArrayList<Integer> validCoins = new ArrayList<>(Arrays.asList(5, 10, 20, 50, 100));


    public void addCoin(int coinId){
        Coin coin = coinsRepository.findById(coinId).get();
        coin.setQuantity(coin.getQuantity()+1);
        coinsRepository.save(coin);
    }

    public void removeCoin(int coinId,int quantity){
        Coin coin = coinsRepository.findById(coinId).get();
        coin.setQuantity(coin.getQuantity()-quantity);
        coinsRepository.save(coin);
    }

    private int getCoinQuantity(int coinId){
        return coinsRepository.getReferenceById(coinId).getQuantity();
    }

    private ArrayList<Coin> getAllCoins(){
        ArrayList<Coin> coins = new ArrayList<>();
        coins.addAll(coinsRepository.findAll());
        return coins;
    }

    private int getMaxCoinAvailable(long leftChange){

        for(int i = validCoins.size()-1; i>=0; i--){
            if(getCoinQuantity(validCoins.get(i))>0 && validCoins.get(i) <= leftChange) {
                return validCoins.get(i);
            }
        }
        return 0;
    }

    private int getAvailableQuantity(int coinId, long leftChange) {

        int numberOfCoinsNeed = (int) Math.floorDiv(leftChange, validCoins.get(getIndex(coinId)));
        int quantityTaken;

        if (numberOfCoinsNeed >= getCoinQuantity(validCoins.get(getIndex(coinId)))) {
            quantityTaken = getCoinQuantity(validCoins.get(getIndex(coinId)));
        } else {
            quantityTaken = numberOfCoinsNeed;
        }
        removeCoin(coinId, quantityTaken);
        return quantityTaken;

    }

    private int getIndex(int coindId){
        switch (coindId){
           case  5 : return 0;

           case  10 : return 1;

           case  20 : return 2;

           case 50 : return 3;

           case 100 : return 4;

           default: return 0;
        }

    }

    public List<Integer> calculateChange(long totalSpent, long credit){
        ArrayList<Integer> arrayOfChanges = new ArrayList<Integer>(Arrays.asList(0,0,0,0,0));
        // indice da moeda mais alta -> maxcoin
        long change = credit - totalSpent;
        //
        int maxCoin;
        int numberOfCoins=0;

        while( change!=0) {

            maxCoin = getMaxCoinAvailable(change);

            if(maxCoin == 0){
                break;
            }
            numberOfCoins = getAvailableQuantity(maxCoin,change);

            arrayOfChanges.set(getIndex(maxCoin),numberOfCoins);

            arrayOfChanges.set(getIndex(maxCoin), numberOfCoins);

            change = change - ((long) maxCoin *numberOfCoins);

        }
        return arrayOfChanges;
    }

}
