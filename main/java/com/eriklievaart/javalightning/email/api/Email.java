package com.eriklievaart.javalightning.email.api;

import com.eriklievaart.javalightning.bundle.api.MultiPartParameter;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class Email {

	private String body;
	private String subject;
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private MultiPartParameter attachment;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public void setAttachment(MultiPartParameter attachment) {
		this.attachment = attachment;
	}

	public MultiPartParameter getAttachment() {
		return attachment;
	}

	@Override
	public String toString() {
		return Str.sub("from:$\nto:$\ncc:$\nbcc:$\nsubject:$\nbody:\n$", from, to, cc, bcc, subject, body);
	}
}