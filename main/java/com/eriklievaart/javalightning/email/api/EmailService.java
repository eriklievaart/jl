package com.eriklievaart.javalightning.email.api;

public interface EmailService {

	public void setFallbackReplyToAddress(String email);

	public void send(Email email);
}
