package com.acescripts.scripts.overloadaio;

import com.acescripts.scripts.overloadaio.framework.ProgressBar;
import com.acescripts.scripts.overloadaio.framework.Task;
import com.acescripts.scripts.overloadaio.gui.GUI;
import com.acescripts.scripts.overloadaio.tutorialisland.methods.TutorialIslandMethods;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.api.util.ExperienceTracker;
import org.osbot.rs07.listener.MessageListener;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import javax.swing.JRootPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Transporter on 26/07/2016 at 01:26.
 */

@ScriptManifest(author = "Transporter", info = "It does EVERYTHING!", name = "Overload AIO", version = 0.3, logo = "http://i.imgur.com/dskvsHA.png")
public class OverloadAIO extends Script implements MessageListener {
    /**
     * TASK DATA
     */
    private List<Task> tasks = new ArrayList<>();
    private Task current = null;
    private GUI gui;
    private TutorialIslandMethods tutorialIslandMethods;
    private String status = "Loading";
    private Image stats_background;
    private Image stats_icon;
    private Image img1;
    private Image show_paint;
    private Image hide_paint;
    private Image tree_icon;
    private Image task;
    private Image empty_task;
    private boolean showhide = true;
    private boolean main_stats_tab = true;
    private boolean woodcutting_tab = false;
    private int taskCounter = 0;

    /**
     * LOG COUNTERS
     */
    private int logCounter;
    private int oakLogCounter;
    private int willowLogCounter;
    private int teakLogCounter;
    private int mapleLogCounter;
    private int yewLogCounter;
    private int magicLogCounter;

    /**
     * LOG PRICES
     */
    private Optional<Integer> logPrice;
    private Optional<Integer> oakLogPrice;
    private Optional<Integer> willowLogPrice;
    private Optional<Integer> teakLogPrice;
    private Optional<Integer> mapleLogPrice;
    private Optional<Integer> yewLogPrice;
    private Optional<Integer> magicLogPrice;

    /**
     * GUI PAINT DATA
     */

    private boolean guiWait = true;
    private ExperienceTracker xpTrack;
    private long timeBegan;

    /**
     * MOUSE PAINT DATA
     */

    private Color DARKRED150 = new Color(73, 226, 5), BLACK100 = new Color(232, 232, 238);
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<>();

    private boolean stopScript = false;

    /**
     * UPDATE DATA
     */

    private long timeRunning = 0;
    private int xpGained = 0;
    private int gpGained = 0;
    private int lvsGained = 0;
    private ScheduledExecutorService executor;

    private JTextArea textArea = new JTextArea();
    private JScrollPane scrollPane = new JScrollPane(textArea);
    private JLayeredPane jFrame;

    private int size = 0;

