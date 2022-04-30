package org.climatechangemakers.parsecongress


fun dumpToCsv(offices: List<ClimateChangemakersDistrictOffice>): String = dumpToCsv(
  headers = listOf("bioguide_id", "phone_numbers", "lat", "long"),
  values = offices,
  writer = StringBuilder::appendDistrictOffice,
)

fun dumpToCsv(members: List<ClimateChangemakersMemberOfCongress>): String = dumpToCsv(
  headers = listOf(
    "bioguide_id",
    "full_name",
    "legislative_role",
    "state",
    "congressional_district",
    "party",
    "dc_phone_number",
    "twitter_handle",
    "cwc_office_code",
  ),
  values = members,
  writer = StringBuilder::appendMemberOfCongress,
)

private fun <T : Any> dumpToCsv(
  headers: List<String>,
  values: List<T>,
  writer: StringBuilder.(T) -> Unit,
): String = buildString {
  append(headers.joinToString(separator = "\t"))
  append("\n")
  values.joinInto(this, separator = "\n", writer = writer)
}

private fun StringBuilder.appendDistrictOffice(office: ClimateChangemakersDistrictOffice) {
  append(office.bioguide)
  append("\t")
  office.phoneNumber?.let(::append)
  append("\t")
  office.lat?.let(::append)
  append("\t")
  office.long?.let(::append)
}

private fun <T, A : Appendable> List<T>.joinInto(buffer: A, separator: CharSequence, writer: A.(T) -> Unit) {
  for ((index, element) in withIndex()) {
    if (index > 0) buffer.append(separator)
    buffer.writer(element)
  }
}

private fun StringBuilder.appendMemberOfCongress(memberOfCongress: ClimateChangemakersMemberOfCongress) {
  append(memberOfCongress.bioguideId)
  append("\t")
  append(memberOfCongress.fullName)
  append("\t")
  append(memberOfCongress.legislativeRole)
  append("\t")
  append(memberOfCongress.state)
  append("\t")
  memberOfCongress.congressionalDistrict?.let(::append)
  append("\t")
  append(memberOfCongress.party)
  append("\t")
  append(memberOfCongress.dcPhoneNumber)
  append("\t")
  memberOfCongress.twitterHandle?.let(::append)
  append("\t")
  memberOfCongress.cwcOfficeCode?.let(::append)
}