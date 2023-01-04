package plm.patientslikeme2.utils.usercontrol.togglebutton

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import plm.patientslikeme2.R
import plm.patientslikeme2.utils.usercontrol.UserButton
import plm.patientslikeme2.utils.usercontrol.UserToggleButton

class MultiStateToggleButton : UserToggleButton {

    private var itemList: ArrayList<PLMToggleButtonData> = arrayListOf()
    private var clickListener: OnClickListener? = null

    private var isToggleButton: Boolean = false

    /**
     * A list of rendered buttons. Used to get state, among others
     */
    private var buttons: MutableList<View>? = null
    /**
     * @return An array of the buttons' text
     */
    /**
     * The specified texts
     */
    var texts: ArrayList<PLMToggleButtonData> = arrayListOf()

    /**
     * If true, multiple buttons can be pressed at the same time
     */
    private var mMultipleChoice = false

    /**
     * The layout containing all buttons
     */
    private var mainLayout: LinearLayout? = null

    private var selectedItem: Int = -1
    private var textLines: Int = 1

    constructor(context: Context?) : super(context!!, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MultiStateToggleButton, 0, 0)
        try {
            colorPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbPrimaryColor, 0)
            colorNotPressed = a.getColor(R.styleable.MultiStateToggleButton_mstbSecondaryColor, 0)
            colorPressedText =
                a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedText, 0)
            colorPressedBackground =
                a.getColor(R.styleable.MultiStateToggleButton_mstbColorPressedBackground, 0)
            pressedBackgroundResource = a.getResourceId(
                R.styleable.MultiStateToggleButton_mstbColorPressedBackgroundResource, 0
            )
            colorNotPressedText =
                a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedText, 0)
            colorNotPressedBackground =
                a.getColor(R.styleable.MultiStateToggleButton_mstbColorNotPressedBackground, 0)
            notPressedBackgroundResource = a.getResourceId(
                R.styleable.MultiStateToggleButton_mstbColorNotPressedBackgroundResource, 0
            )
            textLines = a.getInteger(R.styleable.MultiStateToggleButton_textlines, 1)
            isToggleButton = a.getBoolean(R.styleable.MultiStateToggleButton_isToggle, false)
        } finally {
            a.recycle()
        }
    }

    /**
     * If multiple choice is enabled, the user can select multiple
     * values simultaneously.
     *
     * @param enable
     */
    fun enableMultipleChoice(enable: Boolean) {
        mMultipleChoice = enable
    }

    public override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putBooleanArray(KEY_BUTTON_STATES, states)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        var state: Parcelable? = state
        if (state is Bundle) {
            val bundle = state
            states = bundle.getBooleanArray(KEY_BUTTON_STATES)
            state = bundle.getParcelable(KEY_INSTANCE_STATE)
        }
        super.onRestoreInstanceState(state)
    }

    /**
     * Set the enabled state of this MultiStateToggleButton, including all of its child buttons.
     *
     * @param enabled True if this view is enabled, false otherwise.
     */
    override fun setEnabled(enabled: Boolean) {
        for (i in 0 until childCount) {
            val child: View = getChildAt(i)
            child.isEnabled = enabled
        }
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param texts            An array of CharSequences for the buttons
     * @param imageResourceIds an optional icon to show, either text, icon or both needs to be set.
     * @param selected         The default value for the buttons
     */
    private fun setElements(
        texts: ArrayList<PLMToggleButtonData>,
    ) {
        this.texts = texts
        val textCount = texts.size
        if (textCount == 0) {
            return
        }

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) {
            mainLayout = inflater.inflate(
                R.layout.view_multi_state_toggle_button, this, true
            ) as LinearLayout?
        }
        mainLayout?.removeAllViews()
        buttons = ArrayList(textCount)

        for (i in 0 until textCount) {
            val b: UserButton = inflater.inflate(
                R.layout.view_center_toggle_button, mainLayout, false
            ) as UserButton
            b.hint = i.toString()
            if (i == 0) {
                // Add a special view when there's only one element
                if (textCount == 1) {
                    b.background = ContextCompat.getDrawable(context, R.drawable.ic_rect_toggle)
                } else {
                    b.background = ContextCompat.getDrawable(context, R.drawable.rounded_rect_left)
                }
            } else if (i == textCount - 1) {
                b.background = ContextCompat.getDrawable(context, R.drawable.rounded_rect_right)
            } else {
                b.background = ContextCompat.getDrawable(context, R.drawable.ic_rect_toggle)
            }
            b.text = texts[i].item.toString()
            b.setLines(textLines)
            b.setIsToggleButton(isToggleButton)

            if (selectedItem == (i + 1)) {
                setButtonState(b, true)
            }
            b.setOnClickListener {
                selectedItem = i + 1
                setValue(i)
                clickListener?.onClick(it)
            }
            mainLayout?.addView(b)
            buttons?.add(b)
        }

    }

    fun setButtonOnClickListener(listener: OnClickListener) {
        clickListener = listener
    }

    /**
     * Set multiple buttons with the specified texts and default
     * initial values. Initial states are allowed, but both
     * arrays must be of the same size.
     *
     * @param buttons  the array of button views to use
     * @param selected The default value for the buttons
     */
    fun setButtons(buttons: Array<View>, selected: BooleanArray?) {
        val elementCount = buttons.size
        if (elementCount == 0) {
            return
        }
        var enableDefaultSelection = true
        if (selected == null || elementCount != selected.size) {
            Log.d(TAG, "Invalid selection array")
            enableDefaultSelection = false
        }
        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mainLayout == null) {
            mainLayout = inflater.inflate(
                R.layout.view_multi_state_toggle_button, this, true
            ) as LinearLayout?
        }
        mainLayout?.removeAllViews()
        this.buttons = ArrayList()
        for (i in 0 until elementCount) {
            val b: View = buttons[i]
            b.setOnClickListener { setValue(i) }
            mainLayout?.addView(b)
            if (enableDefaultSelection) {
                setButtonState(b, selected!![i])
            }
            this.buttons?.add(b)
        }
    }

