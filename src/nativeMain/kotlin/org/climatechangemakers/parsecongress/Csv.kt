package org.climatechangemakers.parsecongress

fun dumpToCsv(members: List<ClimateChangemakersMemberOfCongress>): String = buildString {
  append(
    listOf(
      "bioguide_id",
      "full_name",
      "legislative_role",
      "state",
      "congressional_district",
      "party",
      "dc_phone_number",
      "twitter_handle",
      "cwc_office_code",
    ).joinToString(separator = "\t")
  )

  append("\n")

  members.joinInto(this, separator = "\n") { member ->
    appendMemberOfCongress(member)
  }
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
  append("\t")
}