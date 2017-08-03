package com.acescripts.scripts.overloadaio.framework;

/**
 * Created by Transporter on 18/11/2016 at 18:41.
 */

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;

import java.awt.*;

public class ProgressBar {
    private Script s;
    private Graphics g;
    private Skill skill;
    private boolean frame;
    private int x, y, width, height;
    private Color red = new Color(255, 0, 0, 150);
    private Color green = new Color(0, 255, 0, 150);

    int[] XP = { 0, 83, 174, 276, 388, 512, 650, 801, 969, 1154, 1358, 1584,
            1833, 2107, 2411, 2746, 3115, 3523, 3973, 4470, 5018, 5624, 6291,
            7028, 7842, 8740, 9730, 10824, 12031, 13363, 14833, 16456, 18247,
            20224, 22406, 24815, 27473, 30408, 33648, 37224, 41171, 45529,
            50339, 55649, 61512, 67983, 75127, 83014, 91721, 101333, 111945,
            123660, 136594, 150872, 166636, 184040, 203254, 224466, 247886,
            273742, 302288, 333804, 368599, 407015, 449428, 496254, 547953,
            605032, 668051, 737627, 814445, 899257, 992895, 1096278, 1210421,
            1336443, 1475581, 1629200, 1798808, 1986068, 2192818, 2421087,
            2673114, 2951373, 3258594, 3597792, 3972294, 4385776, 4842295,
            5346332, 5902831, 6517253, 7195629, 7944614, 8771558, 9684577,
            10692629, 11805606, 13034431, 200000000 };

    public ProgressBar(Script script, Graphics graphics, String name,
                       Skill skill, boolean frame, int transparency, Font font, int x,
                       int y, int width, int height) {
        this.s = script;
        this.g = graphics;
        this.skill = skill;
        this.frame = frame;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.red = new Color(255, 0, 0, transparency);
        this.green = new Color(0, 255, 0, transparency);
    }

    private int getCurrentExperience() {
        return s.getSkills().getExperience(skill)
                - XP[s.getSkills().getStatic(skill) - 1];
    }

    private int getTargetExperience() {
        return XP[s.getSkills().getStatic(skill)]
                - XP[s.getSkills().getStatic(skill) - 1];
    }

    private int getPercentage(int current, int total) {
        return current * 100 / total;
    }

    private int getWidth(int percentage, int totalWidth) {
        return percentage * totalWidth / 100;
    }

    public void draw() {
        // Current percentage
        g.setColor(green);
        g.fillRect(x, y, getWidth(getPercentage(getCurrentExperience(),getTargetExperience()), width), height);

        // Remaining percentage
        g.setColor(red);
        g.fillRect(x + getWidth(getPercentage(getCurrentExperience(), getTargetExperience()), width), y, width - getWidth(getPercentage(getCurrentExperience(), getTargetExperience()), width), height);

        // Frame
        if (frame) {
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
        }
    }
}