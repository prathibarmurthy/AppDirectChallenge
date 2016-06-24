package main.java.appdirect.interview.appdirectchallenge.service;

import java.util.List;
import java.util.stream.Collectors;

import main.java.appdirect.interview.appdirectchallenge.domain.Subscription;
import main.java.appdirect.interview.appdirectchallenge.repository.SubscriptionRepo;
import main.java.appdirect.interview.appdirectchallenge.repository.UserAccountRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class ProductSubscriptionService {

    private final SubscriptionRepo subscriptionRepository;

    private final UserAccountRepo userAccountRepository;

    @Autowired
    public ProductSubscriptionService(SubscriptionRepo subscriptionRepository, UserAccountRepo userAccountRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @RequestMapping("subscriptions")
    public List<Subscription> list() {
        final List<Subscription> subscriptions = subscriptionRepository.listSubscriptions();
        return subscriptions.stream().map(s -> new Subscription.Builder(s)
                .users(userAccountRepository.list(s.id))
                .build()).collect(Collectors.toList());
    }

}