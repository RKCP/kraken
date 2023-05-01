package com.raphael.kraken.outage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class KrakenController {

    @Autowired
    private RestTemplate restTemplate;

    private final KrakenService krakenService;

    @Autowired
    public KrakenController(KrakenService krakenService) {
        this.krakenService = krakenService;
    }

    @GetMapping("/outages")
    public List<Outage> getAllOutages() {
        List<Outage> outages = krakenService.getAllOutages();
        return outages;
    }

    @GetMapping("/site-info/{siteId}")
    public Site getSite(@PathVariable String siteId) {
        krakenService.setSiteId(siteId);
        Site site = krakenService.getSite();
        return site;
    }

    @PostMapping(
            path = "/site-outages/{siteId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void postFilteredOutages(@PathVariable String siteId) {
        krakenService.setSiteId(siteId);
        krakenService.postFilteredOutages();
    }

}
