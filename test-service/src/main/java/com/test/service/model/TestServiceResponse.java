package com.test.service.model;

import java.util.List;
import java.util.Map;

public class TestServiceResponse {

	private String source;

	private String output;

	private Map<String, List<String>> contacts;

	public Map<String, List<String>> getContacts() {
		return contacts;
	}

	public void setContacts(Map<String, List<String>> contacts) {
		this.contacts = contacts;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "TestServiceResponse [source=" + source + ", output=" + output + ", contacts=" + contacts + "]";
	}

}
