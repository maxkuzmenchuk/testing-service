package com.kzumenchuk.testingservice.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;

public class PDFFonts {
    public static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLDITALIC, BaseColor.BLACK);
    public static final Font INFO_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
    public static final Font TEXT_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.NORMAL, BaseColor.BLACK);
    public static final Font QUESTION_DESCRIPTION_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

}
