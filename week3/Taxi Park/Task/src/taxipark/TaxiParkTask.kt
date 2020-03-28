package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        // allDrivers.filter { d -> trips.none { it.driver = d } }.toSet()
        allDrivers - trips.map { it.driver }

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        if (minTrips < 1) allPassengers
        else trips
            .flatMap(Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { group -> group.size >= minTrips }
            .keys

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
        trips
            .filter { trip -> trip.driver == driver }
            .flatMap(Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { group -> group.size > 1 }
            .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
        allPassengers
                .associateWith { p -> trips.filter { t -> p in t.passengers } }
                .filterValues { group ->
                    val (withDiscount, withoutDiscount) = group.partition { it.discount != null }
                    withDiscount.size > withoutDiscount.size
                }
                .keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return trips.map { IntRange(it.duration / 10 * 10, it.duration - it.duration % 10 + 9) }
            .groupingBy { it }.eachCount()
            .maxBy { it.value }
            ?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val income80 = trips.sumByDouble(Trip::cost) * 0.8
    val drivers20count = (allDrivers.size * 0.2).toInt();
    val incomeDrivers20 = trips
        .asSequence()
        .groupBy(Trip::driver)
        .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble(Trip::cost) }
        .sortedDescending()
        .take(drivers20count)
        .sum()
    return incomeDrivers20 >= income80
}