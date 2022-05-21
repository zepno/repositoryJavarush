package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;

import java.util.List;

public interface PlayerService {

    Player createPlayer(Player player);

    List<Player> getPlayers(String name, String title, Race race, Profession profession, Long after, Long before, Boolean banned, Integer minExperience, Integer maxExperience, Integer minLevel, Integer maxLevel);

    List<Player> sortPlayers(List<Player> playersList, PlayerOrder order);

    List<Player> getPage(List<Player> sortPlayers, Integer pageNumber, Integer pageSize);

    Player findById(Long id);

    Player updatePlayer(Player findPlayer, Player player);

    void deletePlayer(Player player);
}
