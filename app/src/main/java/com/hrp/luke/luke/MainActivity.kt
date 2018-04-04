package com.hrp.luke.luke

import com.hrp.luke.luke.AccountActivity.LoginActivity
import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import com.hrp.luke.luke.AccountActivity.SingupActivity


class MainActivity(parcel: Parcel) : AppCompatActivity() {

    private var btnChangePassword: Button? = null
    private var btnRemoveUser: Button? = null
    private var changePassword: Button? = null
    private var remove: Button? = null
    private var signOut: Button? = null
    private var email: TextView? = null

    private var oldEmail: EditText? = null
    private var password: EditText? = null
    private var newPassword: EditText? = null
    private var progressBar: ProgressBar? = null
    private var auth: FirebaseAuth? = null

    // this listener will be called when there is change in firebase user session
    internal var authListener: FirebaseAuth.AuthStateListener? = FirebaseAuth.AuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user == null) {
            // user auth state is changed - user is null
            // launch login activity
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        } else {
            setDataToView(user)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //get firebase auth instance
        auth = FirebaseAuth.getInstance()
        email = findViewById<View>(R.id.useremail) as TextView?

        //get current user
        val user = FirebaseAuth.getInstance().currentUser
        setDataToView(user!!)

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }


        btnChangePassword = findViewById<View>(R.id.change_password_button) as Button

        btnRemoveUser = findViewById<View>(R.id.remove_user_button) as Button

        changePassword = findViewById<View>(R.id.changePass) as Button

        remove = findViewById<View>(R.id.remove) as Button
        signOut = findViewById<View>(R.id.sign_out) as Button

        oldEmail = findViewById<View>(R.id.old_email) as EditText?

        password = findViewById<View>(R.id.password) as EditText?
        newPassword = findViewById<View>(R.id.newPassword) as EditText?

        oldEmail!!.visibility = View.GONE

        password!!.visibility = View.GONE
        newPassword!!.visibility = View.GONE

        changePassword!!.setVisibility(View.GONE)

        remove!!.setVisibility(View.GONE)

        progressBar = findViewById<View>(R.id.progressBar) as ProgressBar?

        if (progressBar != null) {
            progressBar!!.visibility = View.GONE
        }


        btnChangePassword!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                oldEmail!!.visibility = View.GONE

                password!!.visibility = View.GONE
                newPassword!!.visibility = View.VISIBLE

                changePassword!!.setVisibility(View.VISIBLE)

                remove!!.setVisibility(View.GONE)
            }
        })

        changePassword!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                progressBar!!.visibility = View.VISIBLE
                if (user != null && newPassword!!.text.toString().trim { it <= ' ' } != "") {
                    if (newPassword!!.text.toString().trim { it <= ' ' }.length < 6) {
                        newPassword!!.error = "Password too short, enter minimum 6 characters"
                        progressBar!!.visibility = View.GONE
                    } else {
                        user.updatePassword(newPassword!!.text.toString().trim { it <= ' ' })
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@MainActivity, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show()
                                        signOut()
                                        progressBar!!.visibility = View.GONE
                                    } else {
                                        Toast.makeText(this@MainActivity, "Failed to update password!", Toast.LENGTH_SHORT).show()
                                        progressBar!!.visibility = View.GONE
                                    }
                                }
                    }
                } else if (newPassword!!.text.toString().trim { it <= ' ' } == "") {
                    newPassword!!.error = "Enter password"
                    progressBar!!.visibility = View.GONE
                }
            }
        })


        btnRemoveUser!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                progressBar!!.visibility = View.VISIBLE
                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Your profile is deleted:( Create a account now!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@MainActivity, SingupActivity::class.java))
                        finish()
                        progressBar!!.visibility = View.GONE
                    } else {
                        Toast.makeText(this@MainActivity, "Failed to delete your account!", Toast.LENGTH_SHORT).show()
                        progressBar!!.visibility = View.GONE
                    }
                }
            }
        })

        signOut!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                signOut()
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun setDataToView(user: FirebaseUser) {

        email!!.text = "User Email: " + user.email!!


    }

    //sign out method
    fun signOut() {
        auth!!.signOut()


        // this listener will be called when there is change in firebase user session
        val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user == null) {
                // user auth state is changed - user is null
                // launch login activity
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar!!.visibility = View.GONE
    }

    public override fun onStart() {
        super.onStart()
        auth!!.addAuthStateListener(authListener!!)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth!!.removeAuthStateListener(authListener!!)
        }
    }

}