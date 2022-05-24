package org.climatechangemakers.parsecongress

import kotlinx.datetime.LocalDate

fun dumpToSql(members: List<ClimateChangemakersMemberOfCongress>) = buildString {
  val values = listOf(
    "bioguide_id",
    "full_name",
    "first_name",
    "last_name",
    "legislative_role",
    "state",
    "congressional_district",
    "party",
    "dc_phone_number",
    "twitter_handle",
    "cwc_office_code",
    "term_end",
  )

  writeInsertFragment(tableName = "member_of_congress", columnNames = values)

  members.joinInto(buffer = this, separator = ",\n", writer = StringBuilder::writerMember)
  append("\n")
  writeOnConflictFragment(conflictedColumn = "bioguide_id", columnNames = values)
  append(";")
}

fun dumpToSql(offices: List<ClimateChangemakersDistrictOffice>) = buildString {
  val tableName = "district_office"
  append("TRUNCATE ", tableName, ";\n")
  val values = listOf("bioguide_id", "phone_number", "lat", "long")
  writeInsertFragment(tableName, columnNames = values)
  offices.joinInto(buffer = this, separator = ",\n", writer = StringBuilder::writeOffice)
  append(";")
}

private fun StringBuilder.writeOffice(office: ClimateChangemakersDistrictOffice) = writeValues(
  listOf(
    office.bioguide,
    office.phoneNumber,
    office.lat,
    office.long,
  )
)

private fun StringBuilder.writerMember(member: ClimateChangemakersMemberOfCongress) = writeValues(
  listOf(
    member.bioguideId,
    member.fullName,
    member.firstName,
    member.lastName,
    member.legislativeRole,
    member.state,
    member.congressionalDistrict,
    member.party,
    member.dcPhoneNumber,
    member.twitterHandle,
    member.cwcOfficeCode,
    member.termEndDate,
  )
)

/**
 * Write the insert delclaration fragment for [tableName] with the associated [columnNames]. Eg
 * This statement will produce
 *
 * ```sql
 * INSERT INTO foo(bar)
 * VALUES
 * ```
 */
private fun StringBuilder.writeInsertFragment(tableName: String, columnNames: List<String>) {
  append("INSERT INTO ", tableName)
  append("(")
  columnNames.joinInto(buffer = this, separator = ", ", writer = StringBuilder::append)
  append(")\n")
  append("VALUES\n")
}

/**
 * Write the on conflict resolution fragment for [conflictedColumn] and override the associated
 * [columnNames] with the conflicted data. Eg:
 *
 * ```sql
 * ON CONFLICT(bar) DO UPDATE
 * SET (bar, baz) = (EXCLUDED.bar, EXCLUDED.baz)
 * ```
 */
private fun StringBuilder.writeOnConflictFragment(
  conflictedColumn: String,
  columnNames: List<String>
) {
  append("ON CONFLICT(", conflictedColumn, ") DO UPDATE\n")
  append("SET ")
  append("(")
  columnNames.joinInto(buffer = this, separator = ", ", writer = StringBuilder::append)
  append(")")
  append(" = ")
  append("(")
  columnNames.joinInto(buffer = this, separator = ", ") { value ->
    append("EXCLUDED.$value")
  }
  append(")")
}

private fun StringBuilder.writeValues(values: List<Any?>) {
  append("(")

  values.joinInto(this, separator = ", ") { value ->
    append(value.sqlEscape())
  }

  append(")")
}

private fun Any?.sqlEscape(): String = when (this) {
  null -> "NULL"
  is String -> "'${this.replace("'", "''")}'"
  is Number -> toString()
  is LocalDate -> "'$year-${monthNumber.toString().padStart(2, padChar = '0')}-$dayOfMonth'"
  else -> error("Unexpected type ${this::class}")
}

private fun <T, A : Appendable> List<T>.joinInto(buffer: A, separator: CharSequence, writer: A.(T) -> Unit) {
  for ((index, element) in withIndex()) {
    if (index > 0) buffer.append(separator)
    buffer.writer(element)
  }
}
