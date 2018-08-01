package com.linecorp.spoon.dto;

import lombok.Data;

@Data
public class PokemonDto {

    private String pokemonName;
    private String pictureUrl;

    public PokemonDto(String pokemonName, String pictureUrl) {
        this.pokemonName = pokemonName;
        this.pictureUrl = pictureUrl;
    }
}
