import { useEffect, useState } from "react";
import InvitationsService from "../../services/InvitationsService";
import styles from "./InvitationsListView.module.css";
import { useAuth } from "../../context/UserContext";

export default function InvitationsListView() {

    const { user } = useAuth();
    const [invitations, setInvitations] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!user) return;

        InvitationsService.getInvitationsForUser()
            .then(res => {
                setInvitations(res.data.filter(inv => inv.active));
            })
            .catch(() => {
                setError("Failed to load invitations.");
            });
    }, [user]);

    async function handleAccept(invitation) {
        try {
            // Optimistically remove invite immediately (prevents ghost feeling)
            setInvitations(prev =>
                prev.filter(i => i.invitationId !== invitation.invitationId)
            );

            await InvitationsService.acceptInvitation(invitation.invitationId);

            // Notify cookouts list to refresh
            window.dispatchEvent(new Event("cookouts-updated"));
        } catch (err) {
            console.error("Failed to accept invitation", err);
            setError("Failed to accept invitation.");
        }
    }

    async function handleDecline(invitation) {
        try {
            // Optimistically remove invite immediately
            setInvitations(prev =>
                prev.filter(i => i.invitationId !== invitation.invitationId)
            );

            await InvitationsService.declineInvitation(invitation.invitationId);
        } catch (err) {
            console.error("Failed to decline invitation", err);
            setError("Failed to decline invitation.");
        }
    }

    return (
        <div className={`user-profile-bg ${styles.pageWrapper}`}>
            <h2 className={styles.title}>Your Invitations</h2>

            {error && <p className={styles.error}>{error}</p>}

            {invitations.length === 0 ? (
                <p className={styles.empty}>No invitations yet.</p>
            ) : (
                <ul className={styles.invitationsList}>
                    {invitations.map(inv => {
                        const isHost = inv.fromId === user.id;

                        return (
                            <li
                                key={inv.invitationId}
                                className={`${styles.inviteCard} ${
                                    isHost ? styles.hostInvite : styles.guestInvite
                                }`}
                            >
                                <div className={styles.inviteInfo}>
                                    <span className={styles.cookoutName}>
                                        {inv.cookoutName}
                                    </span>
                                    <span className={styles.inviteFrom}>
                                        Invite from {inv.fromUsername}
                                    </span>
                                </div>

                                <div className={styles.actions}>
                                    <button
                                        className={`${styles.actionBtn} ${styles.accept}`}
                                        onClick={() => handleAccept(inv)}
                                    >
                                        Accept
                                    </button>

                                    <button
                                        className={`${styles.actionBtn} ${styles.decline}`}
                                        onClick={() => handleDecline(inv)}
                                    >
                                        Decline
                                    </button>
                                </div>
                            </li>
                        );
                    })}
                </ul>
            )}
        </div>
    );
}
