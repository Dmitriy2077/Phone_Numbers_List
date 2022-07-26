package mingaz.lubinskiy.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import mingaz.lubinskiy.app.databinding.ActivityLoginBinding
import mingaz.lubinskiy.app.entities.Login
import mingaz.lubinskiy.app.ui.activities.DepartmentsEmployeesListActivity
import mingaz.lubinskiy.app.utils.USERNAME_VALUE
import mingaz.lubinskiy.app.utils.USERNAME_PASSWORD

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var userList: MutableList<Login> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.database.setPersistenceEnabled(true)
        val database = Firebase.database(getString(R.string.db_url))
        val ref = database.getReference("login")
        Thread { onChangeListener(ref) }.start()

        val s = getSharedPreferences("userData", Context.MODE_PRIVATE)

        binding.login.setOnClickListener {
            onClick(binding.username.text.toString(), binding.password.text.toString())

            s.edit().apply {
                putString(USERNAME_VALUE, binding.username.text.toString())
                putString(USERNAME_PASSWORD, binding.password.text.toString())
            }.apply()
        }

        binding.username.setText(s.getString(USERNAME_VALUE, "").toString())
        binding.password.setText(s.getString(USERNAME_PASSWORD, "").toString())
    }

    override fun onStop() {
        super.onStop()
        finishAfterTransition()
    }

    private fun onClick(username: String, password: String) = with(binding) {
        val context = this@LoginActivity
        val intent = Intent(
            context,
            DepartmentsEmployeesListActivity::class.java
        )

        if (username.isNotEmpty() && password.isNotEmpty()) {
            var validUser = false
            userList.forEach { user ->
                if (username.lowercase()
                        .trim() == user.name?.lowercase() && password.toInt() == user.password
                ) {
                    validUser = true
                    startActivity(intent)
                } else if ((username.lowercase().trim() != user.name?.lowercase()
                            || password.toInt() != user.password) && !validUser
                ) {
                    validUser = false
                }
            }
            if (!validUser)
                Toast.makeText(
                    context,
                    "ФИО или/и Табельный номер введен/ы неверно",
                    Toast.LENGTH_LONG
                ).show()
        } else {
            Toast.makeText(context, "ФИО или/и Табельный номер не заполнен/ы", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun onChangeListener(ref: DatabaseReference) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val user = it.getValue(Login::class.java)
                    if (user != null) userList.add(user)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Login01234", "Failed to read value", error.toException())
            }
        })
    }
}
