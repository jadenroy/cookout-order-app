package com.techelevator.services;

import com.techelevator.model.Authority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestAuthorityService implements AuthorityService {

    private RestClient restClient = RestClient.create("https://teapi.netlify.app/api");

    @Override
    public Authority getAuthority() {
        return restClient.get().uri("/authority").retrieve().body(Authority.class);
    }
}

