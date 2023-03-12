package mingaz.lubinskiy.app.entities

import java.io.Serializable

data class Employee(
    var name: String? = null,
    var info: EmployeeInfo? = null,
    var referenceUrl: String? = null
) :Serializable
