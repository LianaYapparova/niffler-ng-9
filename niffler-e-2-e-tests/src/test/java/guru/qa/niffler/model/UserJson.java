package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.UUID;

public record UserJson(@JsonProperty("id")
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

    public static UserJson fromEntity(UserEntity entity) {

        return new UserJson(
                null,

                entity.getUsername(),
                null,
                entity.getCurrency(),
                entity.getFirstname(),
                entity.getSurname(),
                entity.getPhoto(),
                entity.getPhotoSmall(),
                entity.getFullname()
        );
    }

}
