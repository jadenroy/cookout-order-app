import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../context/UserContext";
import { Link } from "react-router-dom";
import CookoutService from "../../services/CookoutService";
import styles from "./CookoutsListView.module.css";

export default function CookoutsListView() {
  const [cookouts, setCookouts] = useState([]);
  const [attendeeMap, setAttendeeMap] = useState({});
  const { user } = useContext(UserContext);

  useEffect(() => {
    CookoutService.getAllCookouts()
      .then((res) => {
        setCookouts(res.data);

        res.data.forEach((cookout) => {
          CookoutService.getAttendees(cookout.id)
            .then((attRes) => {
              setAttendeeMap((prev) => ({
                ...prev,
                [cookout.id]: attRes.data.map((a) => a.id),
              }));
            })
            .catch(() => {
              setAttendeeMap((prev) => ({
                ...prev,
                [cookout.id]: [],
              }));
            });
        });
      })
      .catch((err) => console.error("Failed to load cookouts", err));
  }, []);

  const formatDateTime = (timestamp) =>
    new Date(timestamp).toLocaleString(undefined, {
      dateStyle: "medium",
      timeStyle: "short",
    });

  const getCookoutRole = (cookout) => {
    if (!user) return "none";
    if (cookout.hostId === user.id) return "host";

    const attendees = attendeeMap[cookout.id] || [];
    if (attendees.includes(user.id)) return "attending";

    return "none";
  };

  return (
    <div className="user-profile-bg">
      <div className={styles.container}>
        <h2 className={styles.title}>All Cookouts</h2>

        {cookouts.map((c) => {
          const role = getCookoutRole(c);

          return (
            <div
              key={c.id}
              className={`${styles.cookoutCard} ${
                role !== "none" ? styles[role] : ""
              }`}
            >
              <h3>
                {c.name}
                {role !== "none" && (
                  <span className={styles.badge}>
                    {role === "host" ? "Host" : "Attending"}
                  </span>
                )}
              </h3>

              <p>{c.location}</p>
              <p className={styles.date}>{formatDateTime(c.startTime)}</p>

              <Link
                to={`/cookout/${c.id}`}
                className={styles.detailsLink}
              >
                View Details
              </Link>
            </div>
          );
        })}
      </div>
    </div>
  );
}
