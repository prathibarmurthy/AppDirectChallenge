package main.java.appdirect.interview.appdirectchallenge.service;

import main.java.appdirect.interview.appdirectchallenge.domain.CreatorInfo;
import main.java.appdirect.interview.appdirectchallenge.domain.ErrorResponse;
import main.java.appdirect.interview.appdirectchallenge.domain.Notification;
import main.java.appdirect.interview.appdirectchallenge.domain.Response;
import main.java.appdirect.interview.appdirectchallenge.domain.SuccessResponse;
import main.java.appdirect.interview.appdirectchallenge.domain.UserAccountInfo;
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
@RequestMapping("user/assignment/notification")
public class EventNotificationRequestService {

	private final Logger logger = LoggerFactory
			.getLogger(EventNotificationRequestService.class);

	private final UserAccountRepo userAccountRepo;

	private final OAuthClient oAuthClient;

	@Autowired
	public EventNotificationRequestService(
			UserAccountRepo userAccountRepository, OAuthClient oAuthClient) {
		this.userAccountRepo = userAccountRepository;
		this.oAuthClient = oAuthClient;
	}

	@RequestMapping("assign")
	public ResponseEntity<Response> assign(@RequestParam("url") String url) {
		try {
			final Notification notificationObj = oAuthClient.getNotification(
					url, Notification.Type.USER_ASSIGNMENT);

			if (Notification.EventFlag.STATELESS.equals(notificationObj.flag)) {
				return new ResponseEntity<>(new SuccessResponse(),
						HttpStatus.OK);
			}

			final CreatorInfo creator = notificationObj.payload.user;

			final Long subscId = Long
					.valueOf(notificationObj.payload.account.accountIdentifier);

			if (userAccountRepo.readUsingOpenId(creator.openId)
					.isPresent()) {
				return new ResponseEntity<>(new ErrorResponse(
						ErrorResponse.ErrorCode.USER_ALREADY_EXISTS, ""),
						HttpStatus.CONFLICT);
			}

			userAccountRepo
					.createUserAccount(new UserAccountInfo.Builder()
							.openId(creator.openId)
							.name(creator.firstName, creator.lastName)
							.email(creator.email).subscriptionId(subscId)
							.build());

			return new ResponseEntity<>(
					new SuccessResponse(subscId.toString()), HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Exception thrown", e);
			return new ResponseEntity<>(new ErrorResponse(
					ErrorResponse.ErrorCode.UNKNOWN_ERROR, String.format(
							"Exception thrown %s", e.getMessage())),
					HttpStatus.OK);
		}
	}

	@RequestMapping("unassign")
	public ResponseEntity<Response> unassign(@RequestParam("url") String url) {
		try {
			final Notification notificationObj = oAuthClient.getNotification(url,
					Notification.Type.USER_UNASSIGNMENT);
			final CreatorInfo creator = notificationObj.payload.user;

			if (Notification.EventFlag.STATELESS.equals(notificationObj.flag)) {
				return new ResponseEntity<>(new SuccessResponse(),
						HttpStatus.OK);
			}

			final Long subscId = Long
					.valueOf(notificationObj.payload.account.accountIdentifier);

			if (userAccountRepo.deleteUsingOpenId(creator.openId)) {
				return new ResponseEntity<>(new SuccessResponse(
						subscId.toString()), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ErrorResponse(
						ErrorResponse.ErrorCode.USER_NOT_FOUND, ""),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception thrown", e);
			return new ResponseEntity<>(new ErrorResponse(
					ErrorResponse.ErrorCode.UNKNOWN_ERROR, String.format(
							"Exception thrown %s", e.getMessage())),
					HttpStatus.OK);
		}
	}

}