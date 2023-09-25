// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.intellij.sdk.toolWindow;

import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.JBColor;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Objects;

public class CalendarToolWindowFactory implements ToolWindowFactory, DumbAware {

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    CalendarToolWindowContent toolWindowContent = new CalendarToolWindowContent(toolWindow);
    Content content = ContentFactory.getInstance().createContent(toolWindowContent.getContentPanel(), "", false);
    toolWindow.getContentManager().addContent(content);
  }

  private static class CalendarToolWindowContent {

    private static final String CALENDAR_ICON_PATH = "/toolWindow/Calendar-icon.png";
    private static final String TIME_ZONE_ICON_PATH = "/toolWindow/Time-zone-icon.png";
    private static final String TIME_ICON_PATH = "/toolWindow/Time-icon.png";

    private final JPanel contentPanel = new JPanel();
    private final JLabel currentDate = new JLabel();
    private final JLabel timeZone = new JLabel();
    private final JLabel currentTime = new JLabel();

    private final ClonesPanel clonesPanel = new ClonesPanel();

    public CalendarToolWindowContent(ToolWindow toolWindow) {
      contentPanel.setLayout(new BorderLayout(0, 20));
      contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      contentPanel.add(clonesPanel, BorderLayout.PAGE_START);
      //contentPanel.add(createCalendarPanel(toolWindow), BorderLayout.PAGE_START);
      //contentPanel.add(createControlsPanel(toolWindow), BorderLayout.CENTER);
      //updateCurrentDateTime();
    }

    @NotNull
    private JPanel createCalendarPanel(ToolWindow toolWindow) {
      JPanel calendarPanel = new JPanel();

      //setIconLabel(currentDate, CALENDAR_ICON_PATH);
      //setIconLabel(timeZone, TIME_ZONE_ICON_PATH);
      //setIconLabel(currentTime, TIME_ICON_PATH);
      //calendarPanel.add(currentDate);
      //calendarPanel.add(timeZone);
      //calendarPanel.add(currentTime);
      calendarPanel.add(clonesPanel);
      return calendarPanel;
    }

    private void setIconLabel(JLabel label, String imagePath) {
      label.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))));
    }

    @NotNull
    private JPanel createControlsPanel(ToolWindow toolWindow) {
      JPanel controlsPanel = new JPanel();
      JButton refreshDateAndTimeButton = new JButton("Refresh");
      refreshDateAndTimeButton.addActionListener(e -> updateCurrentDateTime());
      controlsPanel.add(refreshDateAndTimeButton);
      JButton hideToolWindowButton = new JButton("Hide");
      hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
      controlsPanel.add(hideToolWindowButton);
      return controlsPanel;
    }

    private void updateCurrentDateTime() {
      Calendar calendar = Calendar.getInstance();
      currentDate.setText(getCurrentDate(calendar));
      timeZone.setText(getTimeZone(calendar));
      currentTime.setText(getCurrentTime(calendar));
    }

    private String getCurrentDate(Calendar calendar) {
      return calendar.get(Calendar.DAY_OF_MONTH) + "/"
          + (calendar.get(Calendar.MONTH) + 1) + "/"
          + calendar.get(Calendar.YEAR);
    }

    private String getTimeZone(Calendar calendar) {
      long gmtOffset = calendar.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
      String gmtOffsetString = String.valueOf(gmtOffset / 3600000);
      return (gmtOffset > 0) ? "GMT + " + gmtOffsetString : "GMT - " + gmtOffsetString;
    }

    private String getCurrentTime(Calendar calendar) {
      return getFormattedValue(calendar, Calendar.HOUR_OF_DAY) + ":" + getFormattedValue(calendar, Calendar.MINUTE);
    }

    private String getFormattedValue(Calendar calendar, int calendarField) {
      int value = calendar.get(calendarField);
      return StringUtils.leftPad(Integer.toString(value), 2, "0");
    }

    public JPanel getContentPanel() {
      return contentPanel;
    }

  }
}





class ClonesPanel extends JPanel {

  public ClonesPanel() {
    super();
    //this.setPreferredSize(new Dimension(800, 500));
    setOpaque(false);
  }

  @Override
  protected void paintComponent(Graphics g) {
    /*
    Graphics2D g2 = (Graphics2D)g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    g2.setStroke(new BasicStroke(0.1F));
    g2.setColor(JBColor.GREEN);

    CubicCurve2D c = new CubicCurve2D.Float();
    c.setCurve(0, 0, 1, 1, 10, 10, 9, 9);
//    c.setCurve(400.0 - 1.0/2.0, 1.0*4.0, 400.0 - 1.0/2.0, 1.0*8.0,
  //          500, 1.0*8.0, 500, 1.0*11.5);

    g2.setStroke(new BasicStroke(1));
    g2.draw(c);
*/

    Graphics2D g2 = (Graphics2D) g;
    this.setSize(new Dimension(this.getParent().getSize()));
    this.setLocation(new Point(0, 0));
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    Color color = this.getParent().getBackground();
    //GradientPaint gradientPaint = new GradientPaint(0, 0, Color.decode("#1CB5E0"), 0, getHeight(), Color.decode("#000046"));
    //g2.setPaint(gradientPaint);
    g2.setColor(color.darker());
    //g2.setClip(null);
    g2.fillRect(0, 0, getWidth(), getHeight());
    //g2.fillRoundRect(3, 3, getWidth()-6, getHeight()-6, 15, 15);
    //g2.fillRect(getWidth() - 20, 3, 17, getHeight()-6);
    super.paintComponent(g);
  }

}

