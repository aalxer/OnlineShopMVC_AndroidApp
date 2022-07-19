package com.example.onlineshopmvc.paymentUnit;

import android.app.Activity;

import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

public interface PaymentUnit {

    /**
     * Create a Payment Client (Instance of a PaymentsClient Class) to use it in the Activity
     * @param activity the current Activity where the Payment Client has to be used, as a Parameter to call the method getPaymentsClient()
     * @return a Payment Client Instance from PaymentsClient Class for interaction with the Google Pay API
     */
    PaymentsClient getPaymentClient(Activity activity);

    /**
     * Determine the viewer's ability to pay with a payment method (using GPay) supported by the app
     * @param activity the current Activity
     * @param paymentsClient the current interaction Client which to be checked
     * @return true if able to pay
     *         false if not able to pay
     */
    boolean isReadyToPay(Activity activity, PaymentsClient paymentsClient);

    /**
     * Request the Payment to open the Payment Sheet
     * @param amount amount of the Order
     * @return PaymentDataRequest Object
     */
    PaymentDataRequest loadPaymentData(long amount);
}
