package plm.patientslikeme2.utils.usercontrol

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.graphics.drawable.toDrawable
import com.bumptech.glide.Glide
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.LayoutProgressbarBinding

object CommonDialog {

    fun showLoadingDialog(context: Context): Dialog {
        val progressDialog = Dialog(context)
        progressDialog.setCancelable(false)
        val binding = LayoutProgressbarBinding.inflate(LayoutInflater.from(context))
        progressDialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
        Glide.with(context).load(R.raw.hexagone_indicator).into(binding.ivLoader)
        progressDialog.setContentView(binding.root)
        return progressDialog
    }
}