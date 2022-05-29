package org.climatechangemakers.parsecongress

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import okio.BufferedSource
import okio.FileSystem
import okio.Path

@Serializable class UnitedStatesMemberOfCongressDistrictOffices(
  val id: UnitedStatesIdentifiers,
  val offices: List<UnitedStatedDistrictOffice>,
)

@Serializable class UnitedStatedDistrictOffice(
  val latitude: Double?,
  val longitude: Double?,
  val phone: String?,
)

class ClimateChangemakersDistrictOffice(
  val bioguide: String,
  val phoneNumber: String?,
  val lat: Double?,
  val long: Double?,
)

fun readDistrictOfficeFile(path: Path, json: Json): List<ClimateChangemakersDistrictOffice> {
  val jsonString = FileSystem.SYSTEM.read(path, BufferedSource::readUtf8)
  return json.decodeFromString(
    deserializer = ListSerializer(UnitedStatesMemberOfCongressDistrictOffices.serializer()),
    string = jsonString,
  ).flatMap { member ->
    member.offices.asSequence().filter { office ->
      // There's no use persising district offices that don't have phone numbers; it's the only data point we care about.
      office.phone != null
    }.map { office ->
      ClimateChangemakersDistrictOffice(
        bioguide = member.id.bioguide,
        phoneNumber = office.phone,
        lat = office.latitude,
        long = office.longitude,
      )
    }
  }
}