package com.genie3.eventsLocation.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserPasswordUpdate
{

    private String id;


    @NotNull(message = "Password field is not provide")
    @NotBlank(message = "password must not be blank")
    @Size(min = 6)
    @JsonIgnore
    private String actualPassword;

    @NotNull(message = "Password field is not provide")
    @NotBlank(message = "password must not be blank")
    @Size(min = 6)
    @JsonIgnore
    private String newPassword;

    @NotNull(message = "Password confirmation field is not provide")
    @NotBlank(message = "password confirmation must not be blank")
    @JsonIgnore
    private String passwordConfirmation;

    @JsonCreator
    public UserPasswordUpdate(
                              @JsonProperty("actual_password") String actualPassword,
                              @JsonProperty("new_password") String newPassword,
                              @JsonProperty("confirm") String confirm){

        this.actualPassword=actualPassword;
        this.newPassword = newPassword;
        this.passwordConfirmation = confirm;


    }

    public UserPasswordUpdate(){

    }

    @JsonIgnore
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getActualPassword() {
        return actualPassword;
    }

    public void setActualPassword(String actual_password) {
        this.actualPassword = actual_password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String new_password) {
        this.newPassword = new_password;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