//    fun setItems(list: ArrayList<*>, displayBy: String = "") {
//        itemList.clear()
//        for (i in list.indices) {
//            val item = list[i]
//            if (item is String) {
//                itemList.add(PLMListDataClass(item, i))
//            } else {
//                if (!displayBy.isNullOrEmpty()) {
//                    var field = item::class.java.getDeclaredField(displayBy)
//                    if (field != null) {
//                        field.isAccessible = true
//                        var propertyValue = field.get(item)
//                        if (propertyValue != null)
//                            itemList.add(PLMListDataClass(propertyValue.toString(), i))
//                    }
//                }
//            }
//        }
//        if (itemList.size > 0) {
//            setElements(itemList)
//        }
//    }

    fun setItems(list: ArrayList<PLMToggleButtonData>) {
        itemList = list
        if (itemList.size > 0) {
            setElements(itemList)
        }
    }

    fun clearItems() {
        itemList.clear()
    }

//    fun setStringItems(list: ArrayList<String>) {
//        itemList.clear()
//        for (i in list.indices) {
//            val item = list[i]
//            itemList.add(PLMListDataClass(item, i))
//        }
//        if (itemList.size > 0) {
//            setElements(itemList)
//        }
//    }

    fun setItemWithSelection(position: Int, list: ArrayList<PLMToggleButtonData>) {
        itemList = list
        selectedItem = position
        if (itemList.size > 0) {
            setElements(itemList)
        }
    }

    private fun setButtonState(button: View?, selected: Boolean) {
        if (button == null) {
            return
        }
        button.isSelected = selected
        val drawable = (button as AppCompatButton).background
        if (selected) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                drawable?.mutate()?.colorFilter = BlendModeColorFilter(
                    itemList[button.hint.toString().toInt()].Color, BlendMode.SRC_ATOP
                )
            } else {
                drawable?.mutate()?.setColorFilter(
                    itemList[button.hint.toString().toInt()].Color, PorterDuff.Mode.SRC_ATOP
                )
            }
        } else {
            drawable?.clearColorFilter()
        }
        button.background = drawable
    }

    fun getValue(): Int {
        for (i in buttons!!.indices) {
            if (buttons!![i].isSelected) {
                return i
            }
        }
        return -1
    }

    override fun setValue(position: Int) {
        for (i in buttons!!.indices) {
            if (mMultipleChoice) {
                if (i == position) {
                    val b: View = buttons!![i]
                    setButtonState(b, !b.isSelected)
                }
            } else {
                if (i == position) {
                    if (buttons?.get(i)?.isSelected == true) {
                        selectedItem = -1
                        setButtonState(buttons!![i], false)
                    } else {
                        setButtonState(buttons!![i], true)
                    }
                } else if (!mMultipleChoice) {
                    setButtonState(buttons!![i], false)
                }
            }
        }
        super.setValue(position)
    }

    private var states: BooleanArray?
        get() {
            val size = if (buttons == null) 0 else buttons!!.size
            val result = BooleanArray(size)
            for (i in 0 until size) {
                result[i] = buttons!![i].isSelected
            }
            return result
        }
        set(selected) {
            if (buttons == null || selected == null || buttons?.size != selected.size) {
                return
            }
            for ((count, b) in buttons!!.withIndex()) {
                setButtonState(b, selected[count])
            }
        }

    /**
     * {@inheritDoc}
     */
    override fun setColors(colorPressed: Int, colorNotPressed: Int) {
        super.setColors(colorPressed, colorNotPressed)
        refresh()
    }

    private fun refresh() {
        val states = states!!
        for (i in states.indices) {
            setButtonState(buttons!![i], states[i])
        }
    }

    companion object {
        private val TAG = MultiStateToggleButton::class.java.simpleName
        private const val KEY_BUTTON_STATES = "button_states"
        private const val KEY_INSTANCE_STATE = "instance_state"
    }
}

data class PLMToggleButtonData(var item: SEVERITY, var Color: Int)
enum class SEVERITY {
    None, Mild, Moderate, Severe
}