package trioidea.iciciappathon.com.trioidea.FragmentControllers;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import rx.Observer;
import rx.Subscription;
import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.AuthenticateDto;
import trioidea.iciciappathon.com.trioidea.DTO.BalanceEnquiryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.EventNumbers;
import trioidea.iciciappathon.com.trioidea.EventResponse;
import trioidea.iciciappathon.com.trioidea.Fragments.RegistrationFragment;
import trioidea.iciciappathon.com.trioidea.R;
import trioidea.iciciappathon.com.trioidea.RxBus;
import trioidea.iciciappathon.com.trioidea.Services.ServiceLayer;

/**
 * Created by asus on 14/04/2017.
 */
public class RegistrationFragmentController implements View.OnClickListener, Observer {

    public Subscription subscription;
    RegistrationFragment registrationFragment;
    RxBus rxBus = RxBus.getInstance();
    AlertDialog alertDialog;
    ProgressDialog progressDialog;

    public RegistrationFragmentController(RegistrationFragment registrationFragment) {
        this.registrationFragment = registrationFragment;
        subscription = rxBus.toObserverable().subscribe(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                ConnectivityManager connectivityManager = ((ConnectivityManager) registrationFragment.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                    if (registrationFragment.validateData()) {
                        progressDialog = new ProgressDialog(registrationFragment.getActivity());
                        progressDialog.setMessage("Registering, please wait...");
                        progressDialog.setProgress(0);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if (ServiceLayer.getServiceLayer().getTokenId() == null || ServiceLayer.getServiceLayer().getTokenId().isEmpty())
                            ServiceLayer.getServiceLayer().autheticateUser();
                        else
                            ServiceLayer.getServiceLayer().userInfo();

                    }
                } else {
                    alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                    alertDialog.setMessage("Cannot connect to internet. Please try again later.");
                    alertDialog.setTitle("Alert");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                break;
        }
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
                    String tokenId = authenticateDtoArrayList.get(0).getToken();
                    Log.e("contr Authenticate user", tokenId);
                    SharedPreferences.Editor editor = registrationFragment.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
                    editor.putString("tokenId", authenticateDtoArrayList.get(0).getToken());
                    editor.commit();
                    ServiceLayer.getServiceLayer().setTokenId(tokenId);
                    ServiceLayer.getServiceLayer().userInfo();
                    break;
                case EventNumbers.All_USER_INFO:
                    boolean flag1 = false;
                    ArrayList<AllUsersInfoDTO> allUsersInfoDTOArrayList = (ArrayList<AllUsersInfoDTO>) ((EventResponse) o).getResponse();
                    Log.e("in controller", "" + allUsersInfoDTOArrayList.get(0).getAccount_no());
                    if (allUsersInfoDTOArrayList != null) {
                        for (int i = 0; i < allUsersInfoDTOArrayList.size(); i++) {
                            if (allUsersInfoDTOArrayList.get(i).getAccount_no() == Long.parseLong(registrationFragment.getAccountNumber())) {
                                ServiceLayer.getServiceLayer().bankAccountSummary(allUsersInfoDTOArrayList.get(i));
                                flag1 = true;
                            }
                        }
                        if(!flag1)
                        {
                            registrationFragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(progressDialog!=null)
                                    progressDialog.dismiss();
                                    final AlertDialog alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                                    alertDialog.setMessage("Could not verify your Account..");
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }

                    } else {
                        registrationFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                final AlertDialog alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                                alertDialog.setMessage("Problem to communicate to server. Please try again later.");
                                alertDialog.setTitle("Alert");
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                    }
                    break;
                case EventNumbers.ACCOUNT_SUMMARY:
                    boolean flag2 = false;
                    ArrayList<BankAccountSummaryDTO> bankAccountSummaryDTO = (ArrayList<BankAccountSummaryDTO>) ((EventResponse) o).getResponse();
                    Log.e("in controller acc summ", "" + bankAccountSummaryDTO.get(1).getAccountno());
                    if (bankAccountSummaryDTO != null) {
                        for (int i = 0; i < bankAccountSummaryDTO.size(); i++) {
                            if (bankAccountSummaryDTO.get(i).getAccountno() == Long.parseLong(registrationFragment.getAccountNumber())) {
                                registrationFragment.setDataInSharedPref(bankAccountSummaryDTO.get(i));
                                flag2 = true;
                                registrationFragment.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        ((MainScreen) registrationFragment.getActivity()).startFeaturesOptionFragment();
                                    }
                                });
                            }
                        }
                        if (!flag2) {
                            registrationFragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    final AlertDialog alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                                    alertDialog.setMessage("Could not verify your Account..");
                                    alertDialog.setTitle("Alert");
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });


                        }

                    } else {
                        registrationFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                final AlertDialog alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                                alertDialog.setMessage("Problem to communicate to server. Please try again later.");
                                alertDialog.setTitle("Alert");
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                    }

                    break;
            }
        else {
            registrationFragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertDialog = new AlertDialog.Builder(registrationFragment.getActivity()).create();
                    alertDialog.setMessage("There was some error to connect. Please try again later.");
                    alertDialog.setTitle("Alert");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });

        }
    }
}
