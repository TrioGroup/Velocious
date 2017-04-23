package trioidea.iciciappathon.com.trioidea.Services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;
import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.CheckTransactionDTO;
import trioidea.iciciappathon.com.trioidea.DTO.FlipkartProductInfoList;
import trioidea.iciciappathon.com.trioidea.DTO.FundTransferDto;
import trioidea.iciciappathon.com.trioidea.DTO.HistoryDateRangeDTO;
import trioidea.iciciappathon.com.trioidea.DTO.ItemLookUpDto;
import trioidea.iciciappathon.com.trioidea.DTO.SingleItem;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
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
    int i;
    private TransactionDto[] transactionDto;
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
        if (tokenId == null || tokenId.isEmpty()) {
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
            observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).toBlocking().subscribe(ServiceLayer.this);
        } else {
            //Event notify
        }
    }

    public void userInfo() {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    if (tokenId == null || tokenId.isEmpty())
                        autheticateUser();
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
                    if (tokenId == null || tokenId.isEmpty())
                        autheticateUser();
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
                    if (tokenId == null || tokenId.isEmpty())
                        autheticateUser();
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

    public void fundTransfer(final TransactionDto[] transactionDtoArrayList) {
        this.transactionDto = transactionDtoArrayList;
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    if (tokenId == null || tokenId.isEmpty())
                        autheticateUser();
                    FundTransferWS fundTransferWS = new FundTransferWS();
                    EventResponse eventResponse = fundTransferWS.fundTransfer(client_id, tokenId, transactionDtoArrayList);
                    if (!((FundTransferDto) eventResponse.getResponse()).getStatus().equalsIgnoreCase("success")) {
                        transactionDtoArrayList[i].setSyncFlag(true);
                    } else {
                        transactionDtoArrayList[i].setSyncFlag(false);
                    }
                    eventResponse.setResponse(transactionDtoArrayList[i]);
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String token) {
        tokenId = token;
    }

    public void historyByDate(final AllUsersInfoDTO allUsersInfoDTO, final Date fromDate, final Date toDate) {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    HistoryDateRangeWS historyDateRangeWS = new HistoryDateRangeWS();
                    EventResponse eventResponse = historyDateRangeWS.getHistory(client_id, tokenId, allUsersInfoDTO.getAccount_no(), fromDate, toDate);
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }

    public void checkTransactionOnServer(final TransactionDto[] transactionDtos) {
        Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                try {
                    CheckTransactionWS checkTransactionWS = new CheckTransactionWS();
                    EventResponse eventResponse = checkTransactionWS.checkOnServer(transactionDtos);
                    return eventResponse;
                } catch (Exception e) {
                    return e;
                }
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).subscribe(ServiceLayer.this);
    }

    public void getItemsFromAmazon(final String searchItem,final int pageNo) {
        final Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {

                ItemSearchAWS itemSearchAWS = new ItemSearchAWS();
                EventResponse eventResponse = itemSearchAWS.getItem(searchItem,pageNo);
                return eventResponse;
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).doOnCompleted(new Action0() {
            @Override
            public void call() {
                observable.unsubscribeOn(Schedulers.io());
            }
        }).subscribe(ServiceLayer.this);
    }
    public void getItemDetails(final String itemASIN) {
        final Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                ItemLookUp itemLookUp=new ItemLookUp();
                EventResponse eventResponse = itemLookUp.getItem(itemASIN);
                return eventResponse;
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).doOnCompleted(new Action0() {
            @Override
            public void call() {
                observable.unsubscribeOn(Schedulers.io());
            }
        }).subscribe(ServiceLayer.this);
    }
    public void getItemsFromFlipkart(final String searchItem) {
        final Observable observable = Observable.fromCallable(new Callable() {
            @Override
            public Object call() throws Exception {
                String query=searchItem.replace(" ","+");
                GetFlipkartProducts getFlipkartProducts=new GetFlipkartProducts();
                EventResponse eventResponse = getFlipkartProducts.getProducts(query);
                return eventResponse;
            }
        });
        observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.computation()).doOnCompleted(new Action0() {
            @Override
            public void call() {
                observable.unsubscribeOn(Schedulers.io());
            }
        }).subscribe(ServiceLayer.this);
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
        if (o != null)
            switch (((EventResponse) o).getEvent()) {
                case EventNumbers.AUTHENTICATE_USER:
                    ArrayList<AuthenticateDto> authenticateDtoArrayList = (ArrayList<AuthenticateDto>) ((EventResponse) o).getResponse();
                    tokenId = authenticateDtoArrayList.get(0).getToken();
                    Log.e("Event Authenticate user", tokenId);
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
                    ArrayList<BalanceEnquiryDTO> balanceEnquiryDTOArrayList = (ArrayList<BalanceEnquiryDTO>) ((EventResponse) o).getResponse();
                    Log.e("Event balance enquiry", "" + balanceEnquiryDTOArrayList.get(0).getBalance());
                    ((EventResponse) o).setEvent(EventNumbers.BALANCE_ENQUIRY);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.ACCOUNT_SUMMARY:
                    ArrayList<BankAccountSummaryDTO> bankAccountSummaryDTO = (ArrayList<BankAccountSummaryDTO>) ((EventResponse) o).getResponse();
                    Log.e("Event account summary", "" + bankAccountSummaryDTO.get(0).getAccountno());
                    ((EventResponse) o).setEvent(EventNumbers.ACCOUNT_SUMMARY);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.FUND_TRANSFER:
                    FundTransferDto fundTransferDto;
                    TransactionDto transactionDto;
                    if ((((EventResponse) o).getResponse()).getClass().getName().equalsIgnoreCase("FundTransferDTO")) {
                        fundTransferDto = (FundTransferDto) ((EventResponse) o).getResponse();
                    } else {
                        transactionDto = (TransactionDto) ((EventResponse) o).getResponse();
                    }
                    Log.e("Event fund transfer", "here");
                    ((EventResponse) o).setEvent(EventNumbers.FUND_TRANSFER);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.HISTORY_DATE_RANGE:
                    ArrayList<HistoryDateRangeDTO> historyDateRangeDTOs = (ArrayList<HistoryDateRangeDTO>) ((EventResponse) o).getResponse();
                    Log.e("Event account summary", "" + historyDateRangeDTOs.get(0).getAccountno());
                    ((EventResponse) o).setEvent(EventNumbers.HISTORY_DATE_RANGE);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.CHECK_TRANSACTION_EVENT:
                    TransactionDto[] checkTransactionDTOArrayList = (TransactionDto[]) ((EventResponse) o).getResponse();
                    Log.e("Event transaction", "" + checkTransactionDTOArrayList[0].getSenderID());
                    ((EventResponse) o).setEvent(EventNumbers.CHECK_TRANSACTION_EVENT);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.AMAZON_GET_PRODUCTS:
                    ArrayList<SingleItem> singleItems = (ArrayList<SingleItem>) ((EventResponse) o).getResponse();
                    Log.e("Event get Products", "" + singleItems.get(0).getASIN());
                    ((EventResponse) o).setEvent(EventNumbers.AMAZON_GET_PRODUCTS);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.AMAZON_GET_ITEM:
                    ItemLookUpDto singleItem = (ItemLookUpDto) ((EventResponse) o).getResponse();
                    Log.e("Event get Products", "" + singleItem.getASIN());
                    ((EventResponse) o).setEvent(EventNumbers.AMAZON_GET_ITEM);
                    rxBus.send((EventResponse) o);
                    break;
                case EventNumbers.FLIPKART_GET_ITEM:
                    List<FlipkartProductInfoList> flipkartProductInfoList = (List<FlipkartProductInfoList>) ((EventResponse) o).getResponse();
                    Log.e("Event get Products", "" + flipkartProductInfoList.get(0).getProductBaseInfoV1().getProductId());
                    ((EventResponse) o).setEvent(EventNumbers.FLIPKART_GET_ITEM);
                    rxBus.send((EventResponse) o);
                    break;
            }

    }
}
