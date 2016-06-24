package main.java.appdirect.interview.appdirectchallenge.utils;

import main.java.appdirect.interview.appdirectchallenge.domain.Notification.Type;

public class InvalidNotificationTypeException extends RuntimeException {

	public InvalidNotificationTypeException(Type expectedType, Type actualType) {
		super(String.format("Bad Notification type. Expected %s, actual %s",
				expectedType, actualType));
	}

}