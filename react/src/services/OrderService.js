import axios from "axios";

const API = "/orders";

export default {
  createOrder(orderDto) {
    return axios.post(API, orderDto);
  },

  getOrdersByCookout(cookoutId) {
    return axios.get(`${API}/cookout/${cookoutId}`);
  },

  finishOrder(orderId) {
    return axios.put(`${API}/${orderId}/finish`);
  }
};
