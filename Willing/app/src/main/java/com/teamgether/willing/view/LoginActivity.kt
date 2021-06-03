package com.teamgether.willing.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : LoginViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        finishLoginBtn.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_pwd.text.toString()

            login_warning_email.text = ""
            login_warning_pwd.text = ""


            if (email.isNotBlank() && password.isNotBlank()) {
                login_warning_email.text = ""
                login_warning_pwd.text = ""
                login(email, password, ::alertUser, ::alertEmail, ::gotoMain)
            } else {
                if (email.isBlank()) {
                    login_warning_email.setText(R.string.login_warning_null)
                } else {
                    login_warning_email.text = ""
                }
                if (password.isBlank()) {
                    login_warning_pwd.setText(R.string.login_warning_null)
                } else {
                    login_warning_pwd.text = ""
                }
            }


        }
        gotoSignUpBtn.setOnClickListener {
            val nextIntent = Intent(this, SignUpActivity::class.java)
            startActivity(nextIntent)
            Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }

    fun alertBuild(setTitle: Int, setMessage: Int, setButton: Int) {
        val builder = AlertDialog.Builder(this)
            .setTitle(setTitle)
            .setMessage(setMessage)
            .setPositiveButton(setButton) { _: DialogInterface?, _: Int ->
            }
        builder.show()
    }

    fun alertEmail() {
        alertBuild(
            R.string.email_verification_title,
            R.string.email_verification_message,
            R.string.email_verification_btnText
        )
    }

    fun alertUser() {
        alertBuild(
            R.string.login_wrong_user_title,
            R.string.login_wrong_user_message,
            R.string.login_wrong_user_btnText
        )
    }

    fun gotoMain(email:String) {
        val nextIntent = Intent(this, MainActivity::class.java)
        nextIntent.putExtra("email",email)
        startActivity(nextIntent)
        finish()
    }
}
