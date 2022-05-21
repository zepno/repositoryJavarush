package com.game.utils;

import com.game.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PlayerValidation {

    public static final int MAX_NAME_LENGTH = 12;
    public static final int MAX_TITLE_LENGTH = 30;
    public static final String EMPTY_STRING = "";
    public static final int MIN_EXPERIENCE = 0;
    public static final int MAX_EXPERIENCE = 10_000_000;
    public static final int MIN_BIRTHDAY = 0;
    public static final int MIN_YEAR_OF_BIRTHDAY = 2000;
    public static final int MAX_YEAR_BIRTHDAY = 3000;
    public static final int MIN_LEVEL = 0;
    public static final int MAX_LEVEL = 9999;
    public static final int ID_NUMBER = 0;
    public static final int EXPERIENCE_TO_NEXT_LEVEL = 0;

    @Autowired
    public PlayerValidation() {
    }

    public boolean validateCreatePlayerFields(Player player) {
        if (player.getName() == null
                || player.getTitle() == null
                || player.getRace() == null
                || player.getProfession() == null
                || player.getBirthday() == null
                || player.getExperience() == null) {
            return false;
        }
        if (player.isBanned() == null){
            player.setBanned(false);
        }
        return validateFields(player);
    }

    private boolean validateFields(Player player) {
        return player.getName().length() <= MAX_NAME_LENGTH
                && player.getTitle().length() <= MAX_TITLE_LENGTH
                && !player.getName().equals(EMPTY_STRING)
                && !player.getTitle().equals(EMPTY_STRING)
                && player.getExperience() >= MIN_EXPERIENCE
                && player.getExperience() <= MAX_EXPERIENCE
                && player.getBirthday().getTime() >= MIN_BIRTHDAY
                && validBirthday(player);
    }

    private boolean validBirthday(Player player) {
        Calendar date = Calendar.getInstance();
        date.setTime(player.getBirthday());
        return date.get(Calendar.YEAR) >= MIN_YEAR_OF_BIRTHDAY && date.get(Calendar.YEAR) <= MAX_YEAR_BIRTHDAY;
    }

    public Integer setCurrentLevel(Player player) {
        int exp = player.getExperience();
        return (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
    }

    public Integer setUnNextLvl(Player player) {
        int exp = player.getExperience();
        int lvl = setCurrentLevel(player);
        return 50 * (lvl + 1) * (lvl + 2) - exp;
    }

    public boolean validateUpdateFields(Player player) {
        if (player.getName() != null && validateString(player.getName(), MAX_NAME_LENGTH))
            return true;
        if (player.getTitle() != null && validateString(player.getTitle(), MAX_TITLE_LENGTH))
            return true;
        if (player.getExperience() != null && (player.getExperience() < MIN_EXPERIENCE || player.getExperience() > MAX_EXPERIENCE))
            return true;
        if (player.getLevel() != null && (player.getLevel() < MIN_LEVEL || player.getLevel() > MAX_LEVEL)) return true;
        if (player.getUntilNextLevel() != null && player.getUntilNextLevel() < EXPERIENCE_TO_NEXT_LEVEL) return true;
        if (player.getBirthday() != null && (!validBirthday(player)))
            return true;
        if (player.getBirthday() != null && player.getBirthday().getTime() < MIN_BIRTHDAY) return true;
        return false;
    }

    private boolean validateString(String name, int maxNameLength) {
        return name.length() > maxNameLength || name.equals(EMPTY_STRING);
    }

    public Long stringIdToId(String pathId) {
        try {
            long id = Long.parseLong(pathId);
            if (id <= ID_NUMBER) {
                return null;
            }
            return id;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}