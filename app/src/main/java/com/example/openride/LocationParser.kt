package com.example.openride

object LocationParser {

    fun parse(url: String): RideLocation? {

        Regex("""!3d(-?\d+\.\d+)!4d(-?\d+\.\d+)""")
            .find(url)
            ?.let {

                return RideLocation(
                    latitude = it.groupValues[1].toDouble(),
                    longitude = it.groupValues[2].toDouble()
                )
            }

        Regex("""q=(-?\d+\.\d+),(-?\d+\.\d+)""")
            .find(url)
            ?.let {

                return RideLocation(
                    latitude = it.groupValues[1].toDouble(),
                    longitude = it.groupValues[2].toDouble()
                )
            }

        return null
    }
}