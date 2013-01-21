package anagrams;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

import examples.NeoAddress;
import examples.FooUsesNeoAddress;
import examples.NeoClient;

public class PopulateLettersShould {

	@Before
	public void setUp() throws Exception {
		Injector injector = Guice.createInjector(new FooUsesNeoAddress("testAnagrams"));
		//injector.get
	}

	@Test
	public void test() {
		
		fail("Not yet implemented"); // TODO
	}

}
