package com.jdd.android.jdd.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.jdd.android.jdd.R;
import com.thinkive.android.app_engine.utils.ScreenUtils;

public class SideBar extends View {
    // 26����ĸ
    public static String[] sCharacters = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    // �����¼�
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int mChoose = -1;// ѡ��
    private Paint mPaint = new Paint();
    private TextView mTextDialog;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**
     * ��д�������
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // ��ȡ����ı䱳����ɫ.
        int height = getHeight();// ��ȡ��Ӧ�߶�
        int width = getWidth(); // ��ȡ��Ӧ���
        int singleHeight = height / sCharacters.length;// ��ȡÿһ����ĸ�ĸ߶�

        for (int i = 0; i < sCharacters.length; i++) {
            mPaint.setColor(getResources().getColor(R.color.text_gray));
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(ScreenUtils.dpToPx(getContext(), 16));
            // ѡ�е�״̬
            if (i == mChoose) {
                mPaint.setColor(getResources().getColor(R.color.text_black));
                mPaint.setFakeBoldText(true);
            }
            // x��������м�-�ַ�����ȵ�һ��.
            float xPos = width / 2 - mPaint.measureText(sCharacters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(sCharacters[i], xPos, yPos, mPaint);
            mPaint.reset();// ���û���
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// ���y����
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * sCharacters.length);// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                mChoose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
//			setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < sCharacters.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(sCharacters[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(sCharacters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        mChoose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * ���⹫���ķ���
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * �ӿ�
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}