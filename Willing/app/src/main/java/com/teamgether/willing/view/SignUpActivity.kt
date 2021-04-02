package com.teamgether.willing.view

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : SignUpViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        sign_up_check_nickName_btn.setOnClickListener {
            val name = signup_nickName.text.toString()
            var isDuplicate = false
            nickNameCheck(name,isDuplicate)


        }


        finishSignUpBtn.setOnClickListener {
            val email = signup_email.text.toString()
            val password = signup_pw.text.toString()
            val name = signup_nickName.text.toString()
            val donationName = signup_dona.text.toString()

            createUser(email, password)
            setData(name, email, donationName)


        }

    }

    internal fun btn_on() {
        sign_up_check_nickName_btn.isEnabled = true
    }

    internal fun btn_off() {
        sign_up_check_nickName_btn.isEnabled = false
    }


}

