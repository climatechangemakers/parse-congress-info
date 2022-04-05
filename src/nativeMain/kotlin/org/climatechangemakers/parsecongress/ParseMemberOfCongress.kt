package org.climatechangemakers.parsecongress

import okio.BufferedSource
import okio.FileSystem
import okio.Path

fun readMemberOfCongressFile(path: Path): List<UnitedStatesMemberOfCongress> {
  val jsonString = FileSystem.SYSTEM.read(path, BufferedSource::readUtf8)
  println(jsonString)
  TODO()
}