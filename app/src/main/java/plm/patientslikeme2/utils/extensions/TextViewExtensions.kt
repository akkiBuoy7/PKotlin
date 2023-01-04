package plm.patientslikeme2.utils.extensions

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.View
import android.webkit.WebView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.color
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import plm.patientslikeme2.R
import plm.patientslikeme2.data.di.MyApplication
import plm.patientslikeme2.data.model.LatestReply
import plm.patientslikeme2.data.model.Tags
import plm.patientslikeme2.data.model.community.groups.discussions.Badges
import plm.patientslikeme2.data.model.community.groups.groupOverview.GroupOverviewResponse
import plm.patientslikeme2.data.model.community.members.Conditions
import plm.patientslikeme2.utils.usercontrol.CustomTypefaceSpan
import plm.patientslikeme2.utils.usercontrol.UserCheckBox
import plm.patientslikeme2.utils.usercontrol.UserTextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

fun UserTextView.addReadMore(text: String, textView: TextView, count: Int) {
    if (text.isNullOrEmpty().not() && text.length > count) {
        val ss = SpannableString(text.substring(0, count) + "... More")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                addReadLess(text, textView, count)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = MyApplication.instance.getColor(R.color.blue_darker)
            }
        }
        ss.setSpan(clickableSpan, ss.length - 4, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = ss
        textView.movementMethod = LinkMovementMethod.getInstance()
    }
}

fun UserTextView.addReadLess(text: String, textView: TextView, count: Int) {
    val stringLess = SpannableString("$text ... Less")
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(view: View) {
            addReadMore(text, textView, count)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
            ds.color = MyApplication.instance.getColor(R.color.blue_darker)
        }
    }
    stringLess.setSpan(
        clickableSpan,
        stringLess.length - 4,
        stringLess.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    textView.text = stringLess
    textView.movementMethod = LinkMovementMethod.getInstance()
}

fun UserTextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = MyApplication.instance.getColor(R.color.blue_darker)
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as UserTextView).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

