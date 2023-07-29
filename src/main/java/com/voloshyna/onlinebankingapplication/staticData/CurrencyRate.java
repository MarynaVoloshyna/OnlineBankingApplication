package com.voloshyna.onlinebankingapplication.staticData;

import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;

import static com.voloshyna.onlinebankingapplication.staticData.CurrencyAPI.getJSON;


@Entity

public  final  class CurrencyRate {
    private final static Double USD=40.0;
    private final static Double EUR=42.0;
    private final static Double CROSS_RATE = 1.006;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    public CurrencyRate() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public static Double getUSD() {
        return USD;
    }

    public static Double getEUR() {
        return EUR;
    }
    public static Double getCrossRate() {
        return CROSS_RATE;
    }

    @Override
    public String toString() {
        return "CurrencyRate{" +
                "USD=" + USD +
                ", EUR=" + EUR +
                '}';
    }

}
