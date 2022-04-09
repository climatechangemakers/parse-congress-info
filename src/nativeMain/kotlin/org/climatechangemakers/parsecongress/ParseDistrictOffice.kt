package org.climatechangemakers.parsecongress

import okio.BufferedSource
import okio.FileSystem
import okio.Path

fun readDistrictOfficeFile(path: Path) {
  val jsonString = FileSystem.SYSTEM.read(path, BufferedSource::readUtf8)
  println(jsonString)
}