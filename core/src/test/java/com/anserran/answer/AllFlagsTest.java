package com.anserran.answer;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertTrue;

public class AllFlagsTest {

	@Test
	public void test() throws IOException {
		BufferedReader atlasReader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("flags/flags.atlas")));
		String line;
		String atlasResult = "";
		while ((line = atlasReader.readLine()) != null) {
			atlasResult += line + "\n";
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				ClassLoader.getSystemResourceAsStream("data/geography.csv")));
		while ((line = reader.readLine()) != null) {
			String id = line.split(",")[0];
			assertTrue("Atlas does not contain " + id, atlasResult.contains(id));
		}

	}
}
