package trioidea.iciciappathon.com.trioidea;

/**
 * Created by pritesh.gandhi on 7/7/16.
 */
public class EventResponse {
    public Object response;
    public int event;

    public EventResponse(int event) {
        this.event=event;
    }

    public EventResponse(Object response, int event) {
        this.response = response;
        this.event = event;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }
}
