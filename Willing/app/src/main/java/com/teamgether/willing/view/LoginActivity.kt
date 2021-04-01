package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : LoginViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        finishLoginBtn.setOnClickListener {

            val email = login_email.text.toString()
            val password = login_pwd.text.toString()

            login(email, password)

        }
        gotoSignUpBtn.setOnClickListener {
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
        }



    }
}