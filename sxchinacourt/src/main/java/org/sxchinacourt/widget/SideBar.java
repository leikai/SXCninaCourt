package org.sxchinacourt.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBar extends View {
    private char[] l;
    private SectionIndexer sectionIndexter = null;
    private ListView list;
    private TextView mDialogText;
    private int m_nItemHeight;
    private int m_nTextSize;
    private int mTouchSlop;
    private int mStartY;

    public SideBar(Context context) {
        super(context);
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        l = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z'};
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListView(ListView _list) {
        list = _list;
        if (_list.getAdapter() instanceof HeaderViewListAdapter) {
            sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter) _list.getAdapter()).getWrappedAdapter();
        } else {
            sectionIndexter = (SectionIndexer) _list.getAdapter();
        }
    }

    public void setTextView(TextView mDialogText) {
        this.mDialogText = mDialogText;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        m_nItemHeight =   l.length * 2;//getHeight()/
        m_nTextSize = m_nItemHeight * 3 / 4;
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int i = 0;
        int idx = 0;
        boolean moved = false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            i = (int) event.getY();
            mStartY = i;
            idx = i / m_nItemHeight;
            if (idx >= l.length) {
                idx = l.length - 1;
            } else if (idx < 0) {
                idx = 0;
            }
            moved = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int endY = (int) event.getY();
            if (Math.abs(endY - mStartY) > mTouchSlop) {
                i = getHeight() - (int) event.getY();
                idx = i / m_nItemHeight;
                if (idx >= l.length) {
                    idx = l.length - 1;
                } else if (idx < 0) {
                    idx = 0;
                }
                mDialogText.setVisibility(View.VISIBLE);
                mDialogText.setText("" + l[idx]);
                moved = true;
            }
        } else {
            mDialogText.setVisibility(View.INVISIBLE);
        }
        if (moved) {
            if (sectionIndexter == null) {
                if (list.getAdapter() instanceof HeaderViewListAdapter) {
                    sectionIndexter = (SectionIndexer) ((HeaderViewListAdapter) list.getAdapter())
                            .getWrappedAdapter();
                } else {
                    sectionIndexter = (SectionIndexer) list.getAdapter();
                }
            }
            int position = sectionIndexter.getPositionForSection(l[idx]);
            if (position == -1) {
                return true;
            }
            list.setSelection(position);
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(0xff000000);
        paint.setTextSize(m_nTextSize);
        paint.setTextAlign(Paint.Align.CENTER);
        float widthCenter = getMeasuredWidth() / 2;
        for (int i = 0; i < l.length; i++) {
            canvas.drawText(String.valueOf(l[i]), widthCenter, m_nItemHeight
                    + (i * m_nItemHeight), paint);
        }
        super.onDraw(canvas);
    }
}
