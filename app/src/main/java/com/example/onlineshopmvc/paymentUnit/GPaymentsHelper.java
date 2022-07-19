package com.example.onlineshopmvc.paymentUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class GPaymentsHelper {

    public static final BigDecimal CENTS_IN_A_UNIT = new BigDecimal(100);
    public static final HashMap<String, String> DIRECT_TOKENIZATION_PARAMETERS = new HashMap<String, String>() {{
            put("protocolVersion", "ECv2");put("publicKey",null);
        }};


    // Google Pay API-Version definieren:
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
    }

    // Zahlungstoken für den Zahlungsanbieter anfodern:
    // TODO: Check with your gateway on the parameters to pass and modify them in Constants.java.
    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {{
                put("gateway", "example");
                put("gatewayMerchantId", "exampleGatewayMerchantId");
            }});
        }};
    }

    private static JSONObject getDirectTokenizationSpecification() throws JSONException, RuntimeException {
        JSONObject tokenizationSpecification = new JSONObject();

        tokenizationSpecification.put("type", "DIRECT");
        JSONObject parameters = new JSONObject(DIRECT_TOKENIZATION_PARAMETERS);
        tokenizationSpecification.put("parameters", parameters);

        return tokenizationSpecification;
    }


    // Kartennetzwerke, die die Anwendung akzeptiert, definieren:
    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray().put("VISA").put("MASTERCARD");
    }


    // Authentifizierungsmethode definieren:
    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS");
    }


    // Die erlaubten Karten mit der Auth. Methoden werden in einem JSOM Obj. komboniert,
    // um die erlaubten Zahlungsmethoden zu bescreiben:
    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
        parameters.put("allowedCardNetworks", getAllowedCardNetworks());

        // Optionally, you can add billing address/phone number associated with a CARD payment method.
        //parameters.put("billingAddressRequired", true);

        //JSONObject billingAddressParameters = new JSONObject();
        //billingAddressParameters.put("format", "FULL");

        //parameters.put("billingAddressParameters", billingAddressParameters);


        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }


    // Erwaeiterung für die Karte der getBaseCardPaymentMethod() um Infos zu beschreiben, die tokenisierte Zahlungsdaten enthalten müssen
    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    // JSON-Objekt zur Ermitllung der Zahlungsbereitschaft mit der GPay-API:
    public static JSONObject getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));

            return isReadyToPayRequest;

        } catch (JSONException e) {
            return null;
        }
    }

    // Beschreibung der Informationen, die von einem Zahler in einem Google Pay-Zahlungsbogen angefordert werden:
    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", "D");
        transactionInfo.put("currencyCode", "Euro");
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

        return transactionInfo;
    }

    // Informationen über den Händler, der Zahlungsinformationen anfordert:
    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject().put("merchantName", "Example Merchant");
    }

    // An object describing information requested in a Google Pay payment sheet
    public static JSONObject getPaymentDataRequest(long priceCents) {

        final String price = GPaymentsHelper.centsToString(priceCents);

        try {
            JSONObject paymentDataRequest = GPaymentsHelper.getBaseRequest();
            paymentDataRequest.put("allowedPaymentMethods", new JSONArray().put(GPaymentsHelper.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", GPaymentsHelper.getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", GPaymentsHelper.getMerchantInfo());

            /*
            //An optional shipping address requirement is a top-level property of the PaymentDataRequest
            // JSON object.
            paymentDataRequest.put("shippingAddressRequired", true);

            JSONObject shippingAddressParameters = new JSONObject();
            shippingAddressParameters.put("phoneNumberRequired", false);

            JSONArray allowedCountryCodes = new JSONArray(Constants.SHIPPING_SUPPORTED_COUNTRIES);

            shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
             */
            return paymentDataRequest;

        } catch (JSONException e) {
            return null;
        }
    }

    public static String centsToString(long cents) {
        return new BigDecimal(cents)
                .divide(CENTS_IN_A_UNIT, RoundingMode.HALF_EVEN)
                .setScale(2, RoundingMode.HALF_EVEN)
                .toString();
    }
}