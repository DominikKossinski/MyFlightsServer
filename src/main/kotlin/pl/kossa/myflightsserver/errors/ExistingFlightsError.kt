package pl.kossa.myflightsserver.errors

enum class ExistingEntityType(val entityName: String) {
    AIRPLANE("airplane"),
    AIRPORT("airport"),
    RUNWAY("runway")
}

class ExistingFlightsError(
    private val type: ExistingEntityType,
    private val entityId: String
) : ApiError() {
    override val message: String
        get() = "existing flights with ${type.entityName}"

    override val description: String
        get() = "You cannot delete ${type.entityName} with id $entityId, because there are some flights containing it."
}
