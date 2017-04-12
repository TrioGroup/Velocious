package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 11/04/2017.
 */
public class FundTransferDto {
    long destination_accountno;
    String transaction_date;
    long referance_no;
    double transaction_amount;
    String payee_name;
    long payee_id;
    String status;

    public long getDestination_accountno() {
        return destination_accountno;
    }

    public void setDestination_accountno(long destination_accountno) {
        this.destination_accountno = destination_accountno;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }

    public long getReferance_no() {
        return referance_no;
    }

    public void setReferance_no(long referance_no) {
        this.referance_no = referance_no;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getPayee_name() {
        return payee_name;
    }

    public void setPayee_name(String payee_name) {
        this.payee_name = payee_name;
    }

    public long getPayee_id() {
        return payee_id;
    }

    public void setPayee_id(long payee_id) {
        this.payee_id = payee_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
