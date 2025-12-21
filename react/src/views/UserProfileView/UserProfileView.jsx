import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../context/UserContext";
import CookoutService from "../../services/CookoutService";
import styles from "./UserProfileView.module.css";
import { Link } from "react-router-dom";

export default function UserProfileView() {
  const { user } = useContext(UserContext);

  const [view, setView] = useState("attending");
  const [attending, setAttending] = useState([]);
  const [hosted, setHosted] = useState([]);

  // Load both lists on mount
  useEffect(() => {
    loadAttending();
    loadHosted();
  }, []);

  const loadAttending = async () => {
    try {
      const res = await CookoutService.getAttending();
      setAttending(res.data);
      setView("attending");
    } catch (error) {
      console.error("Error fetching attending list", error);
    }
  };

  const loadHosted = async () => {
    try {
      const res = await CookoutService.getHosted();
      setHosted(res.data);
      setView("hosted");
    } catch (error) {
      console.error("Error fetching hosted list", error);
    }
  };

  return (
    <div className={`user-profile-bg ${styles.pageWrapper}`}>
      <div className={styles.profileCard}>

        <h1 className={styles.title}>User Profile</h1>
        <p className={styles.greeting}>Hello, {user.username}!</p>

        {/* Toggle Buttons */}
      <div className={styles.toggle}>
        <button
          onClick={() => setView("attending")}
          className={view === "attending" ? styles.active : ""}
        >
          Attending
        </button>

        <button
          onClick={() => setView("hosted")}
          className={view === "hosted" ? styles.active : ""}
        >
          Hosted
        </button>
      </div>   

      <h2 className={styles.sectionTitle}>
        {view === "attending"
          ? "Cookouts you are attending:"
          : "Cookouts you are hosting:"}
      </h2> 

      {/* Attending List */}
      {view === "attending" && (
        <>
          {attending.length > 0 ? (
            <ul className={styles.list}>
              {attending.map((c) => (
                <li key={c.id} className={styles.cookoutCard}>
                  <Link to={`/cookout/${c.id}`}>
                    <h3 className={styles.cardName}>{c.name}</h3>
                  </Link>
                  <p className={styles.cardLocation}>{c.location}</p>
                </li>
              ))}
            </ul>
          ) : (
            <p className={styles.empty}>No cookouts attended yet.</p>
          )}
        </>
      )}

      {/* Hosted List */}
      {view === "hosted" && (
        <>
          {hosted.length > 0 ? (
            <ul className={styles.list}>
              {hosted.map((c) => (
                <li key={c.id} className={styles.cookoutCard}>
                 <Link to={`/cookout/${c.id}`}>
                  <h3 className={styles.cardName}>{c.name}</h3>
                 </Link>
                 <p className={styles.cardLocation}>{c.location}</p>
                </li>
              ))}
            </ul>
          ) : (
            <p className={styles.empty}>No cookouts hosted yet.</p>
          )}
        </>
      )}
    </div>
  </div>
  );
}