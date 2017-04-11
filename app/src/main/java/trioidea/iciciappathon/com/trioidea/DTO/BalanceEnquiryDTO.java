package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 11/04/2017.
 */
public class BalanceEnquiryDTO {
    double balance;
    long accountno;
    String accounttype;
    String balancetime;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public long getAccountno() {
        return accountno;
    }

    public void setAccountno(long accountno) {
        this.accountno = accountno;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getBalancetime() {
        return balancetime;
    }

    public void setBalancetime(String balancetime) {
        this.balancetime = balancetime;
    }
}
