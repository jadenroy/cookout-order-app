import axios from "axios";

const UserService = {
  getAllUsers() {
    return axios.get("/users");
  },

  getUserProfile(userId) {
    return axios.get(`/users/${userId}`);
  }
};

export default UserService;
