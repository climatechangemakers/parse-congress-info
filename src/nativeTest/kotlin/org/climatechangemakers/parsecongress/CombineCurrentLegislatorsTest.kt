package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CombineCurrentLegislatorsTest {

  private val activeScwcOffices = mapOf(
    "P000145" to "SCA03",
    "K000367" to "SMN01",
    "K000383" to "SME01",
  )

  private val legislatorTwitterAccounts = mapOf(
    "P000145" to "SenAlexPadilla",
    "K000367" to "SenAmyKlobuchar",
    "K000383" to "SenAngusKing",
  )

  @Test fun `combining gets correct twitter account`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "P000145"),
      name = UnitedStatesNameInfo(
        firstName = "Alex",
        lastName = "Padilla",
        officialFullname = "Alex Padilla",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "sen",
          state = "CA",
          district = null,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1)
        )
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertEquals(
      expected = "SenAlexPadilla",
      actual = climateChangemakersMemberOfCongress.first().twitterHandle,
    )
  }

  @Test fun `combining gets correct cwc office code for senate`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "P000145"),
      name = UnitedStatesNameInfo(
        firstName = "Alex",
        lastName = "Padilla",
        officialFullname = "Alex Padilla",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "sen",
          state = "CA",
          district = null,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1)
        )
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertEquals(
      expected = "SCA03",
      actual = climateChangemakersMemberOfCongress.first().cwcOfficeCode,
    )
  }

  @Test fun `combining gets correct cwc office code for house`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "M001200"),
      name = UnitedStatesNameInfo(
        firstName = "Donald",
        lastName = "McEachin",
        officialFullname = "A. Donald McEachin",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "rep",
          state = "VA",
          district = 4,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1)
        )
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertEquals(
      expected = "HVA04",
      actual = climateChangemakersMemberOfCongress.first().cwcOfficeCode,
    )
  }

  @Test fun `combining has special case for American Somoa`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "M001200"),
      name = UnitedStatesNameInfo(
        firstName = "Donald",
        lastName = "McEachin",
        officialFullname = "A. Donald McEachin",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "rep",
          state = "AS",
          district = 0,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1)
        )
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertEquals(
      expected = "HAQ00",
      actual = climateChangemakersMemberOfCongress.first().cwcOfficeCode,
    )
  }

  @Test fun `historical legislators have a null CWC code`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "M001200"),
      name = UnitedStatesNameInfo(
        firstName = "Donald",
        lastName = "McEachin",
        officialFullname = "A. Donald McEachin",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "rep",
          state = "VA",
          district = 4,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2016, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1)
        )
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertNull(climateChangemakersMemberOfCongress.first().cwcOfficeCode)
  }

  @Test fun `combining gets correct term`() {
    val unitedStatesMemberOfCongress = UnitedStatesMemberOfCongress(
      id = UnitedStatesIdentifiers(bioguide = "M001200"),
      name = UnitedStatesNameInfo(
        firstName = "Donald",
        lastName = "McEachin",
        officialFullname = "A. Donald McEachin",
      ),
      terms = listOf(
        UnitedStatesTermInfo(
          representativeType = "rep",
          state = "AS",
          district = 0,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2024, monthNumber = 1, dayOfMonth = 1)
        ),
        UnitedStatesTermInfo(
          representativeType = "rep",
          state = "AS",
          district = 0,
          party = "Democrat",
          phone = "867.5309",
          start = LocalDate(year = 2016, monthNumber = 1, dayOfMonth = 1),
          end = LocalDate(year = 2020, monthNumber = 1, dayOfMonth = 1)
        ),
      )
    )

    val climateChangemakersMemberOfCongress = combineLegislators(
      legislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
    )

    assertEquals(
      expected = LocalDate(year = 2024, dayOfMonth = 1, monthNumber = 1),
      actual = climateChangemakersMemberOfCongress.first().termEndDate,
    )
  }
}