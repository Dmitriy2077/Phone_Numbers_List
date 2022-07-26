package mingaz.lubinskiy.app.entities

import java.io.Serializable

data class Department(
    var name: String? = null,
    var employees: MutableList<Employee>? = null
): Serializable