    @Override
    public void onStart() throws InterruptedException {
        try {
            gui = new GUI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        tutorialIslandMethods = new TutorialIslandMethods(this);
        guiWait = true;

        while(guiWait) {
            if(!gui.isShowing() && !stopScript) {
                gui.setVisible(true);
            } else {
                if(stopScript) {
                    break;
                }
            }
            sleep(random(200, 500));
        }

        try {
            stats_background = loadImage("stats_background.png");
            stats_icon = loadImage("stats_icon.png");
            img1 = loadImage("img1.png");
            show_paint = loadImage("show_paint.png");
            hide_paint = loadImage("hide_paint.png");
            tree_icon = loadImage("tree_icon.png");
            task = loadImage("task.png");
            empty_task = loadImage("empty.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!guiWait) {
            gui.dispose();
            xpTrack = getExperienceTracker();
            xpTrack.startAll();
            logPrice = getPrice(1511);
            oakLogPrice = getPrice(1521);
            willowLogPrice = getPrice(1519);
            teakLogPrice = getPrice(6333);
            mapleLogPrice = getPrice(1517);
            yewLogPrice = getPrice(1515);
            magicLogPrice = getPrice(1513);

            timeBegan = System.currentTimeMillis();

            bot.getCanvas().addMouseListener(new MouseListener() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    Point p = e.getPoint();
                    Rectangle rec = new Rectangle(398, 477, 116, 22);

                    if(rec.contains(p)) {
                        showhide = !showhide;
                    }

                    Rectangle rec2 = new Rectangle(7, 349, 47, 35);

                    if(rec2.contains(p)) {
                        if(main_stats_tab) {
                            main_stats_tab = false;
                            woodcutting_tab = true;
                        } else {
                            main_stats_tab = true;
                            woodcutting_tab = true;
                        }
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    Point p = e.getPoint();
                    Rectangle rec = new Rectangle(398, 477, 116, 22);

                    if(rec.contains(p)) {
                        showhide = !showhide;
                    }

                    Rectangle rec2 = new Rectangle(7, 349, 47, 35);

                    if(rec2.contains(p)) {
                        if(main_stats_tab) {
                            main_stats_tab = false;
                            woodcutting_tab = true;
                        } else {
                            main_stats_tab = true;
                            woodcutting_tab = true;
                        }
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    Point p = e.getPoint();
                    Rectangle rec = new Rectangle(398, 477, 116, 22);

                    if(rec.contains(p)) {
                        showhide = !showhide;
                    }

                    Rectangle rec2 = new Rectangle(7, 349, 47, 35);

                    if(rec2.contains(p)) {
                        if(main_stats_tab) {
                            main_stats_tab = false;
                            woodcutting_tab = true;
                        } else {
                            main_stats_tab = true;
                            woodcutting_tab = false;
                        }
                    }
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {

                }

                @Override
                public void mouseExited(MouseEvent arg0) {

                }
            });

            Runnable dataRunnable = () -> {
                long timeRan = System.currentTimeMillis() - timeBegan;
                long runTime = timeRan - timeRunning;

                long time_seconds = TimeUnit.MILLISECONDS.toSeconds(runTime);
                int profit = totalProfit() - gpGained;
                int levels = getTotalLevels() - lvsGained;
                int xp = getTotalXp() - xpGained;

                sendSignatureData(time_seconds, 0, profit, levels, 0, xp);
                timeRunning += runTime;
                gpGained += profit;
                lvsGained += levels;
                xpGained += xp;
            };

            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(dataRunnable, 0, 5, TimeUnit.MINUTES);
            getBot().addMessageListener(this);
        }
    }

    private Optional<Integer> getPrice(int id){
        Optional<Integer> price = Optional.empty();

        try {
            URL url = new URL("http://api.rsbuddy.com/grandExchange?a=guidePrice&i=" + id);
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
            con.setUseCaches(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String[] data = br.readLine().replace("{", "").replace("}", "").split(",");
            br.close();
            price = Optional.of(Integer.parseInt(data[0].split(":")[1]));
        } catch(Exception e){
            e.printStackTrace();
        }
        return price;
    }

    @Override
    public int onLoop() throws InterruptedException {
        int curr_size = logger.getBuffer().size();

        if(size != curr_size) {
            if(textArea != null) {
                for(int i = size; i < logger.getBuffer().size() - 1; i++) {
                    textArea.append(logger.getBuffer().get(i) + "\n");
                }
                size = curr_size - 1;
            }
        }

        if(current != null) {
            if (!current.isFinished()) {
                current.proceed();
            } else {
                taskCounter++;
                current = null;
            }
        } else {
            if (!tasks.isEmpty()) {
                current = tasks.remove(0);
            } else {
                stop();
            }
        }
        return random(500, 700);
    }

    private String formatTime(long duration) {
        String res;
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        String daysFormat = days < 10 ? "0" + days : "" + days;
        String hoursFormat = hours < 10 ? "0" + hours : "" + hours;
        String minutesFormat = minutes < 10 ? "0" + minutes : "" + minutes;
        String secondsFormat = seconds < 10 ? "0" + seconds : "" + seconds;

        if (days == 0) {
            res = (hoursFormat + ":" + minutesFormat + ":" + secondsFormat);
        } else {
            res = (daysFormat + ":" + hoursFormat + ":" + minutesFormat + ":" + secondsFormat);
        }
        return res;
    }

    public int getTotalLogs() {
        return logCounter + oakLogCounter + willowLogCounter + teakLogCounter + mapleLogCounter + yewLogCounter + magicLogCounter;
    }

    public int getTotalFish() {
        return 0;
    }

    private int getTotalLevels() {
        int total = 0;
        for(Skill levels: Skill.values()) {
            total += getXpTrack().getGainedLevels(levels);
        }
        return total;
    }

    private int getTotalXp() {
        int total = 0;
        for(Skill levels: Skill.values()) {
            total += getXpTrack().getGainedXP(levels);
        }
        return total;
    }

    private int getTotalXpPerHour() {
        int total = 0;
        for(Skill levels: Skill.values()) {
            total += getXpTrack().getGainedXPPerHour(levels);
        }
        return total;
    }

    private Font arial_narrow = new Font("Arial Narrow", Font.PLAIN, 11);

    @Override
    public void onPaint(Graphics2D g) {
        if(!guiWait) {
            if(showhide) {
                g.drawImage(hide_paint, 404, 480, null);
                g.setColor(new Color(255, 255, 255));

                if(main_stats_tab) {
                    g.drawImage(stats_background, 1, 338, null);
                    drawCenteredString(g, "Total Levels Gained: " + formatInteger(getTotalLevels()), new Rectangle(13, 401, 117, 17), arial_narrow);
                    drawCenteredString(g, "Total XP Gained: " + formatInteger(getTotalXp()), new Rectangle(13, 424, 117, 17), arial_narrow);
                    drawCenteredString(g, "Total Profit: " + formatInteger(totalProfit()), new Rectangle(13, 445, 117, 17), arial_narrow);
                    drawCenteredString(g, "Tasks Complete: " + formatInteger(taskCounter), new Rectangle(135, 401, 117, 17), arial_narrow);
                    drawCenteredString(g, "Total XP/PH: " + formatInteger(getTotalXpPerHour()), new Rectangle(135, 424, 117, 17), arial_narrow);
                    drawCenteredString(g, "Total Profit P/H: " + formatInteger(perHour(totalProfit())), new Rectangle(135, 445, 117, 17), arial_narrow);
                    g.drawImage(stats_icon, 11, 347, null);

                    if(current != null) {
                        g.drawImage(task, 255, 401, null);
                        drawCenteredString(g, "1", new Rectangle(257, 401, 25, 17), arial_narrow);
                        drawCenteredString(g, current.getTaskName(), new Rectangle(280, 401, 109, 17), arial_narrow);
                        drawCenteredString(g, current.getTaskCurrentAmount(), new Rectangle(389, 401, 25, 17), arial_narrow);
                        drawCenteredString(g, current.getTaskType(), new Rectangle(413, 401, 67, 17), arial_narrow);
                        drawCenteredString(g, current.getTaskGoalAmount(), new Rectangle(480, 401, 25, 17), arial_narrow);
                    } else {
                        g.drawImage(empty_task, 255, 401, null);
                    }

                    if(tasks.size() >= 1) {
                        g.drawImage(task, 255, 423, null);
                        drawCenteredString(g, "2", new Rectangle(257, 423, 25, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(0).getTaskName(), new Rectangle(280, 423, 109, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(0).getTaskCurrentAmount(), new Rectangle(389, 423, 25, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(0).getTaskType(), new Rectangle(413, 423, 67, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(0).getTaskGoalAmount(), new Rectangle(480, 423, 25, 17), arial_narrow);
                    } else {
                        g.drawImage(empty_task, 255, 423, null);
                    }

                    if(tasks.size() >= 2) {
                        g.drawImage(task, 255, 445, null);
                        drawCenteredString(g, "3", new Rectangle(257, 445, 25, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(1).getTaskName(), new Rectangle(280, 445, 109, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(1).getTaskCurrentAmount(), new Rectangle(389, 445, 25, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(1).getTaskType(), new Rectangle(413, 445, 67, 17), arial_narrow);
                        drawCenteredString(g, tasks.get(1).getTaskGoalAmount(), new Rectangle(480, 445, 25, 17), arial_narrow);
                    } else {
                        g.drawImage(empty_task, 255, 445, null);
                    }
                }

                if(woodcutting_tab) {
                    g.drawImage(img1, 1, 338, null);
                    drawCenteredString(g, "Level: " + getXpTrack().getSkills().getStatic(Skill.WOODCUTTING) + " (+" + getXpTrack().getGainedLevels(Skill.WOODCUTTING) + ")", new Rectangle(13, 401, 117, 17), arial_narrow);
                    drawCenteredString(g, "XP Gained: " + formatInteger(getXpTrack().getGainedXP(Skill.WOODCUTTING)), new Rectangle(13, 424, 117, 17), arial_narrow);
                    drawCenteredString(g, "Total Profit: " + formatInteger(totalProfit()), new Rectangle(13, 445, 117, 17), arial_narrow);
                    drawCenteredString(g, "Logs Cut: " + formatInteger(getTotalLogs()), new Rectangle(135, 401, 117, 17), arial_narrow);
                    drawCenteredString(g, "XP/PH: " + formatInteger(getXpTrack().getGainedXPPerHour(Skill.WOODCUTTING)), new Rectangle(135, 424, 117, 17), arial_narrow);
                    drawCenteredString(g, "Profit P/H: " + formatInteger(perHour(totalProfit())), new Rectangle(135, 445, 117, 17), arial_narrow);
                    drawCenteredString(g, "" + logCounter, new Rectangle(259, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + oakLogCounter, new Rectangle(295, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + willowLogCounter, new Rectangle(331, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + teakLogCounter, new Rectangle(367, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + mapleLogCounter, new Rectangle(403, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + yewLogCounter, new Rectangle(438, 423, 32, 17), arial_narrow);
                    drawCenteredString(g, "" + magicLogCounter, new Rectangle(475, 423, 32, 17), arial_narrow);
                    ProgressBar bar = new ProgressBar(this, g, "", Skill.WOODCUTTING, true, 150, arial_narrow, 260, 446, 244, 14);
                    bar.draw();
                    g.setColor(new Color(255, 255, 255));
                    drawCenteredString(g, "Time Until Level: " + formatTime(getXpTrack().getTimeToLevel(Skill.WOODCUTTING)), new Rectangle(256, 445, 246, 17), arial_narrow);
                    g.drawImage(tree_icon, 14, 349, null);
                }

                long timeRan = System.currentTimeMillis() - this.timeBegan;
                drawCenteredString(g, "Time Running: " + formatTime(timeRan), new Rectangle(56, 348, 127, 18), arial_narrow);
                drawCenteredString(g, "Version: " + getVersion(), new Rectangle(188, 348, 79, 17), arial_narrow);
                drawCenteredString(g, "Status: " + status, new Rectangle(56, 369, 211, 17), arial_narrow);
            } else {
                g.drawImage(show_paint, 404, 480, null);
            }
            paintMouseSpline(g);
            paintMouse(g);
        }
    }

    private int perHour(int number) {
        long timeRan = System.currentTimeMillis() - this.timeBegan;
        return (int) ((3600000.0 / timeRan) * number);
    }

    public void setStopScript(boolean stop) {
        stopScript = stop;
    }

    private int totalProfit() {
        int logProfit = logCounter * logPrice.get();
        int oakProfit = oakLogCounter * oakLogPrice.get();
        int willowProfit = willowLogCounter * willowLogPrice.get();
        int teakProfit = teakLogCounter * teakLogPrice.get();
        int mapleProfit = mapleLogCounter * mapleLogPrice.get();
        int yewProfit = yewLogCounter * yewLogPrice.get();
        int magicProfit = magicLogCounter * magicLogPrice.get();

        return logProfit + oakProfit + willowProfit + teakProfit + mapleProfit + yewProfit + magicProfit;
    }

    /**
     * Draw a String centered in the middle of a Rectangle.
     *
     * @param g The Graphics instance.
     * @param text The String to draw.
     * @param rect The Rectangle to center the text in.
     */
    private void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (rect.width - metrics.stringWidth(text)) / 2 + rect.x;
        int y = ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent() + rect.y;
        g.setFont(font);
        g.drawString(text, x, y);
    }

    private final int circleSize = 2;
    private final int outerSize = 24;
    private final int medSize = 12;

    private void paintMouse(Graphics2D g) {
        Point p = mouse.getPosition();

        Graphics2D middle = (Graphics2D) g.create();
        Graphics2D spinG = (Graphics2D) g.create();
        Graphics2D spinGRev2 = (Graphics2D) g.create();

        middle.setColor(BLACK100);
        spinG.setColor(BLACK100);
        spinGRev2.setColor(DARKRED150);

        spinG.rotate(System.currentTimeMillis() % 2000d / 2000d * (360d) * 2 * Math.PI / 180.0, p.x, p.y);
        spinGRev2.rotate(System.currentTimeMillis() % 2000d / 2000d * (-360d) * 2 * Math.PI / 180.0, p.x, p.y);

        spinG.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        spinGRev2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        middle.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        spinG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        spinGRev2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        middle.fillOval(p.x - circleSize, p.y - circleSize, circleSize * 2, circleSize * 2);
        spinG.drawArc(p.x - (outerSize / 2), p.y - (outerSize / 2), outerSize, outerSize, 0, 275);
        spinGRev2.drawArc(p.x - (medSize / 2), p.y - (medSize / 2), medSize, medSize, 0, 275);
    }

    private void paintMouseSpline(Graphics2D g) {
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) mousePath.remove();
        Point clientCursor = mouse.getPosition();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y, 3000);

        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) mousePath.add(mpp);
        MousePathPoint lastPoint = null;

        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(a.getColor());
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
    }

    @SuppressWarnings("serial")
    private class MousePathPoint extends Point { // credits to Enfilade
        private int toColor(double d) {
            return Math.min(255, Math.max(0, (int) d));
        }

        private long finishTime;

        private double lastingTime;

        MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

        Color getColor() {
            return new Color(69, 139, 0, toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setStatus(String newStatus) {
        this.status = newStatus;
    }

    public void setGuiWait(boolean waitStatus) {
        this.guiWait = waitStatus;
    }

    public TutorialIslandMethods getMethods() {
        return tutorialIslandMethods;
    }

    public GUI getGui() { return gui; }

    private ExperienceTracker getXpTrack() {
        return xpTrack;
    }

    private String formatInteger(int integer) {
        return NumberFormat.getNumberInstance(Locale.US).format(integer);
    }

    private Image loadImage(String imageName) throws IOException {
        return ImageIO.read(getClass().getResourceAsStream("/resources/paint/" + imageName));
    }

    @Override
    public void onMessage(Message message) throws java.lang.InterruptedException {
        String Txt = message.getMessage();

        switch (Txt) {
            case "You get some logs.":
                logCounter++;
                break;
            case "You get some oak logs.":
                oakLogCounter++;
                break;
            case "You get some willow logs.":
                willowLogCounter++;
                break;
            case "You get some teak logs.":
                teakLogCounter++;
                break;
            case "You get some maple logs.":
                mapleLogCounter++;
                break;
            case "You get some yew logs.":
                yewLogCounter++;
                break;
            case "You get some magic logs.":
                magicLogCounter++;
                break;
        }
    }

    private boolean sendSignatureData(long runtimeInSeconds, int var1, int var2, int var3, int var4, int var5) {
        String privateKey = "199 26 173 152 8";
        String initVector = "199 26 173 152 8";

        try {
            String data = initVector+","+getClient().getUsername()+","+runtimeInSeconds+","+var1+","+var2+","+var3+","+var4+","+var5;

            IvParameterSpec ivspec = new IvParameterSpec(initVector.getBytes());
            SecretKeySpec keyspec = new SecretKeySpec(privateKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));

            String token = "";
            for (byte anEncrypted : encrypted) {
                if ((anEncrypted & 0xFF) < 16) {
                    token = token + "0" + Integer.toHexString(anEncrypted & 0xFF);
                } else {
                    token = token + Integer.toHexString(anEncrypted & 0xFF);
                }
            }

            sendUpdate("http://www.acescripts.com/overloadaio/input.php?token="+token);
            return true;
        } catch (Exception e) {
            log(e);
        }
        return false;
    }

    private void sendUpdate(String urlName) throws IOException {
        URL url = new URL(urlName);
        URLConnection conn = url.openConnection();
        conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2.13) Gecko/20101203 Firefox/3.6.13 (.NET CLR 3.5.30729)");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        in.readLine();
        in.close();
    }

    @Override
    public void onExit() {
        jFrame.remove(scrollPane);
        executor.shutdown();
        long timeRan = System.currentTimeMillis() - timeBegan;
        long runTime = timeRan - timeRunning;

        long time_seconds = TimeUnit.MILLISECONDS.toSeconds(runTime);
        int profit = totalProfit() - gpGained;
        int levels = getTotalLevels() - lvsGained;
        int xp = getTotalXp() - xpGained;

        sendSignatureData(time_seconds, 0, profit, levels, 0, xp);
        timeRunning += runTime;
        gpGained += profit;
        lvsGained += levels;
        xpGained += xp;
        bot.getMessageListeners().clear();
    }
}