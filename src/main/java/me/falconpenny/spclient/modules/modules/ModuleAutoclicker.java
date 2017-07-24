package me.falconpenny.spclient.modules.modules;

import lombok.Setter;
import me.falconpenny.spclient.config.Config;
import me.falconpenny.spclient.modules.Module;
import me.falconpenny.spclient.modules.ModuleData;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

@ModuleData(name = "AutoClicker", aliases = {"ac", "autoc", "autoclick", "aclick", "aclicker"}, description = "Auto clicker using java's Robot.")
public class ModuleAutoclicker extends Module {
    private volatile int cps = 0;
    private volatile int lastCps = 0;

    private CpsRandomizer randomizer = new CpsRandomizer();
    private CpsClicker clicker = new CpsClicker();

    {
        randomizer.start();
        clicker.start();
    }

    private class CpsRandomizer extends Thread {
        @Setter
        private boolean stop = false;

        @Override
        public void run() {
            if (stop) {
                return;
            }
            if (cps != 0) {
                lastCps = cps;
            }
            Map<Integer, Float> chances = getChances(Config.getInstance().getMinCps(), Config.getInstance().getMaxCps(), lastCps);
            Map<Integer, float[]> bounds = new HashMap<>();
            float current = 0;
            for (Map.Entry<Integer, Float> entry : chances.entrySet()) {
                bounds.putIfAbsent(entry.getKey(), new float[]{current, current += entry.getValue()});
            }
            float random = (float) ThreadLocalRandom.current().nextDouble(0, 100);
            for (Map.Entry<Integer, float[]> entry : bounds.entrySet()) {
                if (entry.getValue()[0] <= random && random <= entry.getValue()[1]) {
                    cps = entry.getKey();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    run();
                    return;
                }
            }
            cps = (Config.getInstance().getMaxCps() + Config.getInstance().getMinCps()) / 2;
        }

        private Map<Integer, Float> getChances(int min, int max, int close) {
            int differ = max - min;
            if (close > max || close < min) {
                close = differ + min;
            }
            int[] numbers = IntStream.rangeClosed(min, max).toArray();
            if (numbers[numbers.length - 1] == close) {
                numbers = reverse(numbers);
            }
            Map<Integer, Float> chances = new LinkedHashMap<>();
            chances.put(close, 50f);
            for (int i = min; i <= max; i++) {
                float chance = (float) Math.round((50f / (Math.abs(i - close) * 2f) * 100f)) / 100f;
                if (String.valueOf(chance).endsWith(".33")) {
                    if (i < close)
                        chance = (float) Math.floor(chance);
                    else
                        chance = (float) Math.ceil(chance);
                }
                chances.putIfAbsent(i, chance);
            }
            return chances;
        }

        private int[] reverse(final int... array) {
            int[] ret = new int[array.length];
            for (int i = array.length - 1; i > 0; i--) {
                ret[Math.abs(0 - i)] = array[i];
            }
            return ret;
        }
    }

    private class CpsClicker extends Thread {
        @Setter
        private boolean stop = false;

        private boolean isHolding() {
            return Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown();
        }

        @Override
        public void run() {
            if (stop) {
                return;
            }
            int millisRemaining = 1000 - (int) Math.ceil(System.currentTimeMillis() % 1000);
            while (!isHolding() && (millisRemaining = 1000 - (int) Math.ceil(System.currentTimeMillis() % 1000)) != 0 && millisRemaining != 1000)
            {
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stop) {
                    return;
                }
            }
            int msPerClick = (int) Math.floor(1000 / cps);
            int remainingClicks = (int) Math.floor(millisRemaining / msPerClick);
            for (int i = 0; i <= remainingClicks; i++) {
                Minecraft.getMinecraft().thePlayer.swingItem();
                try {
                    sleep(msPerClick);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stop) {
                    return;
                }
                if (!isHolding()) break;
            }
            double remaining = Math.ceil(1000 - (int) Math.ceil(System.currentTimeMillis() % 1000));
            if (remaining != 0)
                try {
                    sleep((long) remaining);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            run();
        }
    }
}
