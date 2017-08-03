package com.acescripts.scripts.overloadaio.skills.fishing;

import com.acescripts.scripts.overloadaio.OverloadAIO;
import com.acescripts.scripts.overloadaio.framework.Bank;
import com.acescripts.scripts.overloadaio.framework.Task;
import com.acescripts.scripts.overloadaio.framework.fishing.FishingEquipment;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.event.WalkingEvent;
import org.osbot.rs07.event.WebWalkEvent;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.concurrent.TimeUnit;

import static org.osbot.rs07.script.MethodProvider.random;
import static org.osbot.rs07.script.MethodProvider.sleep;

/**
 * Created by Transporter on 08/12/2016 at 14:33.
 */

public class FishingTask implements Task {
    private OverloadAIO script;
    private String toFish;
    private int stopLevel;
    private String stopType;
    private Area fishingArea;
    private String fishingAreaName;
    private boolean shouldBank = false;
    private long timeBegan;

    public FishingTask(OverloadAIO script, String toFish, int stopLevel, String stopType, Area fishingArea, String fishingAreaName, boolean shouldBank) {
        this.script = script;
        this.toFish = toFish;
        this.stopLevel = stopLevel;
        this.stopType = stopType;
        this.fishingArea = fishingArea;
        this.fishingAreaName = fishingAreaName;
        this.shouldBank = shouldBank;
    }

    private enum State {
        BANK, FISH, DROP, WALK_TO_FISH, WAIT
    }

    private State getState() {
        NPC fishingSpot = script.getNpcs().closest("Fishing spot");

        if(script.inventory.isFull() && shouldBank || !script.getInventory().contains(getFishingEquipment())) {
            return State.BANK;
        }

        if(script.getInventory().contains(getFishingEquipment()) && !script.inventory.isFull() && fishingSpot != null && !script.myPlayer().isAnimating() && fishingArea.contains(script.myPlayer())) {
            return State.FISH;
        }

        if(script.inventory.isFull() && !shouldBank) {
            return State.DROP;
        }

        if(script.getInventory().contains(getFishingEquipment()) && !script.inventory.isFull() && !fishingArea.contains(script.myPlayer())) {
            return State.WALK_TO_FISH;
        }
        return State.WAIT;
    }

    private String[] getFishingEquipment() {
        String[] names = new String[100];

        if(toFish.equals("SHRIMPS / ANCHOVIES")) {
            names[0] = FishingEquipment.SMALL_NET.getName();
        }
        return names;
    }

    private void withdrawFishingEquipment(String[] equipmentName) {
        script.getBank().depositAllExcept(equipmentName);

        for(String anEquipmentName : equipmentName) {
            script.getBank().withdraw(anEquipmentName, 1);
        }
    }

    @Override
    public void proceed() throws InterruptedException {
        switch (getState()) {
            case BANK:
                script.setStatus("Banking");

                Area currentBank;

                if(fishingAreaName.equals("LUMBRIDGE_SWAMP")) {
                    currentBank = Banks.LUMBRIDGE_UPPER;
                } else {
                    currentBank = Bank.closestTo(script.myPlayer());
                }

                if(currentBank.contains(script.myPlayer())) {
                    if(script.getBank().isOpen()) {
                        if(script.getInventory().contains(getFishingEquipment())) {
                            script.getBank().depositAllExcept(getFishingEquipment());
                            script.getBank().close();
                        } else {
                            if(toFish.equals("SHRIMPS / ANCHOVIES")) {
                                withdrawFishingEquipment(new String[] {
                                        FishingEquipment.SMALL_NET.getName()
                                });
                            }
                        }
                    } else {
                        script.getBank().open();
                    }
                    sleep(random(1000, 1400));
                } else {
                    script.walking.webWalk(currentBank);
                }
                break;
            case FISH:
                script.setStatus("Fishing");

                NPC fishing_spot = script.getNpcs().closest((Filter<NPC>) npc -> npc != null && npc.getName().equals("Fishing spot") && fishingArea.contains(npc.getPosition()) && !npc.getPosition().equals(new Position(3246, 3157, 0)));

                if(fishing_spot != null && fishing_spot.isVisible()) {
                    fishing_spot.interact("Net");
                    //GET FISHING TYPE E.G SHRIMS = NET
                    //DO CHECK FOR EACH AND MAKE IT INTERACTION OPTION


                    new ConditionalSleep(2000) {
                        @Override
                        public boolean condition() {
                            return script.myPlayer().isAnimating() && script.myPlayer().isMoving();
                        }
                    }.sleep();
                } else {
                    if(fishing_spot != null) {
                        if (script.myPosition().distance(fishing_spot.getPosition()) > 3) {
                            script.getWalking().walk(fishing_spot);
                        }
                        script.camera.toPosition(fishing_spot.getPosition());
                    }
                }
                break;
            case DROP:
                script.setStatus("Drop");
                //MULTIPLE DROP OPTIONS?
                //DROP_WORSE, DROP_BOTH
                break;
            case WALK_TO_FISH:
                script.setStatus("Walking to Fishing Area");
                script.getWalking().webWalk(fishingArea);
                break;
            case WAIT:
                script.setStatus("Waiting...");
                break;
        }
    }

    @Override
    public boolean isFinished() {
        if(timeBegan <= 0) {
            timeBegan = System.currentTimeMillis();
        }

        long timeRan = System.currentTimeMillis() - this.timeBegan;

        switch (stopType) {
            case "Level":
                return script.skills.getDynamic(Skill.FISHING) >= stopLevel;
            case "Fish Count":
                return script.getTotalFish() >= stopLevel;
            case "Timed":
                return TimeUnit.MILLISECONDS.toMinutes(timeRan) >= stopLevel;
            default:
                return script.skills.getDynamic(Skill.FISHING) >= stopLevel;
        }
    }

    private String formatTime(long duration) {
        String res;
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));

        String hoursFormat = hours < 10 ? "0" + hours : "" + hours;
        String minutesFormat = minutes < 10 ? "0" + minutes : "" + minutes;
        String secondsFormat = seconds < 10 ? "0" + seconds : "" + seconds;

        if (hours == 0) {
            res = (minutesFormat + ":" + secondsFormat);
        } else {
            res = (hoursFormat + ":" + minutesFormat + ":" + secondsFormat);
        }
        return res;
    }

    @Override
    public String getTaskName() {
        return "Fishing Task";
    }

    @Override
    public String getTaskType() {
        return stopType;
    }

    @Override
    public String getTaskCurrentAmount() {
        long timeRan = System.currentTimeMillis() - timeBegan;

        switch (stopType) {
            case "Level":
                return "" + script.skills.getDynamic(Skill.FISHING);
            case "Fish Count":
                return "" + script.getTotalFish();
            case "Timed":
                return "" + formatTime(timeRan);
            default:
                return "" + script.skills.getDynamic(Skill.FISHING);
        }
    }

    @Override
    public String getTaskGoalAmount() {
        return "" + stopLevel;
    }
}
