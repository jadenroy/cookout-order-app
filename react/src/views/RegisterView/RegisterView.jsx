import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import Notification from '../../components/Notification/Notification';

import styles from './RegisterView.module.css';

export default function RegisterView() {
  const navigate = useNavigate();

  const [notification, setNotification] = useState(null);

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  function handleSubmit(event) {
    event.preventDefault();

    if (password !== confirmPassword) {
      setNotification({
        type: 'error',
        message: 'Passwords do not match.',
      });
      return; // IMPORTANT FIX
    }

    AuthService.register({
      username,
      password,
      confirmPassword,
      role: 'user'
    })
      .then(() => {
        setNotification({
          type: 'success',
          message: 'Registration successful! Redirecting...',
        });

        setTimeout(() => navigate('/login'), 2000);
      })
      .catch((error) => {
        const message =
          error.response?.data?.message || 'Registration failed.';
        setNotification({ type: 'error', message });
      });
  }

  return (
    <div className={`user-profile-bg ${styles.pageWrapper}`}>
    <div className={styles.registerCard}>
      <h2 className={styles.title}>Register</h2>
      <p className={styles.subtitle}>Register here</p>

      <Notification
        notification={notification}
        clearNotification={() => setNotification(null)}
      />

      <form className={styles.form} onSubmit={handleSubmit}>
        <div className={styles.formControls}>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            required
            autoComplete="username"
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>

        <div className={styles.formControls}>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            required
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>

        <div className={styles.formControls}>
          <label htmlFor="confirmPassword">Confirm Password:</label>
          <input
            type="password"
            id="confirmPassword"
            value={confirmPassword}
            required
            onChange={(e) => setConfirmPassword(e.target.value)}
          />
        </div>

        <button type="submit" className={styles.formBtn}>
          Register
        </button>

        <Link className={styles.loginLink} to="/login">Have an account? Log in</Link>
      </form>
    </div>
    </div>
  );
}
