package mvpMatch.Backend1.DataProvider;

import java.util.List;

public class BuyOutput {

    private long totalSpent;
    private String productName;
    private List<Integer> change;

    public BuyOutput(long totalSpent, String productName, List<Integer> change){
        this.totalSpent=totalSpent;
        this.productName=productName;
        this.change=change;
    }

    public long getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(int totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Integer> getChange() {
        return change;
    }

    public void setChange(List<Integer> change) {
        this.change = change;
    }
}
