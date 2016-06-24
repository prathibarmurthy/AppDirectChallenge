package main.java.appdirect.interview.appdirectchallenge.service;

import main.java.appdirect.interview.appdirectchallenge.domain.AccountInfo;
import main.java.appdirect.interview.appdirectchallenge.domain.CreatorInfo;
import main.java.appdirect.interview.appdirectchallenge.domain.CompanyInfo;
import main.java.appdirect.interview.appdirectchallenge.domain.ErrorResponse;
import main.java.appdirect.interview.appdirectchallenge.domain.Notification;
import main.java.appdirect.interview.appdirectchallenge.domain.SubscriptionOrder;
import main.java.appdirect.interview.appdirectchallenge.domain.Response;
import main.java.appdirect.interview.appdirectchallenge.domain.Subscription;
import main.java.appdirect.interview.appdirectchallenge.domain.SuccessResponse;
import main.java.appdirect.interview.appdirectchallenge.domain.UserAccountInfo;
import main.java.appdirect.interview.appdirectchallenge.domain.ErrorResponse.ErrorCode;
import main.java.appdirect.interview.appdirectchallenge.domain.Notification.EventFlag;
import main.java.appdirect.interview.appdirectchallenge.repository.SubscriptionRepo;
import main.java.appdirect.interview.appdirectchallenge.repository.UserAccountRepo;
import main.java.appdirect.interview.appdirectchallenge.utils.OAuthClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subscription/notification")
public class SubscriptionNotificationService {

    private final Logger logger = LoggerFactory.getLogger(SubscriptionNotificationService.class);
    private final SubscriptionRepo subscriptionRepository;
    private final UserAccountRepo userAccountRepository;
    private final OAuthClient oAuthClient;


    @Autowired
    public SubscriptionNotificationService(SubscriptionRepo subscriptionRepository, UserAccountRepo userAccountRepository, OAuthClient oAuthClient) {
        this.subscriptionRepository = subscriptionRepository;
        this.userAccountRepository = userAccountRepository;
        this.oAuthClient = oAuthClient;
    }

    @RequestMapping("create")
    public ResponseEntity<Response> create(@RequestParam("url") String url) {
        try {
            final Notification notification = oAuthClient.getNotification(url, Notification.Type.SUBSCRIPTION_ORDER);

            final CreatorInfo creator = notification.creator;
            final CompanyInfo company = notification.payload.company;
            final SubscriptionOrder order = notification.payload.order;

            if (EventFlag.STATELESS.equals(notification.flag)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            }

            if (userAccountRepository.readUsingOpenId(creator.openId).isPresent()) {
                return new ResponseEntity<>(new ErrorResponse(ErrorCode.USER_ALREADY_EXISTS, ""), HttpStatus.CONFLICT);
            }

            final Long subscriptionId = subscriptionRepository.createSubscription(new Subscription.Builder().companyName(company.name).edition(order.editionCode).marketPlaceBaseUrl(notification.marketplace.baseUrl).build());
            userAccountRepository.createUserAccount(new UserAccountInfo.Builder().openId(creator.openId).name(creator.firstName, creator.lastName).email(creator.email).subscriptionId(subscriptionId).build());

            return new ResponseEntity<>(new SuccessResponse(subscriptionId.toString()), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Exception thrown", e);
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.UNKNOWN_ERROR, String.format("Exception thrown %s", e.getMessage())), HttpStatus.OK);
        }
    }

    @RequestMapping("change")
    public ResponseEntity<Response> change(@RequestParam("url") String url) {
        try {
            final Notification notification = oAuthClient.getNotification(url, Notification.Type.SUBSCRIPTION_CHANGE);
            final AccountInfo account = notification.payload.account;
            final SubscriptionOrder order = notification.payload.order;

            if (EventFlag.STATELESS.equals(notification.flag)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            }

            final Long subscriptionId = Long.valueOf(account.accountIdentifier);

            if (subscriptionRepository.update(subscriptionId, order.editionCode)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse(ErrorCode.ACCOUNT_NOT_FOUND, String.format("The account %s could not be found.", account.accountIdentifier)), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Exception thrown", e);
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.UNKNOWN_ERROR, String.format("Exception thrown %s", e.getMessage())), HttpStatus.OK);
        }
    }

    @RequestMapping("status")
    public ResponseEntity<Response> status(@RequestParam("url") String url) {
        try {
            final Notification notification = oAuthClient.getNotification(url, Notification.Type.SUBSCRIPTION_NOTICE);
            final AccountInfo account = notification.payload.account;

            if (EventFlag.STATELESS.equals(notification.flag)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            }

            final Long subscriptionId = Long.valueOf(account.accountIdentifier);

            if (subscriptionRepository.updateStatus(subscriptionId, account.status)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse(ErrorCode.ACCOUNT_NOT_FOUND, String.format("The account %s could not be found.", account.accountIdentifier)), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Exception thrown", e);
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.UNKNOWN_ERROR, String.format("Exception thrown %s", e.getMessage())), HttpStatus.OK);
        }
    }

    @RequestMapping("cancel")
    public ResponseEntity<Response> cancel(@RequestParam("url") String url) {
        try {
            final Notification notification = oAuthClient.getNotification(url, Notification.Type.SUBSCRIPTION_CANCEL);

            if (EventFlag.STATELESS.equals(notification.flag)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            }

            final Long subscriptionId = Long.valueOf(notification.payload.account.accountIdentifier);

            userAccountRepository.deleteUsingSubscriptionId(subscriptionId);
            if (subscriptionRepository.deleteSubscription(subscriptionId)) {
                return new ResponseEntity<>(new SuccessResponse(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorResponse(ErrorCode.ACCOUNT_NOT_FOUND, "The account " + subscriptionId + " could not be found."), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Exception thrown", e);
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.UNKNOWN_ERROR, String.format("Exception thrown %s", e.getMessage())), HttpStatus.OK);
        }
    }

}