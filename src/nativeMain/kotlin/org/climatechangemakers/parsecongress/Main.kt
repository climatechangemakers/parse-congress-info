package org.climatechangemakers.parsecongress

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import org.climatechangemakers.parsecongress.extensions.path
import platform.posix.clock

fun main(args: Array<String>) = Parse().main(args)

private fun Path.readContents(): String = FileSystem.SYSTEM.read(this, BufferedSource::readUtf8)

private val json: Json = Json {
  ignoreUnknownKeys = true
  explicitNulls = false
}

sealed class FileOption(description: String) : OptionGroup(description) {

  class DistrictOffices : FileOption("Convert a single district offices json into CSV") {
    val districtOfficesPath: Path by option("-d", "--district-offices").path(mustExist = true).required()
  }

  class CurrentLegislators : FileOption("Merge CWC and legislators JSON into a single CSV") {
    val legislatorsPath: Path by option("-l", "--legislators").path(mustExist = true).required()
    val cwcOfficeCodesPath: Path by option("-c", "--cwc").path(mustExist = true).required()
    val socialMediaPath: Path by option("-s", "--social").path(mustExist = true).required()
  }
}

class Parse : CliktCommand() {

  private val fileGroup by option("-t", "--type").groupChoice(
    "current-legislators" to FileOption.CurrentLegislators(),
    "district-offices" to FileOption.DistrictOffices(),
  ).required()

  private val outputFilePath: Path by argument(
    name = "output",
    help = "output CSV file",
  ).path(mustExist = false)

  override fun run() = when (val group = fileGroup) {
    is FileOption.CurrentLegislators -> {
      val currentLegislators: List<UnitedStatesMemberOfCongress> = parseUnitedStatesMemberOfCongressFile(
        group.legislatorsPath.readContents(),
        json,
      )

      val activeScwcOffices: Map<String, String> = parseActiveCwcOffices(
        group.cwcOfficeCodesPath.readContents(),
        json,
      ).associateBy(ActiveOffice::bioguide, ActiveOffice::officeCode)

      val legislatorTwitterAccounts: Map<String, String> = parseUnitedStatedMemberOfCongressSocialMedia(
        group.socialMediaPath.readContents(),
        json,
      ).associateBy(
        keySelector = { it.id.bioguide },
        valueTransform = { it.social.twitter },
      ).filterValues { it != null } as Map<String, String>


      val stuff = combineCurrentLegislators(currentLegislators, activeScwcOffices, legislatorTwitterAccounts)
      print(json.encodeToString(stuff))
    }
    is FileOption.DistrictOffices -> {
      println(group.districtOfficesPath.readContents())
    }
  }
}