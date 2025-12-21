import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import Notification from '../../components/Notification/Notification';
import { useAuth } from '../../context/UserContext';
import styles from './LoginView.module.css';

export default function LoginView() {
  const { setUser, setToken } = useAuth();
  const navigate = useNavigate();
  const [notification, setNotification] = useState(null);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  function handleSubmit(event) {
    event.preventDefault();

    AuthService.login({ username, password })
      .then(response => {
        const { user, token } = response.data;

        setUser(user);
        setToken(token);

        navigate('/');
      })
      .catch(err => {
        const message = err.response?.data?.message || "Login failed.";
        setNotification({ type: "error", message });
      });
  }

  return (
    <div className={`user-profile-bg ${styles.pageWrapper}`} id="view-login">
      <div className={styles.loginCard}>
        <h2 className={styles.title}>Log in</h2>
        <p className={styles.subtitle}>Log in to plan your next cookout</p>
     
        <Notification
        notification={notification}
        clearNotification={() => setNotification(null)}
      />

      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.formControls}>
          <label htmlFor="username">Username:</label>
          <input
            id="username"
            type="text"
            required
            value={username}
            onChange={e => setUsername(e.target.value)}
          />
        </div>

        <div className={styles.formControls}>
          <label htmlFor="password">Password:</label>
          <input
            id="password"
            type="password"
            required
            value={password}
            onChange={e => setPassword(e.target.value)}
          />
        </div>

        <button className={styles.loginBtn} type="submit">
          Sign in
        </button>

        <Link className={styles.registerLink} to="/register">
        New? Register here!
        </Link>
      </form>
     </div>
    </div>
  );
}
