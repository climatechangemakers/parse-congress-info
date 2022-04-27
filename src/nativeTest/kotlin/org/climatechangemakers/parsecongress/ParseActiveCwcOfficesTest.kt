package org.climatechangemakers.parsecongress

import kotlin.test.Test
import kotlin.test.assertEquals

class ParseActiveCwcOfficesTest {

  @Test fun `active office parses bioguide correctly`() {
    val activeOffice = ActiveOffice(
      name = "Alex Padilla [bioguide_id: P000145]",
      officeCode = "SCA03",
    )

    assertEquals(expected = "P000145", actual = activeOffice.bioguide)
  }
}