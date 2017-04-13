package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 10/04/2017.
 */
public class AllUsersInfoDTO {
    long debitcardno;
    long cust_id;
    long account_no;

    public long getCust_id() {
        return cust_id;
    }

    public void setCust_id(long cust_id) {
        this.cust_id = cust_id;
    }

    public long getAccount_no() {
        return account_no;
    }

    public void setAccount_no(long account_no) {
        this.account_no = account_no;
    }

    public long getDebitcardno() {
        return debitcardno;
    }

    public void setDebitcardno(long debitcardno) {
        this.debitcardno = debitcardno;
    }

}
