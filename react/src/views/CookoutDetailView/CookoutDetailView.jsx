import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";
import CookoutService from "../../services/CookoutService";
import MenuService from "../../services/MenuService";
import { useAuth } from "../../context/UserContext";
import styles from "./CookoutDetailView.module.css";

export default function CookoutDetailView() {
  const { id } = useParams();
  const { user } = useAuth();

  const [cookout, setCookout] = useState(null);
  const [attendees, setAttendees] = useState([]);
  const [menu, setMenu] = useState(null);
  const [error, setError] = useState(null);

  function loadAll() {
    CookoutService.getCookoutById(id).then((res) => setCookout(res.data));
    CookoutService.getAttendees(id).then((res) => setAttendees(res.data));

    MenuService.getMenuByCookoutId(id)
      .then((res) => setMenu(res.data))
      .catch(() => setMenu(null));
  }

  useEffect(() => {
    loadAll();
  }, [id]);

  function handleAddAttendee() {
    CookoutService.addAttendee(id)
      .then(() => loadAll())
      .catch((err) => {
        const message =
          err.response?.data?.message || "Failed to add attendee.";
        setError(message);
      });
  }

  if (!cookout) return <p>Loading...</p>;

  const isHost = user?.id === cookout.hostId;
  const isChef = cookout.chefId && user?.id === cookout.chefId;
  const canViewKitchen = isHost || isChef;

  return (
    <div className="user-profile-bg">
      <div className={styles.container}>
        <h2 className={styles.title}>{cookout.name}</h2>

        <div className={styles.detailsCard}>
          <p>
            📍 <strong>Location:</strong> {cookout.location}
          </p>
          <p>
            📝 <strong>Description:</strong> {cookout.description}
          </p>
        </div>

        {/* ACTION BUTTONS */}
        <div className={styles.actions}>
          <Link to={`/cookout/${id}/menu/new`} className={styles.actionBtn}>
            🍽️ Manage Menu
          </Link>

          <Link to={`/cookout/${id}/order`} className={styles.actionBtn}>
            🧾 Place Order
          </Link>

          {canViewKitchen && (
            <Link to={`/cookout/${id}/orders`} className={styles.actionBtn}>
              👨‍🍳 View Orders (Kitchen)
            </Link>
          )}

          <Link to={`/cookout/${id}/invite`} className={styles.actionBtn}>
            👥 Invite Guests
          </Link>
        </div>

        {/* MENU */}
        {menu && menu.items && menu.items.length > 0 && (
          <>
            <h3 className={styles.sectionHeader}>Menu</h3>
            <ul>
              {menu.items.map((item) => (
                <li key={item.id}>{item.itemName}</li>
              ))}
            </ul>
          </>
        )}

        {/* ATTENDEES */}
        <h3 className={styles.sectionHeader}>
          Current Attendees ({attendees.length})
        </h3>

        <div className={styles.attendeeList}>
          {attendees.map((a) => (
            <div key={a.id} className={styles.attendeeCard}>
              {a.username}
            </div>
          ))}
        </div>

        <button className={styles.addBtn} onClick={handleAddAttendee}>
          ➕ Add Me as Attendee
        </button>

        {error && <p className={styles.error}>{error}</p>}
      </div>
    </div>
  );
}
