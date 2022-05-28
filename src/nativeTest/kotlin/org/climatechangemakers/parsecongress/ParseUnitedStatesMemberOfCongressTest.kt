package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class ParseUnitedStatesMemberOfCongressTest {

  @Test fun `gets most recent term`() {
    val terms = listOf(
      UnitedStatesTermInfo(
        representativeType = "sen",
        state = "VA",
        party = "Democrat",
        district = null,
        phone = null,
        start = LocalDate(year = 2020, dayOfMonth = 1, monthNumber = 1),
        end = LocalDate(year = 2024, dayOfMonth = 1, monthNumber = 1),
      ),
      UnitedStatesTermInfo(
        representativeType = "sen",
        state = "VA",
        party = "Democrat",
        district = null,
        phone = null,
        start = LocalDate(year = 2016, dayOfMonth = 1, monthNumber = 1),
        end = LocalDate(year = 2020, dayOfMonth = 1, monthNumber = 1),
      ),
      UnitedStatesTermInfo(
        representativeType = "sen",
        state = "VA",
        party = "Democrat",
        district = null,
        phone = null,
        start = LocalDate(year = 2012, dayOfMonth = 1, monthNumber = 1),
        end = LocalDate(year = 2016, dayOfMonth = 1, monthNumber = 1),
      ),
    )

    assertEquals(
      expected = LocalDate(year = 2024, dayOfMonth = 1, monthNumber = 1),
      actual = terms.mostRecent().end,
    )
  }
}