package main.java.appdirect.interview.appdirectchallenge.utils;

import main.java.appdirect.interview.appdirectchallenge.domain.Notification;
import main.java.appdirect.interview.appdirectchallenge.domain.Notification.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth.consumer.ProtectedResourceDetails;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.stereotype.Component;

@Component
public class OAuthClient {

	@Autowired
	private ProtectedResourceDetails protectedResourceDetails;

	public Notification getNotification(String url, Type type) {
		final OAuthRestTemplate oAuthRestTemplate = new OAuthRestTemplate(
				protectedResourceDetails);
		final Notification notificationObj = oAuthRestTemplate.getForObject(url,
				Notification.class);
		if (!type.equals(notificationObj.type)) {
			throw new InvalidNotificationTypeException(type, notificationObj.type);
		}
		return notificationObj;
	}

}