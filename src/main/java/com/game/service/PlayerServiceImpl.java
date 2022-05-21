package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.utils.PlayerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PlayerServiceImpl implements PlayerService {

    public static final int MIN_EXPERIENCE = 0;
    public static final int MIN_LEVEL = 0;
    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 3;
    private final PlayerRepository playerRepository;
    private final PlayerValidation playerValidation;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerValidation playerValidation) {
        this.playerRepository = playerRepository;
        this.playerValidation = playerValidation;
    }

    @Override
    public Player createPlayer(Player player) {
        player.setLevel(playerValidation.setCurrentLevel(player));
        player.setUntilNextLevel(playerValidation.setUnNextLvl(player));
        return playerRepository.save(player);
    }

    @Override
    public List<Player> getPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel) {
        List<Player> playerList = new ArrayList<>();
        Date afterDate;
        if (after == null) afterDate = null;
        else afterDate = new Date(after);
        Date beforeDate;
        if (before == null) beforeDate = null;
        else beforeDate = new Date(before);
        for (Player player : playerRepository.findAll()) {
            if (name != null && !player.getName().contains(name)) continue;
            if (title != null && !player.getTitle().contains(title)) continue;
            if (race != null && player.getRace() != race) continue;
            if (profession != null && player.getProfession() != profession) continue;
            if (afterDate != null && player.getBirthday().before(afterDate)) continue;
            if (beforeDate != null && player.getBirthday().after(beforeDate)) continue;
            if (banned != null && player.isBanned() != banned) continue;
            if (minExperience != null && player.getExperience().compareTo(minExperience) < MIN_EXPERIENCE) continue;
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > MIN_EXPERIENCE) continue;
            if (minLevel != null && player.getLevel().compareTo(minLevel) < MIN_LEVEL) continue;
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > MIN_LEVEL) continue;
            playerList.add(player);
        }
        return playerList;
    }

    @Override
    public List<Player> sortPlayers(List<Player> playersList, PlayerOrder order) {
        if (order != null) {
            playersList.sort((playersList1, playersList2) -> {
                switch (order) {
                    case ID:return playersList1.getId().compareTo(playersList2.getId());
                    case NAME:return playersList1.getName().compareTo(playersList2.getName());
                    case LEVEL:return playersList1.getLevel().compareTo(playersList2.getLevel());
                    case BIRTHDAY:return playersList1.getBirthday().compareTo(playersList2.getBirthday());
                    case EXPERIENCE:return playersList1.getExperience().compareTo(playersList2.getExperience());
                    default:return 0;
                }
            });
        }
        return playersList;
    }

    @Override
    public List<Player> getPage(List<Player> sortPlayers, Integer pageNumber, Integer pageSize) {
        int page = pageNumber == null ? PAGE_NUMBER : pageNumber;
        int size = pageSize == null ? PAGE_SIZE : pageSize;
        int from = page * size;
        int to = from + size;
        if (to > sortPlayers.size()) {
            to = sortPlayers.size();
        }
        return sortPlayers.subList(from, to);
    }

    @Override
    public Player findById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Override
    public Player updatePlayer(Player foundPlayer, Player player) {
        if (player.getName() != null) foundPlayer.setName(player.getName());
        if (player.getTitle() != null) foundPlayer.setTitle(player.getTitle());
        if (player.getRace() != null) foundPlayer.setRace(player.getRace());
        if (player.getProfession() != null) foundPlayer.setProfession(player.getProfession());
        if (player.getExperience() != null) foundPlayer.setExperience(player.getExperience());
        if (player.isBanned() != null) foundPlayer.setBanned(player.isBanned());
        if (player.getBirthday() != null) foundPlayer.setBirthday(player.getBirthday());
        foundPlayer.setLevel(playerValidation.setCurrentLevel(foundPlayer));
        foundPlayer.setUntilNextLevel(playerValidation.setUnNextLvl(foundPlayer));
        return playerRepository.save(player);
    }

    @Override
    public void deletePlayer(Player player) {
        playerRepository.delete(player);
    }
}
