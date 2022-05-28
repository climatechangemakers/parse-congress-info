package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals

class DumpToSqlTest {

  @Test fun `dumping a single member of congress produces expected result`() {
    val members = listOf(
      ClimateChangemakersMemberOfCongress(
        bioguideId = "C000001",
        fullName = "Kevin Cianfarini",
        firstName = "Kevin",
        lastName = "Cianfarini",
        legislativeRole = "rep",
        state = "VA",
        congressionalDistrict = 4,
        party = "Democrat",
        dcPhoneNumber = "555.555.5555",
        twitterHandle = null,
        cwcOfficeCode = null,
        termEndDate = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
      )
    )

    assertEquals(
      expected = """
        |INSERT INTO member_of_congress(bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end)
        |VALUES
        |('C000001', 'Kevin Cianfarini', 'Kevin', 'Cianfarini', 'rep', 'VA', 4, 'Democrat', '555.555.5555', NULL, NULL, '2022-04-27')
        |ON CONFLICT(bioguide_id) DO UPDATE
        |SET (bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end) = (EXCLUDED.bioguide_id, EXCLUDED.full_name, EXCLUDED.first_name, EXCLUDED.last_name, EXCLUDED.legislative_role, EXCLUDED.state, EXCLUDED.congressional_district, EXCLUDED.party, EXCLUDED.dc_phone_number, EXCLUDED.twitter_handle, EXCLUDED.cwc_office_code, EXCLUDED.term_end);
      """.trimMargin(),
      actual = dumpToSql(members)
    )
  }

  @Test fun `date is padded correctly`() {
    val members = listOf(
      ClimateChangemakersMemberOfCongress(
        bioguideId = "C000001",
        fullName = "Kevin Cianfarini",
        firstName = "Kevin",
        lastName = "Cianfarini",
        legislativeRole = "rep",
        state = "VA",
        congressionalDistrict = 4,
        party = "Democrat",
        dcPhoneNumber = "555.555.5555",
        twitterHandle = null,
        cwcOfficeCode = null,
        termEndDate = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 7),
      )
    )

