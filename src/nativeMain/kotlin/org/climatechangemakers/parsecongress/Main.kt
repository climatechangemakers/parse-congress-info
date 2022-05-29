package org.climatechangemakers.parsecongress

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.groups.required
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt
import kotlinx.serialization.json.Json
import okio.Path
import org.climatechangemakers.parsecongress.extensions.filterNotNullValues
import org.climatechangemakers.parsecongress.extensions.path
import org.climatechangemakers.parsecongress.extensions.readContents
import org.climatechangemakers.parsecongress.extensions.writeContents

fun main(args: Array<String>) = Parse().main(args)

sealed class FileOption(description: String) : OptionGroup(description) {

  class DistrictOffices : FileOption("Convert a single district offices json into CSV") {
    val districtOfficesPath: Path by option("-d", "--district-offices").path(mustExist = true).required()
  }

  class CurrentLegislators : FileOption("Merge CWC and legislators JSON into a single CSV") {
    val legislatorsPath: Path by option("-l", "--legislators").path(mustExist = true).required()
    val historicalLegislatorsPath: Path by option("-h", "--historical-legislators").path(mustExist = true).required()
    val cwcOfficeCodesPath: Path by option("-c", "--cwc").path(mustExist = true).required()
    val socialMediaPath: Path by option("-s", "--social").path(mustExist = true).required()
  }
}

class Parse : CliktCommand() {

  private val json: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
  }

  private val fileGroup by option("-t", "--type").groupChoice(
    "current-legislators" to FileOption.CurrentLegislators(),
    "district-offices" to FileOption.DistrictOffices(),
  ).required()

  private val outputFilePath: Path by argument(
    name = "output",
    help = "output SQL file",
  ).path(mustExist = false)

  override fun run() = when (val group = fileGroup) {
    is FileOption.CurrentLegislators -> runCurrentLegislators(group)
    is FileOption.DistrictOffices -> runDistrictOffices(group)
  }

  private fun runCurrentLegislators(group: FileOption.CurrentLegislators) {
    val currentLegislators: List<UnitedStatesMemberOfCongress> = parseUnitedStatesMemberOfCongressFile(
      group.legislatorsPath.readContents(),
      json,
    )

    val historicalLegislators: List<UnitedStatesMemberOfCongress> = parseUnitedStatesMemberOfCongressFile(
      group.historicalLegislatorsPath.readContents(),
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
    ).filterNotNullValues()

    combineLegislators(
      historical = historicalLegislators,
      current = currentLegislators,
      activeScwcOffices = activeScwcOffices,
      twitterAccounts = legislatorTwitterAccounts,
      today = Clock.System.todayAt(TimeZone.UTC),
    ).let(::dumpToSql).run(outputFilePath::writeContents)
  }

  private fun runDistrictOffices(group: FileOption.DistrictOffices) {
    outputFilePath.writeContents(
      dumpToSql(readDistrictOfficeFile(group.districtOfficesPath, json))
    )
  }
}