package parking


class ParkingLot {
    private val regexParkingPattern = """(?<action>park)\s(?<regNum>[A-Za-z0-9-\S]+)\s(?<carColor>[A-Za-z]{2,})""".toRegex()
    private val regexLeavingPattern = """(?<action>[l|L]eave)\s(?<parkingSpce>\d)""".toRegex()
    private val parkingLot = mutableListOf(Car("KA-01-HH-2024", "Black"))

    init {
        val userInput = readln()
        when {
            userInput.matches(regexParkingPattern) -> {
                val (action, regNum, carColor) = regexParkingPattern.find(userInput)!!.destructured
                parkingLot.add(Car(regNum, carColor))
                println("$carColor car parked in spot 2.")
            }

            userInput.matches(regexLeavingPattern) -> {
                val (action, parkingSpace) = regexLeavingPattern.find(userInput)!!.destructured
                val index = parkingSpace.toInt() - 1
                try {
                    parkingLot.removeAt(index)
                    println("Spot $parkingSpace is free.")
                } catch (e: Exception) {
                    println("There is no car in spot $parkingSpace.")

                }

            }


        }
    }

    inner class Car(val regNum: String, val carColor: String)

}

fun main() {
    ParkingLot()

}