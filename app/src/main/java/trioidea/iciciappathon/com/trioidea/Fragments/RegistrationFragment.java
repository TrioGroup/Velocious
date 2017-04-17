package trioidea.iciciappathon.com.trioidea.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import trioidea.iciciappathon.com.trioidea.Activities.MainScreen;
import trioidea.iciciappathon.com.trioidea.DTO.AllUsersInfoDTO;
import trioidea.iciciappathon.com.trioidea.DTO.BankAccountSummaryDTO;
import trioidea.iciciappathon.com.trioidea.DTO.TransactionDto;
import trioidea.iciciappathon.com.trioidea.DbHelper;
import trioidea.iciciappathon.com.trioidea.EncryptionClass;
import trioidea.iciciappathon.com.trioidea.FragmentControllers.RegistrationFragmentController;
import trioidea.iciciappathon.com.trioidea.R;

import android.support.design.widget.TextInputLayout;

/**
 * Created by asus on 13/04/2017.
 */
public class RegistrationFragment extends Fragment {

    EditText etName, etAddress, etPhoneNumber, etAadhar, etCard;
    TextInputLayout tilName, tilAddress, tilPhoneNumber, tilAadhar, tilCard;
    ImageButton register;
    double balance;
    AllUsersInfoDTO allUsersInfoDTO;
    RegistrationFragmentController registrationFragmentController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.registration, container, false);

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //   super.onSaveInstanceState(outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        registrationFragmentController = new RegistrationFragmentController(this);
        init();
        ((MainScreen)getActivity()).changeTitle("Register");
        resetUi();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        registrationFragmentController.subscription.unsubscribe();
    }
    public void init() {
        etName = (EditText) RegistrationFragment.this.getActivity().findViewById(R.id.et_name);
        //etName.addTextChangedListener(this);
        etAddress = (EditText) RegistrationFragment.this.getActivity().findViewById(R.id.et_address);
        etPhoneNumber = (EditText) RegistrationFragment.this.getActivity().findViewById(R.id.et_phone);
        etAadhar = (EditText) RegistrationFragment.this.getActivity().findViewById(R.id.et_account_number);
        etCard = (EditText) RegistrationFragment.this.getActivity().findViewById(R.id.et_card);
        register = (ImageButton) RegistrationFragment.this.getActivity().findViewById(R.id.btn_register);
        register.setOnClickListener(registrationFragmentController);
        tilName = (TextInputLayout) RegistrationFragment.this.getActivity().findViewById(R.id.til_name);
        tilAddress = (TextInputLayout) RegistrationFragment.this.getActivity().findViewById(R.id.til_address);
        tilPhoneNumber = (TextInputLayout) RegistrationFragment.this.getActivity().findViewById(R.id.til_phone);
        tilAadhar = (TextInputLayout) RegistrationFragment.this.getActivity().findViewById(R.id.til_aadhar);
        tilCard = (TextInputLayout) RegistrationFragment.this.getActivity().findViewById(R.id.til_card);
        addTextWatcher(tilName);
        addTextWatcher(tilAddress);
        addTextWatcher(tilPhoneNumber);
        addTextWatcher(tilAadhar);
        addTextWatcher(tilCard);

    }

    public boolean validateData() {
        boolean flag = true;
        if (etName.getText().toString().isEmpty()) {
            tilName.setError("Name is a required field.");
            tilName.setErrorEnabled(true);
            flag = false;
        }
        if (etAddress.getText().toString().isEmpty()) {
            tilAddress.setError("Address is a required field.");
            tilAddress.setErrorEnabled(true);
        }
        if (etPhoneNumber.getText().toString().isEmpty()) {
            tilPhoneNumber.setError("Phone Number is a required field.");
            tilPhoneNumber.setErrorEnabled(true);
            flag = false;
        }
        else if (etPhoneNumber.getText().toString().length() < 10) {
            tilPhoneNumber.setError("Phone Number invalid.");
            tilPhoneNumber.setErrorEnabled(true);
            flag = false;
        }
        if (etAadhar.getText().toString().isEmpty()) {
            tilAadhar.setError("Aadhar card number is a required field.");
            tilAadhar.setErrorEnabled(true);
            flag=false;
        }
        else if(etAadhar.getText().length() < 12)
        {
            tilAadhar.setError("Aadhar card number is invalid.");
            tilAadhar.setErrorEnabled(true);
            flag=false;
        }
        if(etCard.getText().toString().isEmpty())
        {
            tilCard.setError("Card Number is a required field.");
            tilCard.setErrorEnabled(true);
            flag=false;
        }
        else if(etCard.getText().length()<16)
        {
            tilCard.setError("Card Number is invalid.");
            tilCard.setErrorEnabled(true);
            flag=false;
        }
        return flag;
    }

    public void resetUi()
    {
        tilName.setErrorEnabled(false);
        tilPhoneNumber.setErrorEnabled(false);
        tilAddress.setErrorEnabled(false);
        tilAadhar.setErrorEnabled(false);
        tilCard.setErrorEnabled(false);
    }
    public void setDataInSharedPref(BankAccountSummaryDTO bankAccountSummaryDTO)
    {
        SharedPreferences.Editor editor = this.getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE).edit();
        editor.putString("name", EncryptionClass.symmetricEncrypt(etName.getText().toString()));
        editor.putString("address",EncryptionClass.symmetricEncrypt(etAddress.getText().toString()));
        editor.putString("phone",EncryptionClass.symmetricEncrypt(etPhoneNumber.getText().toString()));
        editor.putString("account",EncryptionClass.symmetricEncrypt(etAadhar.getText().toString()));
        editor.putString("card",EncryptionClass.symmetricEncrypt(etCard.getText().toString()));
        editor.putBoolean("registered",true);
        editor.putString("cust_id",EncryptionClass.symmetricEncrypt(String.valueOf(bankAccountSummaryDTO.getCustid())));
        TransactionDto transactionDto=new TransactionDto(0,0,"Bank",Long.parseLong(etAadhar.getText().toString()),etName.getText().toString(),0, String.valueOf(System.currentTimeMillis()),bankAccountSummaryDTO.getBalance(),true);
        DbHelper.getInstance(this.getActivity()).insertTransaction(transactionDto);
        //editor.putString("balance",EncryptionClass.symmetricEncrypt(String.valueOf(bankAccountSummaryDTO.getBalance())));
        editor.commit();
    }
    private void addTextWatcher(final TextInputLayout textInputLayout)
    {
        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public String getAccountNumber()
    {
        return etAadhar.getText().toString();
    }
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}


