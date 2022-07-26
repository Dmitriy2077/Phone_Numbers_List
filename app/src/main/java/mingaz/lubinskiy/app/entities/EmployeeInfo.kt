package mingaz.lubinskiy.app.entities

import java.io.Serializable

data class EmployeeInfo(
    var position: String? = null,
    var numbers: HashMap<String, Long>? = null
): Serializable