fun UserCheckBox.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    var startIndexOfLink = -1
    for (link in links) {
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(textPaint: TextPaint) {
                // use this to change the link color
                textPaint.color = MyApplication.instance.getColor(R.color.blue_darker)
                // toggle below value to enable/disable
                // the underline shown below the clickable text
                textPaint.isUnderlineText = false
            }

            override fun onClick(view: View) {
                Selection.setSelection((view as UserCheckBox).text as Spannable, 0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
        if (startIndexOfLink == -1) continue
        spannableString.setSpan(
            clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

@BindingAdapter("firstCharacter")
fun UserTextView.getFirstCharacter(string: String) {
    text = if (TextUtils.isEmpty(string)) {
        ""
    } else {
        string.first().toString().uppercase()
    }
}

@BindingAdapter("upperFirstChar")
fun UserTextView.getUpperFirstChar(string: String) {
    text = if (TextUtils.isEmpty(string)) {
        ""
    } else {
        string.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

fun UserTextView.setTextBold(header: String, value: String) {
    val spannable: Spannable = SpannableString(header + value)
    header.let {
        spannable.setSpan(
            CustomTypefaceSpan(
                "fonts/Poppins-Regular.ttf",
                Typeface.createFromAsset(
                    context.applicationContext?.assets,
                    "fonts/Poppins-SemiBold.ttf"
                ),
                ContextCompat.getColor(context, R.color.black)
            ),
            0,
            it.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
    spannable.setSpan(
        CustomTypefaceSpan(
            "fonts/Poppins-Regular.ttf",
            Typeface.createFromAsset(
                context.applicationContext?.assets,
                "fonts/Poppins-Regular.ttf"
            ),
            ContextCompat.getColor(context, R.color.black)
        ),
        header.length,
        header.length + value.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = spannable
}

@BindingAdapter("setHtmlText")
fun UserTextView.setHtmlText(string: String?) {
    movementMethod = LinkMovementMethod.getInstance()
    text =
        string?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_COMPACT) }
}

@BindingAdapter("conditions", "showLivingWithText")
fun UserTextView.conditions(conditions: ArrayList<Conditions>?, showLivingWithText: Boolean = true) {
    if (conditions.isNullOrEmpty().not()) {
        if (conditions?.size == 1) this.text =
            if (showLivingWithText) "Living with ${conditions.first().name}" else conditions.first().name
        else
            if (showLivingWithText) this.text = SpannableStringBuilder().append("Living with ${conditions?.first()?.name}, ").color(ContextCompat.getColor(context, R.color.blue_darker)) { append("+${conditions!!.size - 1} more.") }
            else this.text = SpannableStringBuilder().append("${conditions?.first()?.name}, ").color(ContextCompat.getColor(context, R.color.blue_darker)) { append("+${conditions!!.size - 1} more.") }
    } else {
        this.visibility = View.INVISIBLE
    }
}

@BindingAdapter("livingWith", "showLivingWithText")
fun UserTextView.livingWith(livingWith: ArrayList<String>, showLivingWithText: Boolean = true) {
    if (livingWith.isNullOrEmpty().not()) {
        if (livingWith.size == 1) this.text =
            if (showLivingWithText) "Living with ${livingWith.first()}" else livingWith.first()
        else this.text =
            if (showLivingWithText) SpannableStringBuilder().append("Living with ${livingWith.first()}, ")
                .color(ContextCompat.getColor(context, R.color.blue_darker)) { append("+${livingWith.size - 1} more.") }
            else SpannableStringBuilder().append("${livingWith.first()}, ").color(ContextCompat.getColor(context, R.color.blue_darker)) { append("+${livingWith.size - 1} more.") }

    } else {
        this.visibility = View.INVISIBLE
    }
}

@BindingAdapter("age", "sex")
fun UserTextView.ageSex(age: Int, sex: String?) {
    if (age > 0 && sex.isNullOrEmpty().not()) {
        this.text = "$sex, $age"
    } else {
        if (sex.isNullOrEmpty() && age == 0) {
            this.visibility = View.INVISIBLE
        }
        if (age > 0) {
            this.text = "$age"
        }
        if (sex.isNullOrEmpty().not()) {
            this.text = "$sex"
        }
    }
}

@BindingAdapter("userInitials")
fun UserTextView.userInitials(name: String?) {
    if (name.isNullOrEmpty().not()) {
        this.text = name?.first().toString().uppercase()
    }
}

@BindingAdapter("textCapSentences")
fun UserTextView.textCapSentences(text: String?) {
    if (text.isNullOrEmpty().not()) {
        this.text =
            text?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}

@BindingAdapter("currentIndex", "groupOverviewResponse")
fun UserTextView.userName(
    currentIndex: Int,
    groupOverviewResponse: GroupOverviewResponse
) {
    if (groupOverviewResponse.data?.group?.highlights?.topContributors.isNullOrEmpty()
            .not() && (groupOverviewResponse.data?.group?.highlights?.topContributors?.size!! > currentIndex)
    ) {
        if (groupOverviewResponse.data?.group?.highlights?.topContributors.isNullOrEmpty().not()) {
            groupOverviewResponse.data?.group?.highlights?.topContributors?.get(currentIndex)?.let {
                this.text = it.name.toString().replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
        }
    }
}

@BindingAdapter("startDate", "endDate")
fun UserTextView.bindEventDate(startDate: String?, endDate: String?) {
    if (startDate.isNullOrEmpty().not() && endDate.isNullOrEmpty().not()) {
        val inputFormat = "yyyy-MM-dd'T'HH:mm:ssZ"
        try {
            val format = SimpleDateFormat(inputFormat)
            val startTime = Calendar.getInstance()
            startTime.time = format.parse(startDate)
            val endTime = Calendar.getInstance()
            endTime.time = format.parse(endDate)
            val sameDay =
                startTime.get(Calendar.DAY_OF_YEAR) == endTime.get(Calendar.DAY_OF_YEAR) && startTime.get(
                    Calendar.YEAR
                ) == endTime.get(Calendar.YEAR)
            if (sameDay) {
                this.text =
                    "${startDate?.formatDate(inputFormat, "MMM dd, h:mm a")}".replace("am", "AM")
                        .replace("pm", "PM")
            } else {
                this.text = "${startDate?.formatDate(inputFormat, "MMM dd")} - ${
                    endDate?.formatDate(
                        inputFormat,
                        "MMM dd"
                    )
                }"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}

@BindingAdapter("tags")
fun UserTextView.tags(tags: ArrayList<Tags>?) {
    if (tags.isNullOrEmpty().not()) {
        this.text = tags?.joinToString(separator = ", ") { it -> "${it.text}" }
    }
}

@BindingAdapter("groupDiscussionsBadges")
fun UserTextView.groupDiscussionsBadges(badges: ArrayList<Badges>?) {
    if (badges.isNullOrEmpty().not()) {
        this.text = badges?.first()?.badge
    } else {
        this.isVisible = false
    }
}

@BindingAdapter("latestReplyGroupDiscussions")
fun UserTextView.latestReplyGroupDiscussions(latestReply: LatestReply?) {
    latestReply?.let {
        var latestReplyText = ""
        it.time?.let {
            latestReplyText = "$latestReplyText$it • "
        }
        it.user?.name?.let {
            latestReplyText += "<font color='#000000'><b>" + it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } + "</b></font>"
        }
        it.body?.let {
            latestReplyText = latestReplyText + " replied " + Jsoup.parse(it).text()
        }
        setResizableText(latestReplyText, 3)
    }
}

@BindingAdapter("description")
fun UserTextView.description(latestReply: String?) {
    latestReply?.let {
        setResizableText(it, 5)
    }
}

@BindingAdapter("threadDescription")
fun UserTextView.threadDescription(latestReply: String?) {
    movementMethod = LinkMovementMethod.getInstance()
    latestReply?.let {
        val doc: Document = Jsoup.parse(it)
        val images: Elements = doc.getElementsByTag("img")
        for (link in images) {
            link.remove()
        }
        val fullText = doc.html()
        text = HtmlCompat.fromHtml(fullText, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
}

@BindingAdapter("fullText", "maxLines")
fun UserTextView.setResizableText(textStr: String?, maxLines: Int) {

    val doc: Document = Jsoup.parse(textStr)
    val images: Elements = doc.getElementsByTag("img")
    for (link in images) {
        link.remove()
    }
    val fullText = doc.html()
    if (fullText.isNullOrEmpty().not()) {
        val width = width
        if (width <= 0) {
            post {
                setResizableText(fullText, maxLines)
            }
            return
        }
        movementMethod = LinkMovementMethod.getInstance()
        // Since we take the string character by character, we don't want to break up the Windows-style
        // line endings.
        val adjustedText = fullText.replace("\n", "")
        // Check if even the text has to be resizable.
        val textLayout = StaticLayout(
            adjustedText,
            paint,
            width - paddingLeft - paddingRight,
            Layout.Alignment.ALIGN_NORMAL,
            lineSpacingMultiplier,
            lineSpacingExtra,
            includeFontPadding
        )

        if (textLayout.lineCount <= maxLines || adjustedText.isEmpty()) {
            // No need to add 'read more' / 'read less' since the text fits just as well (less than max lines #).
            text = addClickablePartTextResizable(
                HtmlCompat.fromHtml(
                    adjustedText,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ), null
            )
            return
        }
        val charactersAtLineEnd = textLayout.getLineEnd(maxLines - 1)
        val suffixText = "... More"
        var charactersToTake = charactersAtLineEnd - suffixText.length / 2
        if (charactersToTake <= 0) {
            // Happens when text is empty
            val htmlText = adjustedText
            //.replace("\n", "<br/>")
            text = addClickablePartTextResizable(
                HtmlCompat.fromHtml(
                    htmlText,
                    HtmlCompat.FROM_HTML_MODE_COMPACT
                ), null
            )
            return
        }

        val lastHasNewLine = adjustedText.substring(
            textLayout.getLineStart(maxLines - 1),
            textLayout.getLineEnd(maxLines - 1)
        ).contains("\n")
        var linedText = if (lastHasNewLine) {
            val charactersPerLine =
                textLayout.getLineEnd(0) / (textLayout.getLineWidth(0) / textLayout.ellipsizedWidth.toFloat())
            val lineOfSpaces =
                "\u00A0".repeat(charactersPerLine.roundToInt()) // non breaking space, will not be thrown away by HTML parser
            charactersToTake += lineOfSpaces.length - 1
            adjustedText.take(textLayout.getLineStart(maxLines - 1)) +
                    adjustedText.substring(
                        textLayout.getLineStart(maxLines - 1),
                        textLayout.getLineEnd(maxLines - 1)
                    ).replace("\n", lineOfSpaces) +
                    adjustedText.substring(textLayout.getLineEnd(maxLines - 1))
        } else {
            adjustedText
        }
        // Check if we perhaps need to even add characters? Happens very rarely, but can be possible if there was a long word just wrapped
        val shortenedString = linedText.take(charactersToTake)
        val shortenedStringWithSuffix = shortenedString + suffixText
        val shortenedStringWithSuffixLayout =
            StaticLayout(
                shortenedStringWithSuffix,
                paint,
                width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
            )
        val modifier: Int
        if (shortenedStringWithSuffixLayout.getLineEnd(maxLines - 1) >= shortenedStringWithSuffix.length) {
            modifier = 1
            charactersToTake-- // We might just be at the right position already
        } else {
            modifier = -1
        }
        do {
            charactersToTake += modifier
            val baseString = linedText.take(charactersToTake)
            val appended = baseString + suffixText
            val newLayout = StaticLayout(
                appended,
                paint,
                width - paddingLeft - paddingRight,
                Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier,
                lineSpacingExtra,
                includeFontPadding
            )
        } while ((modifier < 0 && newLayout.getLineEnd(maxLines - 1) < appended.length) ||
            (modifier > 0 && newLayout.getLineEnd(maxLines - 1) >= appended.length)
        )
        if (modifier > 0) {
            charactersToTake-- // We went overboard with 1 char, fixing that
        }
        // We need to convert newlines because we are going over to HTML now
        val htmlText = linedText.take(charactersToTake).replace("\n", "<br/>")
        text = addClickablePartTextResizable(
            HtmlCompat.fromHtml(
                htmlText,
                HtmlCompat.FROM_HTML_MODE_COMPACT
            ), suffixText
        )
    }
}

private fun UserTextView.addClickablePartTextResizable(
    shortenedText: Spanned,
    clickableText: String?,
): Spannable {
    val builder = SpannableStringBuilder(shortenedText)
    if (clickableText != null) {
        builder.append(clickableText)
        val startIndexOffset = 4
        builder.setSpan(
            object : NoUnderlineClickSpan(context) {
                override fun onClick(widget: View) {

                }
            },
            builder.indexOf(clickableText) + startIndexOffset,
            builder.indexOf(clickableText) + clickableText.length,
            0
        )
    }

    return builder
}

open class NoUnderlineClickSpan(val context: Context) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = ContextCompat.getColor(context, R.color.blue_darker)
    }

    override fun onClick(widget: View) {}
}

@BindingAdapter("getTodayYesterdayWithTimeFromDate")
fun UserTextView.getTodayYesterdayWithTimeFromDate(date: String) {
    val timeSDF = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val weekdaySDF = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val dateSDF = SimpleDateFormat("MMM dd", Locale.ENGLISH)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

    val dateIs = sdf.parse(date)
    val cal = Calendar.getInstance()
    cal.timeInMillis = dateIs.time
    cal.timeZone = TimeZone.getDefault()

    val currentTime = Calendar.getInstance()

    val diff: Long = currentTime.time.time - cal.time.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    text = if (days.toInt() == 0) {
        timeSDF.format(cal.time)
    } else if (days.toInt() == 1) {
        "Yesterday"
    } else if (days.toInt() in 2..6) {
        weekdaySDF.format(cal.time)
    } else {
        dateSDF.format(cal.time)
    }
}

@BindingAdapter("getTodayYesterdayWithTimeFromDateTime")
fun UserTextView.getTodayYesterdayWithTimeFromDateTime(date: String) {
    val timeSDF = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    val weekdaySDF = SimpleDateFormat("EEEE", Locale.ENGLISH)
    val dateSDF = SimpleDateFormat("MMM dd", Locale.ENGLISH)
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)

    val dateIs = sdf.parse(date)
    val cal = Calendar.getInstance()
    cal.timeInMillis = dateIs.time
    cal.timeZone = TimeZone.getDefault()

    val currentTime = Calendar.getInstance()

    val diff: Long = currentTime.time.time - cal.time.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    text = if (days.toInt() == 0) {
        timeSDF.format(cal.time)
    } else if (days.toInt() == 1) {
        "Yesterday, " + timeSDF.format(cal.time)
    } else if (days.toInt() in 2..6) {
        weekdaySDF.format(cal.time) + ", " + timeSDF.format(cal.time)
    } else {
        dateSDF.format(cal.time) + ", " + timeSDF.format(cal.time)
    }
}

@BindingAdapter("htmlString")
fun UserTextView.setHtmlString(string: String?) {
    string?.let {
        this.text = Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
    }
}

@BindingAdapter("oneStringTagOrBadge")
fun UserTextView.oneStringTagOrBadge(TagOrBadges: ArrayList<String>?) {
    if (TagOrBadges.isNullOrEmpty().not()) {
        this.text = TagOrBadges?.first()
    } else {
        this.isVisible = false
    }
}

@BindingAdapter("allStringTagsOrBadges", "tagPrefix")
fun UserTextView.badges(badges: ArrayList<String>?, tagPrefix: Boolean = false) {
    if (badges.isNullOrEmpty().not()) {
        this.text = if (tagPrefix) {
            this.context.getString(R.string.tags_colon) + " " + badges?.joinToString(separator = ", ")
        } else {
            badges?.joinToString(separator = ", ")
        }
    } else {
        this.isVisible = false
    }
}


fun UserTextView.setClickableTagsOrBadges(
    tags: ArrayList<Tags>?,
    tagPrefix: Boolean = false,
    tagClicked: (tag: String) -> Unit
) {
    movementMethod = LinkMovementMethod.getInstance()
    if (tags.isNullOrEmpty().not()) {
        val builder = SpannableStringBuilder()
        val wholeValue = if (tagPrefix) {
            this.context.getString(R.string.tags_colon) + " " + tags?.joinToString(separator = ", ") { it -> "${it.text}" }
        } else {
            tags?.joinToString(separator = ", ")
        }
        wholeValue?.let {
            builder.append(wholeValue)
            for (tag in tags!!) {
                val span = ColoredClickableSpan(
                    context,
                    ContextCompat.getColor(context, R.color.blue_darker),
                    tag.text ?: "",
                    tagClicked
                )
                val startIndex = wholeValue.indexOf(tag.text ?: "")
                val endIndex = wholeValue.indexOf(tag.text ?: "") + (tag.text ?: "").length
                builder.setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            text = builder
        }
    }
}

open class ColoredClickableSpan(
    val context: Context,
    val color: Int,
    val str: String,
    val tagClicked: (tag: String) -> Unit
) : ClickableSpan() {
    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = false
        textPaint.color = color
    }

    override fun onClick(widget: View) {
        tagClicked(str)
    }
}

@BindingAdapter("setTextHighlight")
fun UserTextView.setTextHighlight(fullText: String, searchText: String?) {
    val span: Spanned?
    if (searchText != null && searchText.isNotEmpty()) {
        val startPos: Int =
            fullText.toLowerCase(Locale.US).indexOf(searchText.toLowerCase(Locale.US))
        val endPos: Int = startPos + searchText.length
        span = if (startPos != -1) {
            val spannable: Spannable = SpannableString(fullText)
            spannable.setSpan(
                BackgroundColorSpan(Color.parseColor("#FEE019")),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannable
        } else {
            HtmlCompat.fromHtml(fullText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    } else {
        span = HtmlCompat.fromHtml(fullText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
    text = span
}

@BindingAdapter("setContentToWebView")
fun WebView.setContentToWebView(html_body: String) {
    val replyText =
        ("<html><head><style img{display: inline;height: auto;max-width: 100%;} type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Poppins-Regular.ttf\")}body{font-family: MyFont;color: #071414}"
                + "</style></head><body>" + html_body + "</body></html>")
    loadDataWithBaseURL(null, replyText, "text/html", "UTF-8", null)
}