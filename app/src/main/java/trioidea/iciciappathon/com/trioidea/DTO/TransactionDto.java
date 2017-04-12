package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 06/04/2017.
 */
public class TransactionDto  {
    int transactionId;
    int senderID;
    String senderName;
    int receiverId;
    String receiverName;
    double amount;
    String time;
    double balance;
    boolean syncFlag;

    public TransactionDto()
    {    }
    public TransactionDto(int transactionId, int senderID, String senderName, int receiverId, String receiverName, double amount, String time, double balance, boolean syncFlag) {
        this.transactionId = transactionId;
        this.senderID = senderID;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.amount = amount;
        this.time = time;
        this.balance = balance;
        this.syncFlag = syncFlag;
    }

    public boolean isSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(boolean syncFlag) {
        this.syncFlag = syncFlag;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }



}
