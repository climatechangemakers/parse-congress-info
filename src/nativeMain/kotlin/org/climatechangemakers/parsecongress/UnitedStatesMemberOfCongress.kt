package org.climatechangemakers.parsecongress

class UnitedStatesMemberOfCongress(
  private val id: UnitedStatesMemberOfCongressBioguide,
  private val name: UnitedStatesMemberOfCongressName,
  private val terms: List<UnitedStatesMemberOfCongressTerm>,
)

class UnitedStatesMemberOfCongressBioguide(
  val bioguide: String,
)

class UnitedStatesMemberOfCongressName(
  val first: String,
  val last: String,
  val fullName: String,
)

class UnitedStatesMemberOfCongressTerm(
  val type: LegislatorType,
  val start: String,
  val end: String,
  val state: String,
  val phone: String,
)