package com.github.agadar.famtreestats.misc;

import java.util.regex.Pattern;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Custom document filter that enforces a regex.
 *
 * @author Agadar <https://github.com/Agadar/>
 */
public class RegexDocumentFilter extends DocumentFilter
{
    private final Pattern Regex;

    public RegexDocumentFilter(String pattern)
    {
        this.Regex = Pattern.compile(pattern);
    }

    @Override
    public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
            String text, AttributeSet attrs) throws BadLocationException
    {
        String str = fb.getDocument().getText(0, fb.getDocument().getLength());
        str += text;

        if (Regex.matcher(str).matches())
        {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
