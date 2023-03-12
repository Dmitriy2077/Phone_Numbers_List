package mingaz.lubinskiy.app.utils

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import mingaz.lubinskiy.app.entities.Department
import mingaz.lubinskiy.app.entities.Employee


object DialogManager {
    fun changeEmpNumDialog(context: Context, number: String?, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val changedNumber = EditText(context)

        dialog.setTitle("Редактирование номера")
        changedNumber.setText(number)
        changedNumber.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
        dialog.setView(changedNumber)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Сохранить") { _, _ ->
            listener.onClick(changedNumber.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()

        fun formatString(s: String): String {
            return if (s.contains(Regex("[0-9]{9,12}")))
                "+${s.substring(1, s.length - 1)}"
            else
                "${s.substring(1, s.length - 1)}:"
        }
    }

    fun changeEmpPosDialog(context: Context, position: String?, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val changedPosition = EditText(context)

        dialog.setTitle("Редактирование должности")
        changedPosition.setText(position)
        dialog.setView(changedPosition)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Сохранить") { _, _ ->
            listener.onClick(changedPosition.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun changeEmpNamePosDialog(context: Context, employee: Employee, listener: EmpListener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val changedName = EditText(context)
        val changedPosition = EditText(context)

        dialog.setTitle("Редактирование сотрудника")
        changedName.inputType = InputType.TYPE_CLASS_TEXT
        changedPosition.inputType = InputType.TYPE_CLASS_TEXT
        changedName.setText(employee.name)
        changedPosition.setText(employee.info?.position)

        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.addView(changedName)
        ll.addView(changedPosition)
        dialog.setView(ll)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Сохранить") { _, _ ->
            listener.onEmpClick(changedName.text.toString(), changedPosition.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun changeDepNameDialog(context: Context, department: Department, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        val changedName = EditText(context)

        dialog.setTitle("Редактирование названия")
        changedName.setText(department.name)
        dialog.setView(changedName)

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Сохранить") { _, _ ->
            listener.onClick(changedName.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Отмена") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun test2(context: Context, employee: Employee, listener: EmpListener) {
        val alertDialog = AlertDialog.Builder(context)
//        val alertDialog = alerDialog1.create()
        alertDialog.setTitle("Values")
        val quantity = EditText(context)
        val lot = EditText(context)

        quantity.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_FLAG_DECIMAL
        lot.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        /*Project = arr.get(0).toString()
        Item = arr.get(1).toString()*/

        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL
        ll.addView(quantity)
        ll.addView(lot)
        alertDialog.setView(ll)

        alertDialog.setCancelable(false)
        alertDialog.setPositiveButton("Update") { _, _ ->
            //ACTION
        }
        alertDialog.setPositiveButton("Yes Option") { _, _ ->
            //What ever you want to do with the value
        }

        alertDialog.show()
    }

    fun test(context: Context, department: Department, listener: Listener) {
        val alert = AlertDialog.Builder(context)

        val edittext = EditText(context)
        alert.setMessage("Enter Your Message")
        alert.setTitle("Enter Your Title")

        alert.setView(edittext)

        alert.setPositiveButton("Yes Option") { _, _ ->
            //What ever you want to do with the value
            var YouEditTextValue: Editable = edittext.text
            //OR
            var YouStringValue: String = edittext.text.toString()
        }

        alert.setNegativeButton("No Option") { _, _ ->
            // what ever you want to do with No option.
        }

        alert.show();
    }

    fun locationSettingsDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("Enable location?")
        dialog.setMessage("Location disabled, do you want enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    fun searchByNameDialog(context: Context, listener: Listener) {
        val builder = AlertDialog.Builder(context)
        val edName = EditText(context)
        builder.setView(edName)
        val dialog = builder.create()
        dialog.setTitle("City name:")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClick(edName.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String?)
    }

    interface EmpListener {
        fun onEmpClick(name: String?, empPosition: String?)
    }
}