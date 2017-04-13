package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 13/04/2017.
 */
public class HistoryDateRangeDTO {
    String transactiondate;
    double closing_balance;
    long accountno;
    String credit_debit_flag;
    double transaction_amount;
    String remark;

    public String getTransactiondate() {
        return transactiondate;
    }

    public void setTransactiondate(String transactiondate) {
        this.transactiondate = transactiondate;
    }

    public double getClosing_balance() {
        return closing_balance;
    }

    public void setClosing_balance(double closing_balance) {
        this.closing_balance = closing_balance;
    }

    public long getAccountno() {
        return accountno;
    }

    public void setAccountno(long accountno) {
        this.accountno = accountno;
    }

    public String getCredit_debit_flag() {
        return credit_debit_flag;
    }

    public void setCredit_debit_flag(String credit_debit_flag) {
        this.credit_debit_flag = credit_debit_flag;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
