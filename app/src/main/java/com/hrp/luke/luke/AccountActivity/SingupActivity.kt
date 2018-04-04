package com.hrp.luke.luke.AccountActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.hrp.luke.luke.R
import android.widget.ProgressBar
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.hrp.luke.luke.MainActivity
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_singup.*


class SingupActivity : AppCompatActivity() {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnSignIn: Button? = null
    private var btnSignUp: Button? = null
    private var btnResetPassword: Button? = null
    private var progressBar: ProgressBar? = null

    private var auth : FirebaseAuth?= null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

//        add my code
        auth = FirebaseAuth.getInstance()

        btnSignIn = findViewById(R.id.sign_in_button) as Button
        btnSignUp = findViewById(R.id.sign_in_button) as Button
        inputEmail = findViewById(R.id.email) as EditText
        inputPassword = findViewById(R.id.password) as EditText
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        btnResetPassword = findViewById(R.id.btn_reset_password) as Button

        btnResetPassword!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@SingupActivity, ResetPasswordActivity::class.java))
        })

        btnSignIn!!.setOnClickListener(View.OnClickListener {
            finish()
        })
        btnSignUp!!.setOnClickListener(View.OnClickListener {
            val email = inputEmail!!.text.toString().trim()
            val password = inputPassword!!.text.toString().trim()

            if (TextUtils.isEmpty(email)){
                Toast.makeText(applicationContext,"Enter your email Address!!", Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (TextUtils.isEmpty(password)){
                Toast.makeText(applicationContext,"Enter your Password",Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            if (password.length < 6){
                Toast.makeText(applicationContext,"Password too short, enter mimimum 6 charcters" , Toast.LENGTH_LONG).show()
                return@OnClickListener
            }
            progressBar!!.setVisibility(View.VISIBLE)

                    //create user
            auth!!.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, OnCompleteListener {
                        task ->
                        Toast.makeText(this@SingupActivity,"createUserWithEmail:onComplete"+task.isSuccessful,Toast.LENGTH_SHORT).show()
                        progressBar!!.setVisibility(View.VISIBLE)

                        if (!task.isSuccessful){
                            Toast.makeText(this@SingupActivity,"User Not crated",Toast.LENGTH_SHORT).show()
                            return@OnCompleteListener
                        }else{
                            startActivity(Intent(this@SingupActivity, MainActivity::class.java))
                            finish()
                        }


                    })

        })
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.setVisibility(View.GONE)
    }
}

