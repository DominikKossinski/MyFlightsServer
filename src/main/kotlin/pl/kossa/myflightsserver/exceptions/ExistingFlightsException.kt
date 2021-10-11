package pl.kossa.myflightsserver.exceptions

import pl.kossa.myflightsserver.errors.ExistingEntityType

class ExistingFlightsException(val type: ExistingEntityType, val entityId: String) :
    ApiException("You cannot delete this ${type.entityName} with id $entityId, because of existing flights with it.")
