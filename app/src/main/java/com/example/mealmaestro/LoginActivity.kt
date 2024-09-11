package com.example.mealmaestro

import com.example.mealmaestro.Auth.FacebookAuth
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mealmaestro.Auth.GoogleAuth
import com.example.mealmaestro.Auth.XAuth
import com.example.mealmaestro.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleAuth: GoogleAuth
    private lateinit var auth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    private lateinit var facebookAuth: FacebookAuth
    private lateinit var xAuth: XAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Clear the text fields when this activity is launched
        binding.loginUsername.setText("")
        binding.loginPassword.setText("")

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            UserLogin()
        }

        binding.loginSignupBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        // ========================= GOOGLE ========================================================
        val getResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            googleAuth.handleSignInResult(result.data, auth)
        }

        googleAuth = GoogleAuth(this, getResult)
        binding.googleBtn.setOnClickListener {
            googleAuth.launchSignIn()
        }
        // ======================= FACEBOOK ========================================================
        facebookAuth = FacebookAuth(this@LoginActivity)
        binding.facebookBtn.setOnClickListener {
            facebookAuth.logInWithFacebook()
        }
        // =========================== X ===========================================================
        xAuth = XAuth(this)
        binding.xBtn.setOnClickListener {
            xAuth.xAuth()
        }
    }

    // ========================= EMAIL & PASSWORD ================================================
    private fun UserLogin() {
        val userName = binding.loginUsername.text.toString()
        val password = binding.loginPassword.text.toString()

        if (userName.isEmpty() || password.isEmpty()) {
            Toast.makeText(
                this@LoginActivity,
                "Please fill in Username and Password",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            auth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, please try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
