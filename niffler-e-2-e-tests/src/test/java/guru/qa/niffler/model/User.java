package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record User(@JsonProperty("id")
                   UUID id,
                   @JsonProperty("username")
                   String username,
                   @JsonProperty("password")
                   String password,
                   CurrencyValues currency,

                   String firstname,
                   String surname,

                   byte[] photo,
                   byte[] photo_small,
                   String full_name) {

}
