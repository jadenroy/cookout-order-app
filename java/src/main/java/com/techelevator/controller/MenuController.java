package com.techelevator.controller;

import com.techelevator.dao.MenuDao;
import com.techelevator.model.Menu;
import com.techelevator.model.MenuDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/menus")
@CrossOrigin
public class MenuController {

    private final MenuDao menuDao;

    public MenuController(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @PostMapping
    public Menu createMenu(@RequestBody MenuDto menuDto) {
        return menuDao.createMenu(menuDto);
    }

    @GetMapping("/{cookoutId}")
    public Menu getMenu(@PathVariable int cookoutId) {
        return menuDao.getMenuByCookoutId(cookoutId);
    }

    @PutMapping("/items/{itemId}")
    public void updateMenuItem(
            @PathVariable int itemId,
            @RequestBody String itemName
    ) {
        boolean success = menuDao.updateMenuItem(itemId, itemName);
        if (!success) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Menu item not found"
            );
        }
    }

    @DeleteMapping("/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable int itemId) {
        boolean success = menuDao.deleteMenuItem(itemId);
        if (!success) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Menu item not found"
            );
        }
    }
}
