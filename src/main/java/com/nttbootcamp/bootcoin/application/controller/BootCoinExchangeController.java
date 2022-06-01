package com.nttbootcamp.bootcoin.application.controller;

import com.nttbootcamp.bootcoin.domain.beans.BootCoinExchangeDTO;
import com.nttbootcamp.bootcoin.domain.model.BootCoinExchange;
import com.nttbootcamp.bootcoin.infraestructure.interfaces.IBootCoinExchangeService;
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
@RequestMapping("/BootCoin/Entities/BootCoinExchange")
public class BootCoinExchangeController {
    @Autowired
    private IBootCoinExchangeService service;
    @GetMapping("/{id}")
    public Mono<BootCoinExchange> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> create(@Valid @RequestBody Mono<BootCoinExchangeDTO> request) {
        Map<String, Object> response = new HashMap<>();


        return request.flatMap(a -> {
            return service.save(a).map(c -> {
                response.put("Bootcoin", c);
                response.put("mensaje", "Succesfull Bootcoin Created");
                return ResponseEntity
                        .created(URI.create("/api/Bootcoin/".concat(c.getId())))
                        .contentType(MediaType.APPLICATION_JSON).body(response);
            });
        });
    }
}
