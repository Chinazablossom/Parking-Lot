package parking

import kotlin.system.exitProcess

class ParkingLot {
    private val regexCreatePattern = """(?i)create\s(?<size>\d+)""".toRegex()
    private val regexParkingPattern = """(?i)park\s(?<regNum>[A-Za-z0-9-]+)\s(?<carColor>[A-Za-z]+)""".toRegex()
    private val regexLeavingPattern = """(?i)leave\s(?<parkingSpace>\d{1,2})""".toRegex()
    private val regexStatusPattern = """(?i)status""".toRegex()
    private val regexRegByColorPattern = """(?i)reg_by_color\s(?<carColor>[A-Za-z]+)""".toRegex()
    private val regexSpotByColorPattern = """(?i)spot_by_color\s(?<carColor>[A-Za-z]+)""".toRegex()
    private val regexSpotByRegPattern = """(?i)spot_by_reg\s(?<regNum>[A-Za-z0-9-]+)""".toRegex()
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

                userInput.matches(regexRegByColorPattern) -> {
                    if (lotCreated) {
                        val (carColor) = regexRegByColorPattern.find(userInput)!!.destructured
                        val regNums = parkingLot.filter { it?.carColor?.equals(carColor, true) == true }
                            .map { it!!.regNum }

                        if (regNums.isEmpty()) {
                            println("No cars with color $carColor were found.")
                        } else {
                            println(regNums.joinToString(", "))
                        }
                    } else {
                        println("Sorry, a parking lot has not been created.")
                    }
                }

                userInput.matches(regexSpotByColorPattern) -> {
                    if (lotCreated) {
                        val (carColor) = regexSpotByColorPattern.find(userInput)!!.destructured
                        val spots = parkingLot.withIndex()
                            .filter { it.value?.carColor?.equals(carColor, true) == true }
                            .map { it.index + 1 }

                        if (spots.isEmpty()) {
                            println("No cars with color $carColor were found.")
                        } else {
                            println(spots.joinToString(", "))
                        }
                    } else {
                        println("Sorry, a parking lot has not been created.")
                    }
                }

                userInput.matches(regexSpotByRegPattern) -> {
                    if (lotCreated) {
                        val (regNum) = regexSpotByRegPattern.find(userInput)!!.destructured
                        val spot = parkingLot.withIndex()
                            .find { it.value?.regNum?.equals(regNum, true) == true }
                            ?.index?.plus(1)

                        if (spot == null) {
                            println("No cars with registration number $regNum were found.")
                        } else {
                            println(spot)
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
