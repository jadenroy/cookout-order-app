import axios from "axios";

const API = "/invitations";

export default {

    getInvitationsForUser() {
        return axios.get(API);
    },

    acceptInvitation(invitationId) {
        return axios.post(`${API}/${invitationId}/accept`);
    },

    declineInvitation(invitationId) {
        return axios.post(`${API}/${invitationId}/decline`);
    },

    sendInvitation(toId, cookoutId) {
        return axios.post(API, { toId, cookoutId });
    }
};
