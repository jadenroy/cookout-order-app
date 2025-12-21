import { Link } from "react-router-dom";
import styles from "./HomeView.module.css";

export default function HomeView() {
  return (
    <div className="user-profile-bg">
      <div className={styles.container}>
        <header className={styles.header}>
          <h1 className={styles.title}>
            Welcome to Cookout Planner 🍔
          </h1>

          <p className={styles.subtitle}>
            Easily plan, organize, and manage your cookout events.
          </p>

          <div className={styles.cookoutBtn}>
            <Link to="/cookouts">View Your Cookouts</Link>
          </div>
        </header>

        <section className={styles.featureSection}>
          <h3 className={styles.sectionTitle}>What Can you do?</h3>

          <div className={styles.featureGrid}>
            <div className={styles.featureCard}>
              <h3>
                <Link to="/cookout/new">Create Cookout</Link>
              </h3>
              <p>Set up your next event with location, time, and details.</p>
              <span className={styles.helperText}>
                Start here to unlock all features
              </span>
            </div>

            <div className={`${styles.featureCard} ${styles.inactive}`}>
              <h3>Invite Guests</h3>
              <p>Invite attendees and keep track of who is coming.</p>
              <span className={styles.helperText}>
                Available after selecting a cookout
              </span>
            </div>

            <div className={`${styles.featureCard} ${styles.inactive}`}>
              <h3>Plan your Menu</h3>
              <p>Assign menu options, track who is bringing what, and more.</p>
              <span className={styles.helperText}>
                Available inside a cookout
              </span>
            </div>
          </div>
        </section>
      </div>
    </div>
  );
}
