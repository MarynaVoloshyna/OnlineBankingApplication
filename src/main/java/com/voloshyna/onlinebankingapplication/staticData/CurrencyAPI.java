package com.voloshyna.onlinebankingapplication.staticData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public  class CurrencyAPI {
    private int currencyCodeA;
    private int currencyCodeB;
    private int date;
    private double rateSell;
    private double  rateBuy;
    private double  rateCross;
    @Id
    private Long id;

    public static String getJSON() throws IOException {
        String json = "";
        URL url = new URL("https://api.monobank.ua/bank/currency");
        URLConnection urlc = (HttpURLConnection) url.openConnection();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlc.getInputStream()))) {
            json = bufferedReader.readLine();
        } catch (IOException e ) {
            e.printStackTrace();
        }

        return json;
    }

    public static double getExhangeRateUSD() throws IOException {
        double rateUSD = 0.0;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CurrencyAPI [] currency = gson.fromJson(getJSON(), CurrencyAPI[].class);
        for (CurrencyAPI currencies : currency) {
            if(currencies.getCurrencyCodeA() == 840 && currencies.getCurrencyCodeB() == 980) {
                rateUSD = currencies.getRateBuy();
            }
        }
        return rateUSD;
    }
    public static double getExhangeRateEUR() throws IOException {
        double rateEUR = 0.0;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CurrencyAPI [] currency = gson.fromJson(getJSON(), CurrencyAPI[].class);
        for (CurrencyAPI currencies : currency) {
            if(currencies.getCurrencyCodeA() == 978 && currencies.getCurrencyCodeB() == 980) {
                rateEUR = currencies.getRateBuy();
            }
        }
        return rateEUR;
    }
    public static double getAPICrossRate() throws IOException {
        double crossRate = 0.0;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CurrencyAPI [] currency = gson.fromJson(getJSON(), CurrencyAPI[].class);
        for (CurrencyAPI currencies : currency) {
            if(currencies.getCurrencyCodeA() == 978 && currencies.getCurrencyCodeB() == 980) {
                crossRate = currencies.getRateBuy();
            }
        }
        return crossRate;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
