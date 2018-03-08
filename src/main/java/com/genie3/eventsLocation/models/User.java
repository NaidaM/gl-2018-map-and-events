package com.genie3.eventsLocation.models;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.mindrot.jbcrypt.BCrypt;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User
{

    private int id;


    @NotNull(message = "Pseudo field is not provide")
    @Size(min = 6,max = 20,message = "Pseudo size must be between 6 and 20 character")
    private String pseudo;

    @Email(message = "please provide a valid email")

    private String email;

    @NotNull(message = "Password field is not provide")
    @NotBlank(message = "password must not be blank")
    @Size(min = 6)
    @JsonIgnore
    private String password;

    @NotNull(message = "Password confirmation field is not provide")
    @NotBlank(message = "password confirmation must not be blank")
    @JsonIgnore
    private String passwordConfirmation;

    @JsonIgnore
    private ArrayList<EventMap> maps;
    @JsonIgnore
    private String token;

    @NotNull(message = "Role  field is not provide")
    private String role;
    
    public static String HashPssd (String password) {
    	return BCrypt.hashpw(password, BCrypt.gensalt(15));
    }
    
    public static boolean checkpassd(String password, String hash) {
    	 return BCrypt.checkpw(password, hash);
    }

    @JsonCreator
    public User( @JsonProperty("pseudo") String pseudo,
                 @JsonProperty("email") String email,
                 @JsonProperty("password") String password ,
                 @JsonProperty("confirm") String confirm){

        this.id = 1;
        this.pseudo=pseudo;
        this.email=email;
        this.password = password;
        this.passwordConfirmation = confirm;
        maps = new ArrayList<EventMap>();
        this.token = "";
        this.role = "anonymous";


    }

    public User(){
        maps = new ArrayList<EventMap>();
        this.token = "";
        this.role = "anonymous";
    }

    @JsonIgnore
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public ArrayList<EventMap> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<EventMap> maps) {
        this.maps = maps;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
