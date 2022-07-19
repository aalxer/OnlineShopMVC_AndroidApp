package com.example.onlineshopmvc.paymentUnit;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

public class PaymentUnitImpl implements PaymentUnit {

    // PaymentsClient erstellen:
    // Die PaymentsClient wird für die Interaktion mit der Google Pay API verwendet
    @Override
    public PaymentsClient getPaymentClient(Activity activity) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        PaymentsClient paymentsClient = Wallet.getPaymentsClient(activity,walletOptions);
        return paymentsClient;
    }

    @Override
    public boolean isReadyToPay(Activity activity, PaymentsClient paymentsClient) {

        IsReadyToPayRequest readyToPayRequest = IsReadyToPayRequest.fromJson(GPaymentsHelper.getIsReadyToPayRequest().toString());
        // Der Aufruf von isReadyToPay ist asynchron und gibt eine Task zurück. Wir müssen eine bereitstellen:
        Task<Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
        // OnCompleteListener wird ausgelöst, wenn das Ergebnis des Aufrufs bekannt ist:
        task.addOnCompleteListener(activity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> completeTask) {
                if(completeTask.isSuccessful()) {
                    // TODO return true um die Zahlungsbutton zu zeigen
                } else {
                    // TODO Exception
                }
            }
        });
        return false;
    }

    @Override
    public PaymentDataRequest loadPaymentData(long amount) {
        PaymentDataRequest request = PaymentDataRequest.fromJson(GPaymentsHelper.getPaymentDataRequest(amount).toString());
        return request;
    }
}