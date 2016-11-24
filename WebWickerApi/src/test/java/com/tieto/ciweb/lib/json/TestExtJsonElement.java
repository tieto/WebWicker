package com.tieto.ciweb.lib.json;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tieto.webwicker.lib.json.ExtJsonElement;

public class TestExtJsonElement {

	@Test
	public void testMatch() {
		ExtJsonElement json = new ExtJsonElement("{\"a\":[{\"b\":\"c\"},{\"b\":\"d\"}]}");
		assertTrue(ExtJsonElement.match(json, "a[0].b", "c"));
		assertFalse(ExtJsonElement.match(json, "a[1].b", "c"));
		assertTrue(ExtJsonElement.match(json, "a[*].b", "c"));
		assertFalse(ExtJsonElement.match(json, "c.d", "f"));
	}

}
