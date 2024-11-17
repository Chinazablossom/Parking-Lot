package parking

import kotlin.system.exitProcess

class ParkingLot {
    private val regexCreatePattern = """(?i)create\s(?<size>\d+)""".toRegex()
    private val regexParkingPattern = """(?i)park\s(?<regNum>[A-Za-z0-9-]+)\s(?<carColor>[A-Za-z]+)""".toRegex()
    private val regexLeavingPattern = """(?i)leave\s(?<parkingSpace>\d{1,2})""".toRegex()
    private val regexStatusPattern = """(?i)status""".toRegex()
    private var parkingLot: MutableList<Car?> = mutableListOf()
    private var lotCreated = false

    init {
        do {
            val userInput = readln()
            when {
                userInput.matches(regexCreatePattern) -> {
                    val (size) = regexCreatePattern.find(userInput)!!.destructured
                    val sizeInt = size.toInt()
                    if (sizeInt > 0) {
                        parkingLot = MutableList(sizeInt) { null }
                        lotCreated = true
                        println("Created a parking lot with $size spots.")
                    } else {
                        println("Invalid size. Please create a parking lot with a positive number of spots.")
                    }
                }

                userInput.matches(regexParkingPattern) -> {
                    if (lotCreated) {
                        val (regNum, carColor) = regexParkingPattern.find(userInput)!!.destructured
                        val availableSpot = parkingLot.indexOfFirst { it == null }

                        if (availableSpot != -1) {
                            parkingLot[availableSpot] = Car(regNum, carColor)
                            println("$carColor car parked in spot ${availableSpot + 1}.")
                        } else {
                            println("Sorry, the parking lot is full.")
                        }
                    } else {
                        println("Sorry, a parking lot has not been created.")
                    }
                }

                userInput.matches(regexLeavingPattern) -> {
                    if (lotCreated) {
                        val (parkingSpace) = regexLeavingPattern.find(userInput)!!.destructured
                        val index = parkingSpace.toInt() - 1
                        if (index in parkingLot.indices && parkingLot[index] != null) {
                            parkingLot[index] = null
                            println("Spot $parkingSpace is free.")
                        } else {
                            println("There is no car in spot $parkingSpace.")
                        }
                    } else {
                        println("Sorry, a parking lot has not been created.")
                    }
                }

                userInput.matches(regexStatusPattern) -> {
                    if (lotCreated) {
                        val occupiedSpots = parkingLot.withIndex()
                            .filter { it.value != null }
                            .map { "${it.index + 1} ${it.value!!.regNum} ${it.value!!.carColor}" }

                        if (occupiedSpots.isEmpty()) {
                            println("Parking lot is empty.")
                        } else {
                            occupiedSpots.forEach { println(it) }
                        }
                    } else {
                        println("Sorry, a parking lot has not been created.")
                    }
                }

                userInput.equals("exit", true) -> exitProcess(0)

                else -> println("Invalid command.")
            }
        } while (true)
    }

    inner class Car(val regNum: String, val carColor: String)
}

fun main() {
    ParkingLot()
}
