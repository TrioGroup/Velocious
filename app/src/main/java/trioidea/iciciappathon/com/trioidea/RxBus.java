package trioidea.iciciappathon.com.trioidea;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by pritesh.gandhi on 7/7/16.
 */
public class RxBus {


    private static RxBus rxBus = null;
    private final Subject<Object, Object> _bus = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        if (rxBus == null)
            rxBus = new RxBus();
        return rxBus;
    }

    public void send(EventResponse o) {
        _bus.onNext(o);
    }

    public void error(Object o) {
        _bus.onError((Throwable) o);
    }

    public Observable<Object> toObserverable() {
        return _bus;
    }

}