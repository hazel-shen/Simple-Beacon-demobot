package com.linecorp.beacamon.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto {

    @JsonProperty("pokeName")
    private String pokeName;

    @JsonProperty("pokeLevel")
    private Integer pokeLevel;

    public UserInfoDto (@JsonProperty("pokeName") String pokeName,
                        @JsonProperty("pokeLevel") Integer pokeLevel) {
        this.pokeName = pokeName;
        this.pokeLevel = pokeLevel;
    }
}
