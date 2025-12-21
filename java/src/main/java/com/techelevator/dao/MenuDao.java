package com.techelevator.dao;

import com.techelevator.model.Menu;
import com.techelevator.model.MenuDto;

public interface MenuDao {

    Menu getMenuByCookoutId(int cookoutId);

    Menu createMenu(MenuDto menuDto);

    boolean updateMenuItem(int itemId, String itemName);

    boolean deleteMenuItem(int itemId);
}
