package nsl.sam.spring.config.ordering

import java.util.*

open class OrderingManager(initialValue: Int = 0, private val delta: Int = 1) {

    private val consumedNumbers: MutableList<Int> = mutableListOf()
    private var currentAutoNumber = initialValue
    var isAlreadyInitializedWithRestrictedList = false

    fun initializeWithRestrictedList(restrictedValues: List<Int>) {

        if(isAlreadyInitializedWithRestrictedList)
            throw IllegalStateException("This object is already initialized with list of restricted values")

        restrictedValues.forEach {
            if (consumedNumbers.contains(it)) {
                throw IllegalStateException("At least one of values from 'restrictedValues' list is already " +
                        "on list of produced/consumed numbers.")
            }
        }

        consumedNumbers.addAll(restrictedValues)
        isAlreadyInitializedWithRestrictedList = true
    }

    fun occupyNumber(number: Int) {
        if (consumedNumbers.contains(number))
            throw IllegalStateException("The number $number is already on the list of occupied number")

        consumedNumbers.add(number)
    }

    fun getNextNumber():Int {

        var nextAutoNumber: Int = currentAutoNumber+delta

        while(consumedNumbers.contains(nextAutoNumber))
            nextAutoNumber+=delta

        currentAutoNumber = nextAutoNumber
        consumedNumbers.add(currentAutoNumber)

        return currentAutoNumber
    }

}