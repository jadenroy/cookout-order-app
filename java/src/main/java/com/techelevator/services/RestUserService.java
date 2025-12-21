package com.techelevator.services;

import com.techelevator.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestUserService implements UserService {
    private RestClient restClient = RestClient.create("https://teapi.netlify.app/api");

    @Override
    public User getUser() {
        return restClient.get().uri("/username").retrieve().body(User.class);
    }
}

