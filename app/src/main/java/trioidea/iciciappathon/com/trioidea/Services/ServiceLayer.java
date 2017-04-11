package trioidea.iciciappathon.com.trioidea.Services;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.schedulers.Schedulers;
import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.RxBus;

/**
 * Created by asus on 09/04/2017.
 */
public class ServiceLayer implements Observer {
    private static ServiceLayer serviceLayer;
    private static String tokenId = null;
    RxBus rxBus = RxBus.getInstance();
    private String client_id = "sandeshbankar24@gmail.com";
    private String pwd = "LP549V52";

    private ServiceLayer() {
    }

    public static ServiceLayer getServiceLayer() {
        if (serviceLayer == null)
            serviceLayer = new ServiceLayer();
        return serviceLayer;
    }

    public void autheticateUser() {
        if (tokenId == null) {
            Observable observable = Observable.fromCallable(new Callable() {
                @Override
                public Object call() throws Exception {
                    try {
                        AuthenticateClient authenticateClient = new AuthenticateClient();
                        EventResponse eventResponse = authenticateClient.authenticateClientWB(client_id, pwd);
                        return eventResponse;
                    } catch (Exception e) {
                        return e;
                    }
                }
            });
            observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
        } else {
            //Event notify
        }
    }

    public void userInfo() {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    AllUserInfoWS authenticateClient = new AllUserInfoWS();
                    EventResponse eventResponse = authenticateClient.allUserInfo(client_id);
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }

            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }

    public void balanceEnquiry(final AllUsersInfoDTO allUsersInfoDTO) {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    BalanceEnquiryWS balanceEnquiryWS = new BalanceEnquiryWS();
                    EventResponse eventResponse = balanceEnquiryWS.balanceEnquiry(client_id, tokenId, allUsersInfoDTO.getAccount_no());
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }

            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }

    public void bankAccountSummary(final AllUsersInfoDTO allUsersInfoDTO) {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    AccountSummaryWS accountSummaryWS = new AccountSummaryWS();
                    EventResponse eventResponse = accountSummaryWS.getAccountSummary(client_id, tokenId, allUsersInfoDTO.getCust_id(), allUsersInfoDTO.getAccount_no());
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }

            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        EventResponse eventResponse = (EventResponse) o;
        switch (((EventResponse) o).getEvent()) {
            case EventNumbers.AUTHENTICATE_USER:
                AuthenticateDto authenticateDtoArrayList = (AuthenticateDto) ((EventResponse) o).getResponse();
                Log.e("Event Authenticate user", authenticateDtoArrayList.getToken());
                ((EventResponse) o).setEvent(EventNumbers.AUTHENTICATE_USER);
                rxBus.send((EventResponse) o);
                break;
            case EventNumbers.All_USER_INFO:
                ArrayList<AllUsersInfoDTO> allUsersInfoDTOArrayList = (ArrayList<AllUsersInfoDTO>) ((EventResponse) o).getResponse();
                Log.e("Event All user", "" + allUsersInfoDTOArrayList.get(0).getAccount_no());
                ((EventResponse) o).setEvent(EventNumbers.All_USER_INFO);
                rxBus.send((EventResponse) o);
                break;
            case EventNumbers.BALANCE_ENQUIRY:
                BalanceEnquiryDTO balanceEnquiryDTOArrayList = (BalanceEnquiryDTO) ((EventResponse) o).getResponse();
                Log.e("Event balance enquiry", "" + balanceEnquiryDTOArrayList.getBalance());
                ((EventResponse) o).setEvent(EventNumbers.BALANCE_ENQUIRY);
                rxBus.send((EventResponse) o);
                break;
            case EventNumbers.ACCOUNT_SUMMARY:
                BankAccountSummaryDTO bankAccountSummaryDTO = (BankAccountSummaryDTO) ((EventResponse) o).getResponse();
                Log.e("Event account summary", "" + bankAccountSummaryDTO.getAccountno());
                ((EventResponse) o).setEvent(EventNumbers.ACCOUNT_SUMMARY);
                rxBus.send((EventResponse) o);
                break;

        }
    }
}
