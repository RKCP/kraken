package com.raphael.kraken.outage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class KrakenService {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers;
    private HttpEntity<?> entity;

    private String siteId;

    private final String dateString = "2022-01-01T00:00:00.000Z";
    private final ZonedDateTime dateToFilter = ZonedDateTime.parse(dateString);

    private static final Logger logger = LoggerFactory.getLogger(KrakenService.class);

    // constructor for mocks/tests
    public KrakenService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public List<Outage> getAllOutages() {


        String url = "https://api.krakenflex.systems/interview-tests-mock-api/v1/outages";

        headers = new HttpHeaders();
        headers.set("x-api-key", "EltgJ5G8m44IzwE6UN2Y4B4NjPW77Zk6FJK3lL23");

        entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<List<Outage>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Outage>>() {}
            );
            List<Outage> outages = responseEntity.getBody();
            logger.info("Outages list: " + outages);
            return outages;
        } catch (RestClientException e) {
            logger.error("Error occurred while getting all outages: {}", e.getMessage());
            return new ArrayList<>();
        }

    }


    public Site getSite() {

        String url = "https://api.krakenflex.systems/interview-tests-mock-api/v1/site-info/" + siteId;

        headers = new HttpHeaders();
        headers.set("x-api-key", "EltgJ5G8m44IzwE6UN2Y4B4NjPW77Zk6FJK3lL23");

        entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Site> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Site.class
            );
            Site site = responseEntity.getBody();
            logger.info("Received site: " + site);
            return site;
        } catch (RestClientException e) {
            // log the error and return null or throw a custom exception
            logger.error("Error getting site information from Kraken API", e);
            e.printStackTrace();
            return null;
        }
    }


    public List<FilteredOutage> getFilteredOutages() {

        try {

            // Get site
            Site site = getSite();

            // Get Devices from site
            List<Device> devices = site.getDevices();

            List<Outage> allOutages = getAllOutages();
            List<FilteredOutage> filteredOutages = new ArrayList<>();

            for (Device device : devices) {

                for (Outage outage : allOutages) {

                    String outageBeginDate = outage.getBegin();
                    ZonedDateTime outageZonedDate = ZonedDateTime.parse(outageBeginDate);

                    if (outage.getId().equals(device.getId())) {
                        if (outageZonedDate.isAfter(dateToFilter) || outageZonedDate.isEqual(dateToFilter)) {
                            FilteredOutage filteredOutage = new FilteredOutage(
                                    outage.getId(),
                                    device.getName(),
                                    outage.getBegin(),
                                    outage.getEnd()
                            );
                            filteredOutages.add(filteredOutage);
                        }
                    }
                }

            }
            logger.info("Filtered Outages: " + filteredOutages);
            return filteredOutages;
        } catch (RestClientException e) {
            logger.error("Error while retrieving devices or outages: {}", e.getMessage());
            return null;
        }
    }


    public void postFilteredOutages() {

        List<FilteredOutage> outagesToPost = getFilteredOutages();

        String url = "https://api.krakenflex.systems/interview-tests-mock-api/v1/site-outages/" + siteId;

        headers = new HttpHeaders();
        headers.set("x-api-key", "EltgJ5G8m44IzwE6UN2Y4B4NjPW77Zk6FJK3lL23");
        headers.setContentType(MediaType.APPLICATION_JSON);

        entity = new HttpEntity<>(outagesToPost, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            HttpStatusCode response = responseEntity.getStatusCode();
            logger.info("Response from server: {}", response);
        } catch (RestClientException e) { // inclusive of HTTP 5XX errors.
            logger.error("Error occurred while sending POST request: {}", e.getMessage());
        }
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
