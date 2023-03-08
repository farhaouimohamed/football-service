package fr.st.fs.payload;

import org.springframework.http.HttpStatus;

public class ApiResponse {
    private Boolean response;
    private String message;
    private HttpStatus statut;

    public ApiResponse(Boolean response, String message, HttpStatus statut) {
        super();
        this.response = response;
        this.message = message;
        this.statut = statut;
    }
    public Boolean getResponse() {
        return response;
    }
    public void setResponse(Boolean response) {
        this.response = response;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public HttpStatus getStatut() {
        return statut;
    }
    public void setStatut(HttpStatus statut) {
        this.statut = statut;
    }
}
