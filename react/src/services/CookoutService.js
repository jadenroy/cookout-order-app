import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:9000",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

const CookoutService = {
  getAllCookouts() {
    return api.get("/cookouts");
  },

  getCookoutById(id) {
    return api.get(`/cookouts/${id}`);
  },

  createCookout(cookout) {
    return api.post("/cookouts", cookout);
  },

  getAttendees(cookoutId) {
    return api.get(`/cookouts/${cookoutId}/attendees`);
  },

  addAttendee(cookoutId) {
    return api.post(`/cookouts/${cookoutId}/attendees`);
  },

  // NEW
  getAttending() {
    return api.get("/cookouts/attending");
  },

  getHosted() {
    return api.get("/cookouts/hosted");
  }
};

export default CookoutService;
