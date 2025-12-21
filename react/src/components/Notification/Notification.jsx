import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faXmark } from '@fortawesome/free-solid-svg-icons';

import styles from './Notification.module.css';

export default function Notification({ notification, clearNotification }) {
  if (!notification) {
    return null;
  }

  return (
    <div className={styles.notifications}>
      <div role="alert" className={`${styles.alert} ${styles[notification.type]}`}>
        <span className={styles.message}>{notification.message}</span>
        <button className="icon-button" onClick={clearNotification}>
          <FontAwesomeIcon icon={faXmark} title="Close" />
        </button>
      </div>
    </div>
  );
}
