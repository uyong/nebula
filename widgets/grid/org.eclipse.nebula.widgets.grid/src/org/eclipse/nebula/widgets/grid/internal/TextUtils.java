/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    chris.gross@us.ibm.com - initial API and implementation
 *******************************************************************************/ 
package org.eclipse.nebula.widgets.grid.internal;

import org.eclipse.swt.graphics.GC;

/**
 * Utility class to provide common operations on strings not supported by the
 * base java API.
 * 
 * @author chris.gross@us.ibm.com
 * @author Mirko Paturzo <mirko.paturzo@exeura.eu>
 * @author gongguangyong@live.cn
 *
 * Mirko modified the pivot calculation for improve short text provider performance. 
 * The pivot number is calculate starting from the size of the cell provided
 * 
 * @since 2.0.0
 */
public class TextUtils
{

    /**
     * Shortens a supplied string so that it fits within the area specified by
     * the width argument. Strings that have been shorted have an "..." attached
     * to the end of the string. The width is computed using the
     * {@link GC#textExtent(String)}.
     * 
     * @param gc GC used to perform calculation.
     * @param t text to modify.
     * @param width Pixels to display.
     * @return shortened string that fits in area specified.
     * @deprecated when text is large, performance is poor, possible occur OOM exception. suggest use {@link #getShortStr(GC, String, int)}
     */
    @Deprecated
    public static String getShortText(GC gc, String t, int width)
    {
        if (t == null)
        {
            return null;
        }

        if (t.equals(""))
        {
            return "";
        }

        if (width >= gc.textExtent(t).x)
        {
            return t;
        }

        int w = gc.textExtent("...").x;
        String text = t;
        int l = text.length();
        int pivot = l / 2;
        int s = pivot;
        int e = pivot + 1;

        while (s >= 0 && e < l)
        {
            String s1 = text.substring(0, s);
            String s2 = text.substring(e, l);
            int l1 = gc.textExtent(s1).x;
            int l2 = gc.textExtent(s2).x;
            if (l1 + w + l2 < width)
            {
                text = s1 + "..." + s2;
                break;
            }
            s--;
            e++;
        }

        if (s == 0 || e == l)
        {
            text = text.substring(0, 1) + "..." + text.substring(l - 1, l);
        }

        return text;
    }
    
    /**
     * Shortens a supplied string so that it fits within the area specified by
     * the width argument. Strings that have been shorted have an "..." attached
     * to the end of the string. The width is computed using the
     * {@link GC#stringExtent(String)}.
     * 
     * @param gc GC used to perform calculation.
     * @param t text to modify.
     * @param width Pixels to display.
     * @return shortened string that fits in area specified.
     * @deprecated when text is large, performance is poor, possible occur OOM exception. suggest use {@link #getShortStr(GC, String, int)}
     */
    @Deprecated
    public static String getShortString(GC gc, String t, int width)
    {
        if (t == null)
        {
            return null;
        }

        if (t.equals(""))
        {
            return "";
        }

        int textWidth = gc.stringExtent(t).x;
	if (width >= textWidth)
        {
            return t;
        }
	String text = t;
	int l = text.length();
	int w = gc.stringExtent("...").x;
	double midChar = (double)textWidth / l;
	int pivot = (int) ((width / midChar) / 2);
        int s = pivot;
        int e = l - pivot + 1;

        while (s >= 0 && e < l)
        {
            String s1 = text.substring(0, s);
            String s2 = text.substring(e, l);
            int l1 = gc.stringExtent(s1).x;
            int l2 = gc.stringExtent(s2).x;
            if (l1 + w + l2 < width)
            {
                text = s1 + "..." + s2;
                break;
            }
            s--;
            e++;
        }

        if (s == 0 || e == l)
        {
            text = text.substring(0, 1) + "..." + text.substring(l - 1, l);
        }

        return text;
    }
    
    /**
     * Shortens a supplied string so that it fits within the area specified by
     * the width argument. Strings that have been shorted have an "..." attached
     * to the end of the string. The width is computed using the
     * {@link GC#getCharWidth(char)}.
     * 
     * @param gc GC used to perform calculation.
     * @param t text to modify.
     * @param width Pixels to display.
     * @return shortened string that fits in area specified.
     */
    public static String getShortStr(GC gc, String t, int width)
    {
        if (t == null || t.equals("")) {
	    return t;
	}

	if (width >= gc.stringExtent(t).x) {
	    return t;
	}
	
	char[] chars = t.toCharArray();
	int length = chars.length;
	int left = 0;
	int right = length - 1;
	int calcWidth = gc.stringExtent("...").x;

	while (left < right) {
	    int step = gc.getCharWidth(chars[left]);
	    calcWidth += step;
	    if(calcWidth >= width) {
	        break;
	    }
	    left++;
			
	    step = gc.getCharWidth(chars[right]);
	    calcWidth += step;
	    if (calcWidth >= width) {
	        break;
	    }
	    right--;
	}
	if (left >= right) {
	    return t;
	}
	StringBuilder builder = new StringBuilder(left + length - right + 4);
	if(left == 0 || right == length - 1) {
	    builder.append(chars[0]).append("...").append(chars[length-1]);
	} else {
	    int leftLen = left == 1 ? left : left - 1;
	    builder.append(chars, 0, leftLen).append("...").append(chars, right+1, length - right - 1);
	}
	return builder.toString();
    }

    /**
     * private constructor to prevent instantiation.
     */
    private TextUtils()
    {
    	//is empty
    }
}
