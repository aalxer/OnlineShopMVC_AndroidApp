package com.example.onlineshopmvc.appActivites.saleActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopmvc.R;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleApp;
import com.example.onlineshopmvc.appController.saleAppLogic.SaleAppImpl;
import com.example.onlineshopmvc.appModel.Order;
import com.example.onlineshopmvc.paymentUnit.GPaymentsHelper;
import com.example.onlineshopmvc.paymentUnit.PaymentUnit;
import com.example.onlineshopmvc.paymentUnit.PaymentUnitImpl;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOutActivity extends AppCompatActivity {

    private SaleApp saleApp;
    private PaymentUnit paymentUnit;
    private Order order;
    private PaymentsClient paymentsClient;
    // Einfach eine Konstante Zahl, um eine Anfrage für Zahlungsdatenaktivitäten zu verfolgen:
    private int LOAD_PAYMENT_DATA_REQUEST_CODE = 845;

    private ImageView googlePayButton;
    private TextView orderNumberText;
    private TextView orderNumberNr;
    private TextView shippingToText;
    private TextView nameText;
    private TextView firstLineAddressText;
    private TextView cityAndPostCodeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Die Activity bekommt die Bestellung als Serializable-Object übergeben und löst sie auf:
        this.order = (Order) getIntent().getSerializableExtra("order");
        this.saleApp = SaleAppImpl.getSaleAppInstance(this);
        this.paymentUnit = new PaymentUnitImpl();

        this.orderNumberText = findViewById(R.id.orderNumberTextPaymentLayout);
        this.orderNumberNr = findViewById(R.id.orderNumberPaymentLayout);
        this.shippingToText = findViewById(R.id.shippingToTextPaymentLayout);
        this.nameText = findViewById(R.id.nameFeldPaymentLayout);
        this.firstLineAddressText = findViewById(R.id.firstLineAddressPaymentLayout);
        this.cityAndPostCodeText = findViewById(R.id.secondLineAddressPaymentLayout);
        this.googlePayButton = findViewById(R.id.gpayBtn);

        // Set die Infos aus dem order Objekt:
        orderNumberNr.setText(String.valueOf(order.getOrderID()));
        nameText.setText(order.getCustomerName());
        firstLineAddressText.setText(order.getFirstLineAddress());
        String cityAndPostCode = order.getCity()+", "+order.getPostcode();
        cityAndPostCodeText.setText(cityAndPostCode);

        // Intilaisiere einen PaymentClient zum interagieren mit GPay API:
        this.paymentsClient = this.paymentUnit.getPaymentClient(this);

        // Checke ob die Zahlung durch GPay erlaubt ist und dann zeige die Button:
        this.isReadyToPay();

        // Beim Klicken wird eine Zahlungsanfrgae an GPay API geschickt:
        googlePayButton.setOnClickListener(this::requestPayment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                    break;

                case Activity.RESULT_CANCELED:
                    break;

                case AutoResolveHelper.RESULT_ERROR:
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    handleError(status);
                    break;
            }
        }
        googlePayButton.setClickable(true);
    }

    private void requestPayment(View view) {
        // Button deaktivieren damit es nicht doubl geklickt wird:
        this.googlePayButton.setClickable(false);

        // TODO Hole die eingegebne Daten aus den Feldern (Preis, ...)
        PaymentDataRequest request = this.paymentUnit.loadPaymentData((long) 45.5);

        if (request != null ) {
            // Da loadPaymentData eine UI anzeigen muss, die den Benutzer auffordert, eine Zahlungsmethode auszuwählen,
            // wird AutoResolveHelper verwendet, der eine UI anzuzeigt und darauf wartet, bis der Benutzer damit interagiert und GPay API ein Ergebniss dafür zurückgibt.
            // AutoResolveHelper pipet schließlich das Ergebnis zu onActivityResult.
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request),this,LOAD_PAYMENT_DATA_REQUEST_CODE);
        } else {
            System.out.println("PaymentDataRequest ist null");
        }
    }

    public void isReadyToPay() {
        IsReadyToPayRequest readyToPayRequest = IsReadyToPayRequest.fromJson(GPaymentsHelper.getIsReadyToPayRequest().toString());
        // Der Aufruf von isReadyToPay ist asynchron und gibt eine Task zurück. Wir müssen eine bereitstellen:
        Task<Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
        // OnCompleteListener wird ausgelöst, wenn das Ergebnis des Aufrufs bekannt ist:
        task.addOnCompleteListener(this, completeTask -> {
            if(completeTask.isSuccessful()) {
                googlePayButton.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(CheckOutActivity.this, "payment with Google-Pay is not allowed on this device", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePaymentSuccess(PaymentData paymentData) {

        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");

            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    private void handleError(Status status) {

        Log.e("loadPaymentData failed", String.format("Error code: %d", status.getStatusCode()));
        //System.out.println("loadPaymentData failed\nError Status: "+status);
    }
}