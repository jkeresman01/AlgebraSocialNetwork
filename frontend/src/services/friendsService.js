import axios from "axios";

const API_BASE = import.meta.env.VITE_ALGEBRA_SOCIAL_NETWORK_BASE_URL;

const getAuthConfig = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem("token")}`,
  },
});

export const sendFriendRequest = async (userId) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/friends/request/${userId}`,
      {},
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const approveFriendRequest = async (requestId) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/friends/approve/${requestId}`,
      {},
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const declineFriendRequest = async (requestId) => {
  try {
    return await axios.post(
      `${API_BASE}/api/v1/friends/decline/${requestId}`,
      {},
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const removeFriend = async (userId) => {
  try {
    return await axios.delete(
      `${API_BASE}/api/v1/friends/remove/${userId}`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const getPendingFriendRequests = async () => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/friends/requests`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error: ${e}`);
  }
};

export const getFriends = async () => {
  try {
    return await axios.get(`${API_BASE}/api/v1/friends/all`, getAuthConfig());
  } catch (e) {
    console.error(`Error fetching friends: ${e}`);
  }
};

export const getNonFriends = async () => {
  try {
    return await axios.get(
      `${API_BASE}/api/v1/friends/non-friends`,
      getAuthConfig(),
    );
  } catch (e) {
    console.error(`Error fetching non-friends: ${e}`);
  }
};
