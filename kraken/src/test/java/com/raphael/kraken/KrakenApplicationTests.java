package com.raphael.kraken;

import com.raphael.kraken.outage.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class KrakenApplicationTests {

	@Mock
	private RestTemplate restTemplate;

	private KrakenService krakenService;

	private final String siteId = "norwich-pear-tree";
	private final String outageUrl = "https://api.krakenflex.systems/interview-tests-mock-api/v1/outages";
	private final String siteInfoUrl = "https://api.krakenflex.systems/interview-tests-mock-api/v1/site-info/" + siteId;
	private final String apiKey = "EltgJ5G8m44IzwE6UN2Y4B4NjPW77Zk6FJK3lL23";

	@Mock
	private Logger loggerMock;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		loggerMock = mock(Logger.class);
		krakenService = new KrakenService(restTemplate);
	}

	@Test
	void testGetAllOutages() {

		// Given
		Outage outage1 = new Outage("1", "2022-01-01T00:00:00.000Z", "2022-01-01T02:00:00.000Z");
		Outage outage2 = new Outage("2", "2022-01-02T00:00:00.000Z", "2022-01-02T02:00:00.000Z");
		List<Outage> outages = Arrays.asList(outage1, outage2); // outages to mock

		when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
				.thenReturn(new ResponseEntity<>(outages, HttpStatus.OK));


		// When
		List<Outage> result = krakenService.getAllOutages();


		// Then
		assertEquals(2, result.size());
		assertEquals(outage1, result.get(0));
		assertEquals(outage2, result.get(1));


		// Verify exchange() method of restTemplate is called with expected Parameters
		HttpHeaders expectedHeaders = new HttpHeaders();
		expectedHeaders.set("x-api-key", apiKey);
		HttpEntity<?> expectedEntity = new HttpEntity<>(expectedHeaders);
		verify(restTemplate).exchange(eq(outageUrl), eq(HttpMethod.GET), eq(expectedEntity), any(ParameterizedTypeReference.class));
	}


	@Test
	void testGetAllOutagesReturnsEmptyList() {

		// Given
		List<Outage> outages = Collections.emptyList();

		when(restTemplate.exchange(any(String.class), any(HttpMethod.class), any(), any(ParameterizedTypeReference.class)))
				.thenReturn(new ResponseEntity<>(outages, HttpStatus.OK));

		// When
		List<Outage> result = krakenService.getAllOutages();

		// Then
		assertTrue(result.isEmpty());
	}


	@Test
	void testGetSite() {

		// Given
		Site expectedSite = new Site(siteId, "Battery 1", new ArrayList<>());
		when(restTemplate.exchange(eq(siteInfoUrl), eq(HttpMethod.GET), any(), eq(Site.class)))
				.thenReturn(new ResponseEntity<>(expectedSite, HttpStatus.OK));

		// When
		krakenService.setSiteId(siteId);
		Site result = krakenService.getSite();

		// Then
		assertEquals(expectedSite, result);

		// Verify that restTemplate.exchange() was called with the expected parameters
		verify(restTemplate).exchange(eq(siteInfoUrl), eq(HttpMethod.GET), any(), eq(Site.class));
	}


	@Test
	public void testGetFilteredOutages() {

		// Given
		krakenService.setSiteId(siteId);

			// Mock site
		Site site = new Site(siteId, "Norwich Pear Tree Site", Collections.singletonList(new Device("12345", "Battery 1")));

		when(restTemplate.exchange(
				eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/site-info/norwich-pear-tree"),
				eq(HttpMethod.GET),
				any(),
				eq(Site.class)))
				.thenReturn(new ResponseEntity<>(site, HttpStatus.OK));


			// Mock outages
		List<Outage> outages = Arrays.asList(
				new Outage("12345", "2020-04-30T20:00:00.000Z", "2022-04-30T21:00:00.000Z"),
				new Outage("12345", "2022-05-01T00:00:00.000Z", "2022-06-01T01:00:00.000Z"));
		when(restTemplate.exchange(
				eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/outages"),
				eq(HttpMethod.GET),
				any(),
				eq(new ParameterizedTypeReference<List<Outage>>() {})
				))
				.thenReturn(new ResponseEntity<>(outages, HttpStatus.OK));


		// When
		List<FilteredOutage> filteredOutages = krakenService.getFilteredOutages();

		// Then
		assertEquals(1, filteredOutages.size());
		assertEquals("Battery 1", filteredOutages.get(0).getName());
		assertEquals("12345", filteredOutages.get(0).getId());
		assertEquals(ZonedDateTime.parse("2022-05-01T00:00:00.000Z"), ZonedDateTime.parse(filteredOutages.get(0).getBegin()));
		assertEquals(ZonedDateTime.parse("2022-06-01T01:00:00.000Z"), ZonedDateTime.parse(filteredOutages.get(0).getEnd()));
	}


	@Test
	public void testPostFilteredOutages() {

		// Given
		krakenService.setSiteId(siteId);

			// Mock site
		Site site = new Site(siteId, "Norwich Pear Tree Site", Collections.singletonList(new Device("12345", "Battery 1")));

		when(restTemplate.exchange(
				eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/site-info/norwich-pear-tree"),
				eq(HttpMethod.GET),
				any(),
				eq(Site.class)))
				.thenReturn(new ResponseEntity<>(site, HttpStatus.OK));


			// Mock outages
		List<Outage> outages = Arrays.asList(
				new Outage("12345", "2020-04-30T20:00:00.000Z", "2022-04-30T21:00:00.000Z"),
				new Outage("12345", "2022-05-01T00:00:00.000Z", "2022-06-01T01:00:00.000Z"));
		when(restTemplate.exchange(
				eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/outages"),
				eq(HttpMethod.GET),
				any(),
				eq(new ParameterizedTypeReference<List<Outage>>() {})
		))
				.thenReturn(new ResponseEntity<>(outages, HttpStatus.OK));


			// Mock the POST request
		String url = "https://api.krakenflex.systems/interview-tests-mock-api/v1/site-outages/" + siteId;
		ResponseEntity<String> responseEntity = new ResponseEntity<>("success", HttpStatus.OK);
		when(restTemplate.exchange(
				eq(url),
				eq(HttpMethod.POST),
				any(HttpEntity.class),
				eq(String.class)))
				.thenReturn(responseEntity);

		// When
		krakenService.postFilteredOutages();

		// Then
		verify(restTemplate).exchange(eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/site-info/norwich-pear-tree"), eq(HttpMethod.GET), any(), eq(Site.class));
		verify(restTemplate).exchange(eq("https://api.krakenflex.systems/interview-tests-mock-api/v1/outages"), eq(HttpMethod.GET), any(), eq(new ParameterizedTypeReference<List<Outage>>() {}));
		verify(restTemplate).exchange(eq(url), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));

		verifyNoMoreInteractions(restTemplate);
	}







}

