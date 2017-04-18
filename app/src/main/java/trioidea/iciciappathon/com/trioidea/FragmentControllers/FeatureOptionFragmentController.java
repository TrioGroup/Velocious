package trioidea.iciciappathon.com.trioidea.FragmentControllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import rx.Observer;
import rx.Subscription;
import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.FundTransferDto;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.Fragments.FeatureOptionFragment;
import trioidea.iciciappathon.com.trioidea.Fragments.RegistrationFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.ServiceLayer;

/**
 * Created by asus on 02/04/2017.
 */
public class FeatureOptionFragmentController implements Observer, View.OnClickListener {
    FeatureOptionFragment featureOptionFragment;
    String temp = null;
    public Subscription subscription;
    AllUsersInfoDTO allUsersInfoDTO=null;
    RxBus rxBus;

    public FeatureOptionFragmentController(FeatureOptionFragment fragment) {
        featureOptionFragment = fragment;
        rxBus = RxBus.getInstance();
        subscription=rxBus.toObserverable().subscribe(this);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(Object o) {
        final EventResponse eventResponse = (EventResponse) o;
        switch (eventResponse.getEvent()) {
            case EventNumbers.AUTHENTICATE_USER:
                SharedPreferences.Editor editor = featureOptionFragment.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
                ArrayList<AuthenticateDto> authenticateDtoArrayList = (ArrayList<AuthenticateDto>) ((EventResponse) o).getResponse();
                editor.putString("tokenId",authenticateDtoArrayList.get(0).getToken());
                editor.commit();
                break;
            case EventNumbers.All_USER_INFO:
                ArrayList<AllUsersInfoDTO> allUsersInfoDTOArrayList = (ArrayList<AllUsersInfoDTO>) ((EventResponse) o).getResponse();
                Log.e("Event All user frag", "" + allUsersInfoDTOArrayList.get(1).getAccount_no());
                allUsersInfoDTO=allUsersInfoDTOArrayList.get(0);
                ServiceLayer.getServiceLayer().balanceEnquiry(allUsersInfoDTO);
                break;
            case EventNumbers.BALANCE_ENQUIRY:
                ArrayList<BalanceEnquiryDTO> balanceEnquiryDTOArrayList = (ArrayList<BalanceEnquiryDTO>) ((EventResponse) o).getResponse();
                Log.e("Event balance enq frag", "" + balanceEnquiryDTOArrayList.get(1).getBalance());
                ((EventResponse) o).setEvent(EventNumbers.BALANCE_ENQUIRY);
                ServiceLayer.getServiceLayer().bankAccountSummary(allUsersInfoDTO);

                break;
            case EventNumbers.ACCOUNT_SUMMARY:
                ArrayList<BankAccountSummaryDTO> bankAccountSummaryDTO = (ArrayList<BankAccountSummaryDTO>) ((EventResponse) o).getResponse();
                Log.e("Event account summ frag", "" + bankAccountSummaryDTO.get(1).getAccountno());
                ((EventResponse) o).setEvent(EventNumbers.ACCOUNT_SUMMARY);
                break;
            case EventNumbers.FUND_TRANSFER:
                FundTransferDto fundTransferDto;
                TransactionDto transactionDto;
                if ((((EventResponse) o).getResponse()).getClass().getName().equalsIgnoreCase("FundTransferDTO")) {
                    fundTransferDto = (FundTransferDto) ((EventResponse) o).getResponse();
                } else {
                    transactionDto = (TransactionDto) ((EventResponse) o).getResponse();
                }
                Log.e("Event transfer frag", "here");
                ((EventResponse) o).setEvent(EventNumbers.FUND_TRANSFER);
                break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_offline_transaction:
                /*String encryptedValue1 = EncryptionClass.symmetricEncrypt("HI");
                String decryptedValue1 = EncryptionClass.symmetricDecrypt(encryptedValue1);
                System.out.println(decryptedValue1);
                Toast.makeText(featureOptionFragment.getActivity(), encryptedValue1 + ":" + decryptedValue1, Toast.LENGTH_SHORT).show();*/
                ((MainScreen)featureOptionFragment.getActivity()).startActivityTransferOffline();
                break;
            case R.id.btn_shopping_assist:
                //"t_id", "sender_id", "sender_name", "receiver_id", "receiver_name", "amount", "time", "balance", "sync_flag"
                ((MainScreen)featureOptionFragment.getActivity()).startActivityShopping();
                break;
        }
    }
}
