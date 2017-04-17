package trioidea.iciciappathon.com.trioidea.DTO;

/**
 * Created by asus on 17/04/2017.
 */
public class CheckTransactionDTO {
    String status;
    String message;
    String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
