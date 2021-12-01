package com.eazybytes.springsecuritybasic.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/myCards")
public class CardsController {

    @GetMapping
    public String getCardsDetails(String input) {
        return "Here are the card details from the DB";
    }
}
