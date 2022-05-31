package com.eriklievaart.jl.email.api;

public interface EmailService {

	public void setFallbackReplyToAddress(String email);

	public void send(Email email);
}