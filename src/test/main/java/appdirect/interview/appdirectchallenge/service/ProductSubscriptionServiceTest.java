package test.main.java.appdirect.interview.appdirectchallenge.service;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;

import main.java.appdirect.interview.appdirectchallenge.domain.Subscription;
import main.java.appdirect.interview.appdirectchallenge.domain.UserAccountInfo;
import main.java.appdirect.interview.appdirectchallenge.repository.SubscriptionRepo;
import main.java.appdirect.interview.appdirectchallenge.repository.UserAccountRepo;
import main.java.appdirect.interview.appdirectchallenge.service.ProductSubscriptionService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProductSubscriptionServiceTest {

	private ProductSubscriptionService productSubscriptionService;

	@Mock
	private SubscriptionRepo subscriptionRepo;

	@Mock
	private UserAccountRepo userAccountRepo;

	@Before
	public void setUp() {

		productSubscriptionService = new ProductSubscriptionService(
				subscriptionRepo, userAccountRepo);
	}

	@Test
	public void productSubscriptionService_list_test() {
		UserAccountInfo userAccountInfoObj = new UserAccountInfo.Builder()
				.id(1L).openId("1248-15861-358135")
				.name("Prathiba", "Ramachandramurthy")
				.email("prathiba.r@gmail.com").subscriptionId(1L).build();
		Subscription subscriptionObj = new Subscription.Builder().id(1L)
				.companyName("God's Algorithm").edition("RECURRING")
				.status("ACTIVE").marketPlaceBaseUrl("https://example.org/")
				.users(Collections.singletonList(userAccountInfoObj)).build();
		when(userAccountRepo.list(1L)).thenReturn(
				Collections.singletonList(userAccountInfoObj));
		when(subscriptionRepo.listSubscriptions()).thenReturn(
				Collections.singletonList(subscriptionObj));

		assertThat(productSubscriptionService.list(), contains(subscriptionObj));
	}

}