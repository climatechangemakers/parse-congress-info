package org.climatechangemakers.parsecongress

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class CombineCurrentLegislatorsTest {

  private val fakeClock = object : Clock {
    // April 27, 2022
    override fun now() = Instant.fromEpochSeconds(epochSeconds = 1651078162L)
  }

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

    val climateChangemakersMemberOfCongress = combineCurrentLegislators(
      currentLegislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      fakeClock,
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

    val climateChangemakersMemberOfCongress = combineCurrentLegislators(
      currentLegislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      fakeClock,
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

    val climateChangemakersMemberOfCongress = combineCurrentLegislators(
      currentLegislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      fakeClock,
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

    val climateChangemakersMemberOfCongress = combineCurrentLegislators(
      currentLegislators = listOf(unitedStatesMemberOfCongress),
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      fakeClock,
    )

    assertEquals(
      expected = "HAQ00",
      actual = climateChangemakersMemberOfCongress.first().cwcOfficeCode,
    )
  }
}