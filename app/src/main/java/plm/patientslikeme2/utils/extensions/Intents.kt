package plm.patientslikeme2.utils.extensions

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import plm.patientslikeme2.R
import plm.patientslikeme2.databinding.DialogActionCommonDialogBinding
import plm.patientslikeme2.utils.PasswordStrength
import plm.patientslikeme2.utils.enum.FontSize


/*
Applying fonts
 */
fun getFonts(context: Context, tag: Int): Typeface? {
    var fromAsset: Typeface? = null
    when (tag) {
        1 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Black.ttf")
        2 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-BlackItalic.ttf")
        3 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Bold.ttf")
        4 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-BoldItalic.ttf")
        5 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-ExtraBold.ttf")
        6 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-ExtraBoldItalic.ttf")
        7 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-ExtraLight.ttf")
        8 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-ExtraLightItalic.ttf")
        9 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Italic.ttf")
        10 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Light.ttf")
        11 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-LightItalic.ttf")
        12 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Medium.ttf")
        13 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-MediumItalic.ttf")
        14 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Regular.ttf")
        15 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-SemiBold.ttf")
        16 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-SemiBoldItalic.ttf")
        17 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-Thin.ttf")
        18 -> fromAsset = Typeface.createFromAsset(context.assets, "fonts/Poppins-ThinItalic.ttf")
    }
    return fromAsset
}

/*
Get json file from path
 */
fun getJsonFile(context: Context, filePath: String): String {
    return context.assets.open(filePath).bufferedReader().use { it.readText() }
}

/*
Applying font size small, medium and large
 */
fun getUpdatedFontSettings(defaultTextSize: Float, fontSizeEnum: Int): Float {
    when (fontSizeEnum) {
        FontSize.Small.ordinal -> {
            return defaultTextSize
        }
        FontSize.Medium.ordinal -> {
            return (defaultTextSize + (defaultTextSize * 0.15)).toFloat()
        }
        FontSize.Large.ordinal -> {
            return (defaultTextSize + (defaultTextSize * 0.30)).toFloat()
        }
    }
    return defaultTextSize
}

/*
Get type face according to font
 */
fun getTypeFace(tag: Any?): Int? {
    tag?.let {
        if (it.toString().isDigitsOnly()) {
            return it.toString().toInt()
        }
    }
    return null
}

fun Int.to2DString(): String {
    return String.format("%02d", this)
}

fun inviteOthers(context: Context?) {
    val sharingIntent = Intent(Intent.ACTION_SEND)
    sharingIntent.type = "text/plain"
    val shareBody =
        "I would like to invite you to download ${context?.getString(R.string.app_name)} application. " + "\nInstall from https://play.google.com/store/apps/details?id=${context?.packageName}"
    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
    context?.startActivity(Intent.createChooser(sharingIntent, "Share via"))
}

fun openHyperLinks(context: Context?, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context?.startActivity(intent)
}

fun showActionDialog(
    context: Context,
    title: String?,
    message: String?,
    positive: String?,
    negative: String?,
    listener: ActionDialogListener,
) {
    val dialog = Dialog(context)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.setCancelable(false)
    val binding = DialogActionCommonDialogBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)
    binding.tvTitle.text = title
    binding.tvMessage.text = message
    binding.btnPositive.text = positive
    binding.btnNegative.text = negative

    binding.btnPositive.setOnClickListener {
        dialog.dismiss()
        listener.onPositiveButtonClick()
    }

    binding.btnNegative.setOnClickListener {
        dialog.dismiss()
    }
    dialog.show()
}

interface ActionDialogListener {
    fun onPositiveButtonClick()
}

interface OnAutoCompleteItemClickListener {
    fun onItemClick(item: Any?)
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

inline fun <reified T> Gson.fromJson(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)

fun calculatePasswordStrength(str: String, linearLayout: LinearLayout?, textView: TextView?) {
    val passwordStrength = PasswordStrength.calculate(str)
    if (str.isNotEmpty()) {
        linearLayout?.visibility = View.VISIBLE
        textView?.setText(passwordStrength.msg)
        textView?.setTextColor(passwordStrength.color)
    } else {
        linearLayout?.visibility = View.GONE
    }
}

fun getSeveritySelectedValue(type: Int): String {
    val value = when (type) {
        0 -> "1"
        2 -> "2"
        4 -> "3"
        6 -> "4"
        else -> "-1"
    }
    return value
}

fun isJSONValid(test: String?): Boolean {
    try {
        JSONObject(test.toString())
    } catch (ex: JSONException) {
        try {
            JSONArray(test)
        } catch (ex1: JSONException) {
            return false
        }
    }
    return true
}