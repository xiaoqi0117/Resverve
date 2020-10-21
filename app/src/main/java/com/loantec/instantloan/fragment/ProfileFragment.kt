package com.loantec.instantloan.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.loantec.instantloan.BankAccountActivity
import com.loantec.instantloan.LoanRecordActivity
import com.loantec.instantloan.MyProfileActivity
import com.loantec.instantloan.R
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        record.setOnClickListener {
            startActivity(Intent(activity,LoanRecordActivity::class.java))
        }

        bank.setOnClickListener {
            startActivity(Intent(activity,BankAccountActivity::class.java))
        }

        profile.setOnClickListener {
            startActivity(Intent(activity,MyProfileActivity::class.java))
        }
    }
}