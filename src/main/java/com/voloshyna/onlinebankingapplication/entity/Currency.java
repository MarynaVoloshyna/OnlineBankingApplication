package com.voloshyna.onlinebankingapplication.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


public enum Currency {
    UAH, USD, EUR;

}
