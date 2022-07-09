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
import mingaz.lubinskiy.app.department.DepartmentsListActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var userList: MutableList<Login> = mutableListOf()
    private val usernameValue = "CURRENT_USERNAME"
    private val passwordValue = "CURRENT_PASSWORD"

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
            startActivity(Intent(this, DepartmentsListActivity::class.java))
            onClick(binding.username.text.toString(), binding.password.text.toString())

            s.edit().apply {
                putString(usernameValue, binding.username.text.toString())
                putString(passwordValue, binding.password.text.toString())
            }.apply()
        }

        binding.username.setText(s.getString(usernameValue, "").toString())
        binding.password.setText(s.getString(passwordValue, "").toString())
    }

    override fun onStop() {
        super.onStop()
        finishAfterTransition()
    }

    private fun onClick(username: String, password: String) = with(binding) {
        val context = this@LoginActivity
        val intent = Intent(context, DepartmentsListActivity::class.java)

        if (username.isNotEmpty() && password.isNotEmpty()) {
            var validUser = false
            userList.forEach { user ->
                if (username == user.name && password.toInt() == user.password) {
                    validUser = true
                    startActivity(intent)
                } else if ((username != user.name || password.toInt() != user.password) && !validUser) {
                    validUser = false
                }
            }
            if (!validUser)
                Toast.makeText(context, "ФИО или/и Табельный номер введен/ы неверно", Toast.LENGTH_SHORT).show()
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
                Log.d("Login2077", "Failed to read value", error.toException())
            }
        })
    }
}
