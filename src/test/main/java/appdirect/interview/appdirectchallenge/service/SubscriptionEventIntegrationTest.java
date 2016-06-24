package test.main.java.appdirect.interview.appdirectchallenge.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.MalformedURLException;
import java.net.URL;

import main.java.appdirect.interview.appdirectchallenge.AppDirectApp;
import main.java.appdirect.interview.appdirectchallenge.domain.Subscription;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppDirectApp.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class SubscriptionEventIntegrationTest {

    @Value("${local.server.port}")
    private int serverPort;

    private URL url;
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws MalformedURLException {
        this.url = new URL("http://localhost:" + serverPort + "/api");
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void subscriptions_event_test() {
        Subscription[] object = restTemplate.getForObject(url + "/subscriptions", Subscription[].class);
        assertThat(object[0].companyName, equalTo("Gods Algorithm inc"));
        assertThat(object[0].edition, equalTo("FREE"));
        assertThat(object[0].status, equalTo("ACTIVE"));
        assertThat(object[0].marketPlaceBaseUrl, equalTo("https://godsalgorithm.org"));
        assertThat(object[0].users.get(0).openId, equalTo("https://appdirectchallenge/openid/id/211aa367-f53b-4606-8887-80a381e0ef69"));
        assertThat(object[0].users.get(0).email, equalTo("prathiba.r@gmail.com"));
        assertThat(object[0].users.get(0).firstname, equalTo("Prathiba"));
        assertThat(object[0].users.get(0).lastname, equalTo("Ramachandramurthy"));
    }

}