package com.barrenlandanalysis.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.barrenlandanalysis.constants.Constants;

public class BarrenLandServiceImplTest {

	private BarrenLandServiceImpl service;

	@Before
	public void setUp() throws Exception {
		this.service = new BarrenLandServiceImpl();
	}

	@After
	public void afterTest() throws Exception {
		this.service = null;
	}
	

	@Test
	public void testFindFertileLand() {
		String[] barrenLandCoordinates = {"0 292 399 307"};
		
		String fertileAreasString = service.findFertileLand(barrenLandCoordinates);
		
		String[] fertileAreas = fertileAreasString.split(Constants.DELIMITER);
		
		assertNotNull(fertileAreas);
		assertEquals(2,fertileAreas.length);
		assertEquals("116800",fertileAreas[0]);
		assertEquals("116800",fertileAreas[1]);
		
		barrenLandCoordinates = new String[]{"48 192 351 207","48 392 351 407", "120 52 135 547", "260 52 275 547"};
		
		
		fertileAreasString = service.findFertileLand(barrenLandCoordinates);
		fertileAreas = fertileAreasString.split(Constants.DELIMITER);
		
		assertNotNull(fertileAreas);
		assertEquals(2,fertileAreas.length);
		
		assertEquals("22816",fertileAreas[0]);
		assertEquals("192608",fertileAreas[1]);
	}
	
}