    assertEquals(
      expected = """
        |INSERT INTO member_of_congress(bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end)
        |VALUES
        |('C000001', 'Kevin Cianfarini', 'Kevin', 'Cianfarini', 'rep', 'VA', 4, 'Democrat', '555.555.5555', NULL, NULL, '2022-04-07')
        |ON CONFLICT(bioguide_id) DO UPDATE
        |SET (bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end) = (EXCLUDED.bioguide_id, EXCLUDED.full_name, EXCLUDED.first_name, EXCLUDED.last_name, EXCLUDED.legislative_role, EXCLUDED.state, EXCLUDED.congressional_district, EXCLUDED.party, EXCLUDED.dc_phone_number, EXCLUDED.twitter_handle, EXCLUDED.cwc_office_code, EXCLUDED.term_end);
      """.trimMargin(),
      actual = dumpToSql(members)
    )
  }

  @Test fun `dumping a single member of congress escapes apostrophe`() {
    val members = listOf(
      ClimateChangemakersMemberOfCongress(
        bioguideId = "C000001",
        fullName = "Kevin Ci'anfarini",
        firstName = "Kevin",
        lastName = "Ci'anfarini",
        legislativeRole = "rep",
        state = "VA",
        congressionalDistrict = 4,
        party = "Democrat",
        dcPhoneNumber = "555.555.5555",
        twitterHandle = null,
        cwcOfficeCode = null,
        termEndDate = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
      )
    )

    assertEquals(
      expected = """
        |INSERT INTO member_of_congress(bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end)
        |VALUES
        |('C000001', 'Kevin Ci''anfarini', 'Kevin', 'Ci''anfarini', 'rep', 'VA', 4, 'Democrat', '555.555.5555', NULL, NULL, '2022-04-27')
        |ON CONFLICT(bioguide_id) DO UPDATE
        |SET (bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end) = (EXCLUDED.bioguide_id, EXCLUDED.full_name, EXCLUDED.first_name, EXCLUDED.last_name, EXCLUDED.legislative_role, EXCLUDED.state, EXCLUDED.congressional_district, EXCLUDED.party, EXCLUDED.dc_phone_number, EXCLUDED.twitter_handle, EXCLUDED.cwc_office_code, EXCLUDED.term_end);
      """.trimMargin(),
      actual = dumpToSql(members)
    )
  }

  @Test fun `dumping multiple members of congress produces the expected result`() {
    val members = listOf(
      ClimateChangemakersMemberOfCongress(
        bioguideId = "C000001",
        fullName = "Kevin Cianfarini",
        firstName = "Kevin",
        lastName = "Cianfarini",
        legislativeRole = "rep",
        state = "VA",
        congressionalDistrict = 4,
        party = "Democrat",
        dcPhoneNumber = "555.555.5555",
        twitterHandle = null,
        cwcOfficeCode = null,
        termEndDate = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
      ),
      ClimateChangemakersMemberOfCongress(
        bioguideId = "C000002",
        fullName = "Kevin Cianfarini 2",
        firstName = "Kevin",
        lastName = "Cianfarini 2",
        legislativeRole = "sen",
        state = "VA",
        congressionalDistrict = null,
        party = "Republican",
        dcPhoneNumber = "555.555.5556",
        twitterHandle = null,
        cwcOfficeCode = null,
        termEndDate = LocalDate(year = 2022, monthNumber = 4, dayOfMonth = 27),
      )
    )

    assertEquals(
      expected = """
        |INSERT INTO member_of_congress(bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end)
        |VALUES
        |('C000001', 'Kevin Cianfarini', 'Kevin', 'Cianfarini', 'rep', 'VA', 4, 'Democrat', '555.555.5555', NULL, NULL, '2022-04-27'),
        |('C000002', 'Kevin Cianfarini 2', 'Kevin', 'Cianfarini 2', 'sen', 'VA', NULL, 'Republican', '555.555.5556', NULL, NULL, '2022-04-27')
        |ON CONFLICT(bioguide_id) DO UPDATE
        |SET (bioguide_id, full_name, first_name, last_name, legislative_role, state, congressional_district, party, dc_phone_number, twitter_handle, cwc_office_code, term_end) = (EXCLUDED.bioguide_id, EXCLUDED.full_name, EXCLUDED.first_name, EXCLUDED.last_name, EXCLUDED.legislative_role, EXCLUDED.state, EXCLUDED.congressional_district, EXCLUDED.party, EXCLUDED.dc_phone_number, EXCLUDED.twitter_handle, EXCLUDED.cwc_office_code, EXCLUDED.term_end);
      """.trimMargin(),
      actual = dumpToSql(members)
    )
  }

  @Test fun `dumping a single district office produces expected result`() {
    val offices = listOf(
      ClimateChangemakersDistrictOffice(
        bioguide = "C000002",
        phoneNumber = "804.555.3456",
        lat = 1.0,
        long = 2.0,
      ),
    )

    assertEquals(
      expected = """
        |TRUNCATE district_office;
        |INSERT INTO district_office(bioguide_id, phone_number, lat, long)
        |VALUES
        |('C000002', '804.555.3456', 1.0, 2.0);
      """.trimMargin(),
      actual = dumpToSql(offices),
    )
  }

  @Test fun `dumping multiple district offices produces expected result`() {
    val offices = listOf(
      ClimateChangemakersDistrictOffice(
        bioguide = "C000002",
        phoneNumber = "804.555.3456",
        lat = 1.0,
        long = 2.0,
      ),
      ClimateChangemakersDistrictOffice(
        bioguide = "C000002",
        phoneNumber = "804.556.3456",
        lat = null,
        long = null,
      ),
    )

    assertEquals(
      expected = """
        |TRUNCATE district_office;
        |INSERT INTO district_office(bioguide_id, phone_number, lat, long)
        |VALUES
        |('C000002', '804.555.3456', 1.0, 2.0),
        |('C000002', '804.556.3456', NULL, NULL);
      """.trimMargin(),
      actual = dumpToSql(offices),
    )
  }
}