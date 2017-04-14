package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 14/04/2017.
 */
public class TransactionDTOEncrypted {
    String transactionId;
    String senderID;
    String senderName;
    String receiverId;
    String receiverName;
    String amount;
    String time;
    String balance;
    String syncFlag;

    public TransactionDTOEncrypted(String transactionId, String senderID, String senderName, String receiverId, String receiverName, String amount, String time, String balance, String syncFlag) {
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

    public TransactionDTOEncrypted()
    {}

    public String getTransactionId() {

        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getSyncFlag() {
        return syncFlag;
    }

    public void setSyncFlag(String syncFlag) {
        this.syncFlag = syncFlag;
    }
}
