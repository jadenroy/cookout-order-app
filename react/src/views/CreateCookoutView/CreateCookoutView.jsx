import { useState } from "react";
import { useNavigate } from "react-router-dom";
import CookoutService from "../../services/CookoutService";
import styles from "./CreateCookoutView.module.css";

export default function CreateCookoutView() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [location, setLocation] = useState("");
  const [description, setDescription] = useState("");
  const [startTime, setStartTime] = useState("");

  const [error, setError] = useState(null);

  function handleSubmit(e) {
    e.preventDefault();

    const cookout = {
      name,
      location,
      description,
      startTime,     // backend accepts LocalDateTime
      chefId: null,
      menuId: null
    };

    CookoutService.createCookout(cookout)
      .then((response) => {
        const newCookout = response.data;
        navigate(`/cookout/${newCookout.id}`);
      })
      .catch((err) => {
        const message =
          err.response?.data?.message || "Failed to create cookout.";
        setError(message);
      });
  }

  return (
    <div className="user-profile-bg">
    <div className={styles.container}>
      <h2 className={styles.title}>Create Cookout</h2>

      {error && <p className={styles.error}>{error}</p>}

      <form onSubmit={handleSubmit} className={styles.form}>
        <div className={styles.formControls}>
          <label>Name:</label>
          <input
            type="text"
            required
            value={name}
            onChange={(e) => setName(e.target.value)}
          />
        </div>

        <div className={styles.formControls}>
          <label>Location:</label>
          <input
            type="text"
            required
            value={location}
            onChange={(e) => setLocation(e.target.value)}
          />
        </div>

        <div className={styles.formControls}>
          <label>Description:</label>
          <textarea
            required
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          ></textarea>
        </div>

        <div className={styles.formControls}>
          <label>Start Time:</label>
          <input
            type="datetime-local"
            required
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
          />
        </div>

        <button className={styles.submitBtn} type="submit">
          Create
        </button>
      </form>
    </div>
    </div>
  );
}
