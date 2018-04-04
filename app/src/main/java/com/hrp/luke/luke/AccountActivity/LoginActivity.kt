package com.hrp.luke.luke.AccountActivity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthResult
import com.hrp.luke.luke.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hrp.luke.luke.MainActivity


class LoginActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword:EditText? = null
    private var btnSignup:Button? =null
    private var btnLogin :Button?=null
    private var btnReset:Button? =null
    private var progressBar:ProgressBar?=null
    private var auth:FirebaseAuth?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (auth!!.currentUser !=null){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }

        setContentView(R.layout.activity_login)
        inputEmail = findViewById(R.id.email) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        btnSignup = findViewById(R.id.sign_in_button) as Button
        btnLogin = findViewById(R.id.login) as Button
        btnReset = findViewById(R.id.btn_reset_password) as Button

        auth = FirebaseAuth.getInstance()

        btnSignup!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity,SingupActivity::class.java))
        })
        btnReset!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@LoginActivity,ResetPasswordActivity::class.java))
        })
        btnLogin!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Please Entre your email.",Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(applicationContext, "Please Enter your Password", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            }
            progressBar!!.setVisibility(View.VISIBLE)

            auth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, OnCompleteListener {
                        task ->
                        progressBar!!.setVisibility(View.VISIBLE)

                        if (!task.isSuccessful){
                            if (password.length < 6){
                                inputPassword!!.setError(getString(R.string.minimum_password))
                            }else{
                                Toast.makeText(this@LoginActivity,getString(R.string.auth_failed),Toast.LENGTH_LONG).show()
                            }
                        }else{
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()
                        }
                    })
        })

    }
}
