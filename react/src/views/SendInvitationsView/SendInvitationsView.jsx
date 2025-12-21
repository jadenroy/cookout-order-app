import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import InvitationsService from "../../services/InvitationsService";
import CookoutService from "../../services/CookoutService";
import UserService from "../../services/UserService";
import styles from "./SendInvitationsView.module.css";

export default function SendInvitationsView() {

    const { id: cookoutId } = useParams();
    const [users, setUsers] = useState([]);
    const [cookout, setCookout] = useState(null);

    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [invitedUserIds, setInvitedUserIds] = useState([]);

    // Load users
    useEffect(() => {
        UserService.getAllUsers()
            .then(res => setUsers(res.data))
            .catch(() => setErrorMessage("Failed to load users."));
    }, []);

    // Load cookout
    useEffect(() => {
        CookoutService.getCookoutById(cookoutId)
            .then(res => setCookout(res.data))
            .catch(() => setErrorMessage("Failed to load cookout."));
    }, [cookoutId]);

    // Auto-clear success message
    useEffect(() => {
        if (!successMessage) return;
        const timer = setTimeout(() => setSuccessMessage(""), 1500);
        return () => clearTimeout(timer);
    }, [successMessage]);

    function handleSendInvite(userId) {
        // Clear old messages BEFORE sending
        setErrorMessage("");
        setSuccessMessage("");

        InvitationsService.sendInvitation(userId, Number(cookoutId))
            .then(() => {
                setSuccessMessage("Invitation sent!");
                setInvitedUserIds(prev => [...prev, userId]);
            })
            .catch(err => {
                // 🔒 ONLY show error if backend actually failed
                if (err.response && err.response.status >= 400) {
                    setErrorMessage("Failed to send invitation.");
                }
            });
    }

    return (
        <div className={`user-profile-bg ${styles.pageWrapper}`}>
            <h2 className={styles.title}>
                Invite Guests to: {cookout ? cookout.name : "Cookout"}
            </h2>

            {successMessage && (
                <p className={styles.message}>{successMessage}</p>
            )}

            {errorMessage && (
                <p className={styles.error}>{errorMessage}</p>
            )}

            <ul className={styles.userList}>
                {users.map(user => (
                    <li key={user.id} className={styles.userItem}>
                        {user.username}
                        <button
                            className={styles.inviteBtn}
                            onClick={() => handleSendInvite(user.id)}
                            disabled={invitedUserIds.includes(user.id)}
                        >
                            {invitedUserIds.includes(user.id) ? "Invited" : "Invite"}
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
}
