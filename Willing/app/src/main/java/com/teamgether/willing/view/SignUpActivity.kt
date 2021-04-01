package com.teamgether.willing.view

import android.os.Bundle
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.SignupViewModel
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : SignupViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        finishSignUpBtn.setOnClickListener {
            val email = signup_email.text.toString()
            val password = signup_pw.text.toString()
            val nickname = signup_nickName.text.toString()
            val donationName = signup_dona.text.toString()

            createUser(email, password)
            setData(nickname,email, donationName)

        }

    }

}

