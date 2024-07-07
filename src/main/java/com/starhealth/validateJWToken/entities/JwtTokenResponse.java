package com.starhealth.validateJWToken.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtTokenResponse {

    @JsonProperty("exp")
    private Long exp;

    @JsonProperty("iat")
    private Long iat;

    @JsonProperty("jti")
    private String jti;

    @JsonProperty("iss")
    private String iss;

    @JsonProperty("aud")
    private Object aud;

    @JsonProperty("sub")
    private String sub;

    @JsonProperty("typ")
    private String typ;

    @JsonProperty("azp")
    private String azp;

    @JsonProperty("session_state")
    private String sessionState;

    @JsonProperty("acr")
    private String acr;

    @JsonProperty("allowed-origins")
    private String[] allowedOrigins;

    @JsonProperty("realm_access")
    private RealmAccess realmAccess;

    Map<String, Object> resource_access;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("name")
    private String name;

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("given_name")
    private String givenName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("hrId")
    private String hrId;
}

