package com.genie3.eventsLocation.models;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User
{

    private int id;


    @NotNull(message = "Pseudo field is not provide")
    @Size(min = 6,max = 20)
    private String pseudo;

    @Email(message = "please provide a valid email")

    private String email;

    @NotNull(message = "Password field is not provide")
    @NotBlank(message = "password must not be blank")
    @Size(min = 6)
    private String password;

    @NotNull(message = "Password confirmation field is not provide")
    @NotBlank(message = "password confirmation must not be blank")
    private String passwordConfirmation;

    @JsonCreator
    public User( @JsonProperty("pseudo") String pseudo,
                 @JsonProperty("email") String email,
                 @JsonProperty("password") String password ,
                 @JsonProperty("password_confirm") String passwordConfirmation ){

        this.id = 1;
        this.pseudo=pseudo;
        this.email=email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;


    }

    public User(){

    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
