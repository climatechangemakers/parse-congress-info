package org.climatechangemakers.parsecongress.extensions

import okio.BufferedSource
import okio.FileSystem
import okio.Path

fun Path.readContents(): String = FileSystem.SYSTEM.read(this, BufferedSource::readUtf8)

fun Path.writeContents(string: String) = FileSystem.SYSTEM.write(this) {
  writeUtf8(string)
}
