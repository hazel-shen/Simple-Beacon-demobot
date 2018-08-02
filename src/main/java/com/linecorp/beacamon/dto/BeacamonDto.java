package com.linecorp.beacamon.dto;

import lombok.Data;

@Data
public class BeacamonDto {

    private String beacamonName;
    private String pictureUrl;

    public BeacamonDto(String beacamonName, String pictureUrl) {
        this.beacamonName = beacamonName;
        this.pictureUrl = pictureUrl;
    }
}
