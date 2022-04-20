package org.climatechangemakers.parsecongress.extensions

@Suppress("UNCHECKED_CAST")
fun <K, V : Any> Map<K, V?>.filterNotNullValues(): Map<K, V> {
  return filterValues { it != null } as Map<K, V>
}