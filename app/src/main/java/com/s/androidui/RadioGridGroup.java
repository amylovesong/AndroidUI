package com.s.androidui;

/**
 * 在{@link android.widget.RadioGroup}的基础上将{@link RadioButton}的布局方式改为多列，通过{@link Adapter}来提供数据
 *
 * <p>This class is used to create a multiple-exclusion scope for a set of radio
 * buttons. Checking one radio button that belongs to a radio group unchecks
 * any previously checked radio button within the same group.</p>
 *
 * <p>Intially, all of the radio buttons are unchecked. While it is not possible
 * to uncheck a particular radio button, the radio group can be cleared to
 * remove the checked state.</p>
 *
 * <p>The selection is identified by the unique id of the radio button as defined
 * in the XML layout file.</p>
 *
 * @see RadioButton
 *
 */
public class RadioGridGroup extends RecyclerView {

    public static abstract class Adapter extends RecyclerView.Adapter<RadioButtonViewHolder> {
        private Context mContext;

        public Adapter(Context context) {
            this.mContext = context;
        }

        @Override
        public final RadioButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final RadioButton radioButton = new RadioButton(mContext);
            radioButton.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    mContext.getResources().getDimensionPixelSize(R.dimen.40dp)));
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setButtonDrawable(R.color.transparent);
            radioButton.setBackgroundResource(R.drawable.radio_button_selector);
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            radioButton.setTextColor(mContext.getResources().getColorStateList(R.color.radio_button_text_color_selector));

            return new RadioButtonViewHolder(radioButton);
        }

        @Override
        public final void onBindViewHolder(RadioButtonViewHolder holder, int position) {
            bindData(holder.mRadioButton, position);
        }

        protected abstract void bindData(RadioButton radioButton, int position);

        /*@Override
        public int getItemCount() {
            return 5;
        }*/
    }

    static class RadioButtonViewHolder extends RecyclerView.ViewHolder {
        private RadioButton mRadioButton;

        RadioButtonViewHolder(View itemView) {
            super(itemView);
            if (itemView instanceof RadioButton) {
                this.mRadioButton = (RadioButton) itemView;
            }
        }
    }

    private static final int SPAN_COUNT = 2;
    private int dividerWidth = 0;

    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public RadioGridGroup(Context context) {
        super(context);
        init();
    }

    public RadioGridGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dividerWidth = getResources().getDimensionPixelSize(R.dimen.10dp);

        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);

        setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
//        setAdapter(new Adapter(getContext()));
    }

    public final void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public final void setAdapter(RecyclerView.Adapter adapter) {
        if (!(adapter instanceof Adapter)) {
            throw new RuntimeException("RadioGridGroup only support " + Adapter.class.getName());
        }
        super.setAdapter(adapter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }

            final LayoutParams lp = (LayoutParams) button.getLayoutParams();
            lp.topMargin = dividerWidth;
            if ((index + 1) % SPAN_COUNT != 0) {
                lp.rightMargin = dividerWidth;
            }
        }

        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     *
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        /*if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }*/
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, (RadioButton) this.findViewById(mCheckedId));
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     *
     * @see #check(int)
     * @see #clearCheck()
     *
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     */
    @IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return RadioGridGroup.class.getName();
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
//        /**
//         * <p>Called when the checked radio button has changed. When the
//         * selection is cleared, checkedId is -1.</p>
//         *
//         * @param group the group in which the checked radio button has changed
//         * @param checkedId the unique identifier of the newly checked radio button
//         */
//        public void onCheckedChanged(RadioGridGroup group, @IdRes int checkedId);

        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedRadioButton is null.</p>
         *
         * @param group the group in which the checked radio button has changed
         * @param checkedRadioButton the newly checked radio button
         */
        public void onCheckedChanged(RadioGridGroup group, @Nullable RadioButton checkedRadioButton);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioGridGroup.this && child instanceof RadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((RadioButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioGridGroup.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
