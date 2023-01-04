package plm.patientslikeme2.utils.usercontrol

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import java.net.URLEncoder

class HtmlEditor : WebView {
    private val HTML_SCHEME = "file:///android_asset/html-editor/editor.html"
    private val CALLBACK_SCHEME = "re-callback://"
    private val STATE_SCHEME = "re-state://"

    enum class Type {
        BOLD, ITALIC, UNDERLINE, ORDEREDLIST, UNORDEREDLIST
    }

    private var isReady = false
    private var mContents: String? = null
    var mHTMLUpdatesListener: HTMLUpdatesListener? = null

    interface HTMLUpdatesListener {
        fun onAfterInitialLoad(isReady: Boolean)
        fun onTextChange(text: String?)
        fun onStateChangeListener(text: String?, types: List<Type?>?)
    }

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context,
        attrs,
        defStyle) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initView() {
        isVerticalScrollBarEnabled = false
        isHorizontalScrollBarEnabled = false
        settings.javaScriptEnabled = true
        webChromeClient = WebChromeClient()
        webViewClient = EditorWebViewClient()
        loadUrl(HTML_SCHEME)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        requestDisallowInterceptTouchEvent(true)
        return super.onTouchEvent(event)
    }

    private inner class EditorWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView, url: String) {
            isReady = url.equals(HTML_SCHEME, ignoreCase = true)
            mHTMLUpdatesListener?.onAfterInitialLoad(isReady)
            if (isReady) {
                exec("javascript:RE.setPadding('" + 10 + "px', '" + 10 + "px', '" + 10 + "px', '" + 10 + "px');")
                exec("javascript:RE.setBaseTextColor('#071414');")
            }
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val decode = Uri.decode(url)
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            val decode = Uri.decode(url)
            if (TextUtils.indexOf(url, CALLBACK_SCHEME) == 0) {
                callback(decode)
                return true
            } else if (TextUtils.indexOf(url, STATE_SCHEME) == 0) {
                stateCheck(decode)
                return true
            }
            return super.shouldOverrideUrlLoading(view, request)
        }
    }

    private fun callback(text: String) {
        mContents = text.replaceFirst(CALLBACK_SCHEME.toRegex(), "")
        mHTMLUpdatesListener?.onTextChange(mContents)
    }

    private fun stateCheck(text: String) {
        val state: String = text.replaceFirst(STATE_SCHEME.toRegex(), "").uppercase()
        val types: MutableList<Type> = ArrayList()
        for (type in Type.values()) {
            if (TextUtils.indexOf(state, type.name) != -1) {
                types.add(type)
            }
        }
        mHTMLUpdatesListener?.onStateChangeListener(state, types)
    }

    private fun exec(trigger: String) {
        if (isReady) {
            evaluateJavascript(trigger, null)
        } else {
            postDelayed({ exec(trigger) }, 1000)
        }
    }

    fun setHtml(contents: String) {
        try {
            exec("javascript:RE.setHtml('" + URLEncoder.encode(contents, "UTF-8") + "')")
            mContents = contents
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getHtml(): String? {
        return mContents
    }

    fun setBold() {
        exec("javascript:RE.setBold()")
    }

    fun setPlaceholder(placeholder: String) {
        exec("javascript:RE.setPlaceholder('$placeholder');")
    }

    fun setItalic() {
        exec("javascript:RE.setItalic()")
    }

    fun setUnderline() {
        exec("javascript:RE.setUnderline()")
    }

    fun setBullets() {
        exec("javascript:RE.setBullets()")
    }

    fun setNumbers() {
        exec("javascript:RE.setNumbers()")
    }

    fun insertImage(url: String, alt: String, width: Int? = 0, height: Int? = 0) {
        exec("javascript:RE.prepareInsert()")
        if (width != null && width > 0 && height != null && height > 0) {
            exec("javascript:RE.insertImageWH('$url', '$alt','$width', '$height')")
        } else {
            exec("javascript:RE.insertImage('$url', '$alt')")
        }
    }

    fun insertLink(href: String, title: String) {
        exec("javascript:RE.prepareInsert()")
        exec("javascript:RE.insertLink('$href', '$title')")
    }

    fun focusEditor() {
        requestFocus()
        exec("javascript:RE.focus();")
    }
}

