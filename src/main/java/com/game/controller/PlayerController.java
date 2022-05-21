package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerService;
import com.game.utils.PlayerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerValidation playerValidation;

    @Autowired
    public PlayerController(PlayerService playerService, PlayerValidation playerValidation) {
        this.playerService = playerService;
        this.playerValidation = playerValidation;
    }

    @RequestMapping(path = "rest/players", method = RequestMethod.GET)
    public ResponseEntity<List<Player>> findAll(@RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "title", required = false) String title,
                                @RequestParam(value = "race", required = false) Race race,
                                @RequestParam(value = "profession", required = false) Profession profession,
                                @RequestParam(value = "after", required = false) Long after,
                                @RequestParam(value = "before", required = false) Long before,
                                @RequestParam(value = "banned", required = false) Boolean banned,
                                @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                @RequestParam(value = "order", required = false) PlayerOrder order,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize) {
        List<Player> playersList = playerService.getPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel);
        List<Player> sortPlayers = playerService.sortPlayers(playersList, order);
        List<Player> finalPlayers = playerService.getPage(sortPlayers, pageNumber, pageSize);
        return new ResponseEntity<>(finalPlayers, HttpStatus.OK);
    }

    @RequestMapping(path = "rest/players/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getCount(@RequestParam(value = "name", required = false) String name,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "race", required = false) Race race,
                                            @RequestParam(value = "profession", required = false) Profession profession,
                                            @RequestParam(value = "after", required = false) Long after,
                                            @RequestParam(value = "before", required = false) Long before,
                                            @RequestParam(value = "banned", required = false) Boolean banned,
                                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel) {
        Integer count = playerService.getPlayers(name, title, race, profession, after, before, banned,
                minExperience, maxExperience, minLevel, maxLevel).size();
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @RequestMapping(path = "rest/players", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        if (!playerValidation.validateCreatePlayerFields(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        player = playerService.createPlayer(player);
        return new ResponseEntity<>(player, HttpStatus.OK);
    }

    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Player> findById(@PathVariable(value = "id") String pathId) {
        Long id = playerValidation.stringIdToId(pathId);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player foundPlayer = playerService.findById(id);
        if (foundPlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundPlayer, HttpStatus.OK);
    }

    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Player> updatePlayer(@PathVariable(value = "id") String pathId, @RequestBody Player player) {
        Long id = playerValidation.stringIdToId(pathId);
        if (id == null || playerValidation.validateUpdateFields(player)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player foundPlayer = findById(pathId).getBody();
        if (foundPlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.updatePlayer(foundPlayer, player);
        return new ResponseEntity<>(foundPlayer, HttpStatus.OK);
    }

    @RequestMapping(path = "rest/players/{id}", method = RequestMethod.DELETE)
        @ResponseBody
        public ResponseEntity<Player> deletePlayer (@PathVariable(value = "id") String pathId){
        Long id = playerValidation.stringIdToId(pathId);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player foundPlayer = findById(pathId).getBody();
        if (foundPlayer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        playerService.deletePlayer(foundPlayer);
        return new ResponseEntity<>(HttpStatus.OK);
        }
    }

