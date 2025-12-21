import axios from "axios";

export default {
  createMenu(menuDto) {
    return axios.post("/menus", menuDto);
  },

  getMenuByCookoutId(cookoutId) {
    return axios.get(`/menus/${cookoutId}`);
  },

  updateMenuItem(itemId, itemName) {
    return axios.put(`/menus/items/${itemId}`, itemName, {
      headers: { "Content-Type": "text/plain" }
    });
  },

  deleteMenuItem(itemId) {
    return axios.delete(`/menus/items/${itemId}`);
  }
};
