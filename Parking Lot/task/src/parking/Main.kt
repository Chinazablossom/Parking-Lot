package parking

import kotlin.system.exitProcess

class ParkingLot {
    private val regexParkingPattern = """(?i)park\s(?<regNum>[A-Za-z0-9-]+)\s(?<carColor>[A-Za-z]+)""".toRegex()
    private val regexLeavingPattern = """(?i)leave\s(?<parkingSpace>\d{1,2})""".toRegex()
    private val parkingLot = MutableList<Car?>(20) { null }

    init {
        do {
            val userInput = readln()
            when {
                userInput.matches(regexParkingPattern) -> {
                    val (regNum, carColor) = regexParkingPattern.find(userInput)!!.destructured
                    val availableSpot = parkingLot.indexOfFirst { it == null }

                    if (availableSpot != -1) {
                        parkingLot[availableSpot] = Car(regNum, carColor)
                        println("$carColor car parked in spot ${availableSpot + 1}.")
                    } else {
                        println("Sorry, the parking lot is full.")
                    }
                }

                userInput.matches(regexLeavingPattern) -> {
                    val (parkingSpace) = regexLeavingPattern.find(userInput)!!.destructured
                    val index = parkingSpace.toInt() - 1
                    if (index in parkingLot.indices && parkingLot[index] != null) {
                        parkingLot[index] = null
                        println("Spot $parkingSpace is free.")
                    } else {
                        println("There is no car in spot $parkingSpace.")
                    }
                }

                userInput.equals("exit", true) -> exitProcess(0)
            }
        } while (true)
    }

    inner class Car(val regNum: String, val carColor: String)

}

fun main() {
    ParkingLot()
}
