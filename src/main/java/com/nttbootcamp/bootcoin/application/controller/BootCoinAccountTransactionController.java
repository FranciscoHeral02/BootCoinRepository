package com.nttbootcamp.bootcoin.application.controller;

import com.nttbootcamp.bootcoin.domain.beans.BootCoinOperationDTO;
import com.nttbootcamp.bootcoin.infraestructure.interfaces.IBootCoinAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/BootCoinMobile/Actions/BootCoinOperations")
public class BootCoinAccountTransactionController {
    @Autowired
    private IBootCoinAccountTransactionService service;
    @PostMapping("/RequestPayment")
    public Mono<ResponseEntity<Map<String, Object>>> requestPayment(@Valid @RequestBody Mono<BootCoinOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doBootCoinPayment(a).map(c -> {
            response.put("BootCoin Request Operation", c);
            response.put("message", "Successful BootCoin Request Transaction ");
            return ResponseEntity.created(URI.create("/BootCoinMobile/Actions/BootCoinOperations".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }
    @PostMapping("/AcceptPayment")
    public Mono<ResponseEntity<Map<String, Object>>> acceptPayment(@Valid @RequestBody Mono<BootCoinOperationDTO> request) {

        Map<String, Object> response = new HashMap<>();

        return request.flatMap(a -> service.doBootCoinPayment(a).map(c -> {
            response.put("BootCoin Request Operation", c);
            response.put("message", "Successful BootCoin Request Transaction ");
            return ResponseEntity.created(URI.create("/BootCoinMobile/Actions/BootCoinOperations".concat(c.getTransactionId())))
                    .contentType(MediaType.APPLICATION_JSON).body(response);
        }));
    }

}
