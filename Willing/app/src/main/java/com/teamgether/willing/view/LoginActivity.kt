package com.teamgether.willing.view

import android.os.Bundle
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.LoginViewModel

class LoginActivity : LoginViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}